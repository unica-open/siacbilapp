/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.richiestaeconomale;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.richiestaeconomale.GestioneRichiestaEconomalePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRichiestaEconomaleHelper;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, richiesta economale.
 * 
 * @author Simona Paggio
 * @version 1.0.0 - 14/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneRichiestaEconomaleInsPrimaNotaIntegrataBaseAction<M extends GestioneRichiestaEconomalePrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<RichiestaEconomale, ConsultaRegistrazioneMovFinRichiestaEconomaleHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3584038134823856934L;

	@Autowired private transient RichiestaEconomaleService richiestaEconomaleService;
	
	/** Nome del model del completamento e validazione della richiesta economale. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_RICHIESTA_ECONOMALE_FIN = "CompletaValidaRichiestaEconomaleInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento e validazione della richiesta economale. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_RICHIESTA_ECONOMALE_GSA = "CompletaValidaRichiestaEconomaleInsPrimaNotaIntegrataGSAModel";
	/** Nome del model del completamento della richiesta economale, modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_RICHIESTA_ECONOMALE_FIN = "CompletaRichiestaEconomaleInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento della richiesta economale, modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_RICHIESTA_ECONOMALE_GSA = "CompletaRichiestaEconomaleInsPrimaNotaIntegrataGSAModel";
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile();
		
		// Recupero registrazione dalla sessione
		RegistrazioneMovFin registrazioneMovFin = sessionHandler.getParametro(BilSessionParameter.REGISTRAZIONEMOVFIN);
		model.setRegistrazioneMovFin(registrazioneMovFin);
		log.debug(methodName, "registrazione ottenuta");
		
		if(inEsecuzioneRegistrazioneMovFin()) {
			return INPUT;
		}
		
		sessionHandler.setParametro(BilSessionParameter.RICHIESTA_ECONOMALE, null);
		
		RichiestaEconomale richiestaEconomale = (RichiestaEconomale) model.getRegistrazioneMovFin().getMovimento();
		model.setRichiestaEconomale(richiestaEconomale);
		
		if(model.getPrimaNota() == null){
			model.setPrimaNota(new PrimaNota());
			model.impostaDatiNelModel();
		}
		computaStringheDescrizioneDaRegistrazione(model.getRegistrazioneMovFin());
		ricavaSoggettoDaMovimento(richiestaEconomale);

		try{
			caricaListaCausaleEP(false);
			caricaListaClassi();
			caricaListaTitoli();
		}catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			// Metto in sessione gli errori ed esco
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		getDatiDaRichiestaEconomale(richiestaEconomale);
		return SUCCESS;
	}

	/**
	 * Ottiene i dati della richiesta economale
	 * @param richiestaEconomale la richiesta economale da cui ottenere i dati
	 */
	private void getDatiDaRichiestaEconomale(RichiestaEconomale richiestaEconomale){
		// Carico da servizio
		RicercaDettaglioRichiestaEconomale req = model.creaRequestRicercaDettaglioRichiestaEconomale(richiestaEconomale);
		logServiceRequest(req);
		
		RicercaDettaglioRichiestaEconomaleResponse res = richiestaEconomaleService.ricercaDettaglioRichiestaEconomale(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return;
		}
		
		RichiestaEconomale richiestaEconomaleServizio = res.getRichiestaEconomale();
		sessionHandler.setParametro(BilSessionParameter.RICHIESTA_ECONOMALE, richiestaEconomaleServizio);
		
		model.setRichiestaEconomale(richiestaEconomaleServizio);
		model.setClassificatoreGerarchico(model.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione().getMacroaggregato());
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinRichiestaEconomaleHelper(richiestaEconomale, model.isGestioneUEB()));
		
	}
	
	/**
	 * Ottiene il soggetto dal movimento.
	 * 
	 * @param richiestaEconomale la richiesta economale da cui ottenere il soggetto
	 */
	private void ricavaSoggettoDaMovimento(RichiestaEconomale richiestaEconomale) {
		if (richiestaEconomale != null) {
			model.setSoggettoMovimentoFIN(richiestaEconomale.getSoggetto());
		}
	}
	
	@Override
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin){
		StringBuilder movimento = new StringBuilder();
		StringBuilder descrizione = new StringBuilder();
		RichiestaEconomale richiestaEconomale = (RichiestaEconomale) registrazioneMovFin.getMovimento();
		
		descrizione.append("RichEcon")
			.append(richiestaEconomale.getMovimento().getNumeroMovimento());
		if(StringUtils.isNotBlank(richiestaEconomale.getDescrizioneDellaRichiesta())) {
			descrizione.append(" ")
				.append(richiestaEconomale.getDescrizioneDellaRichiesta());
		}
		movimento.append(richiestaEconomale.getMovimento().getNumeroMovimento())
			.append(" (richEcon)");

		model.setDescrMovimentoFinanziario(movimento.toString());
		model.getPrimaNota().setDescrizione(descrizione.toString());
		model.getPrimaNota().setDataRegistrazione(new Date());
	}

	/**
	 * Validazione per il metodo {@link #completeSalva()}.
	 */
	public void validateCompleteSalva() {
		// Se non ho la prima nota, qualcosa e' andato storto: esco subito
		checkNotNull(model.getPrimaNota(), "Prima Nota Integrata ", true);
		checkNotNullNorInvalidUid(model.getCausaleEP(), "CausaleEP");
		checkNotNull(model.getPrimaNota().getDataRegistrazione(), "Data registrazione ");
		checkNotNullNorEmpty(model.getPrimaNota().getDescrizione(), "Descrizione ");

		List<MovimentoDettaglio> listaMovimentiDettaglioFinal = checkScrittureCorrette();
		
		// Esco se ho errori
		if(hasErrori()) {
			return;
		}
		
		MovimentoEP movEP = new MovimentoEP();
		movEP.setCausaleEP(model.getCausaleEP());
		movEP.setRegistrazioneMovFin(model.getRegistrazioneMovFin());
		model.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		movEP.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		
		model.getListaMovimentoEP().add(movEP);
	}

	/**
	 * Completamento per lo step 2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	
	public String completeSalva() {
		final String methodName = "completeSalva";
		
		aggiornaNumeroRiga();
		// L'inserisci e' sempre presente da registrazione quindi chiamo il completa
		
		// Inserimento della causale
		RegistraPrimaNotaIntegrata req = model.creaRequestRegistraPrimaNotaIntegrata();
		logServiceRequest(req);
		RegistraPrimaNotaIntegrataResponse res = primaNotaService.registraPrimaNotaIntegrata(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'inserimento della Prima Nota integrata");
			addErrori(res);
			return INPUT;
		}
		
		log.info(methodName, "registrata correttamente Prima Nota integrata con uid " + res.getPrimaNota().getUid());
		if (model.isValidazione()) {
			log.info(methodName, "validata correttamente Prima Nota integrata con uid " + res.getPrimaNota().getUid());
		}
		
		// Imposto i dati della causale restituitimi dal servizio
		model.setPrimaNota(res.getPrimaNota());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}

	@Override
	protected ElementoPianoDeiConti ottieniElementoPianoDeiContiDaMovimento() {
		return model.getRichiestaEconomale() != null
			&& model.getRichiestaEconomale().getImpegno() != null
			&& model.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione() != null
				? model.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		return model.getRichiestaEconomale() != null ? model.getRichiestaEconomale().getSoggetto() : null;
	}
}

