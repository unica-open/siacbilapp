/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.rendicontorichiesta;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.rendicontorichiesta.GestioneRendicontoRichiestaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRendicontoRichiestaHelper;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, rendiconto richiesta economale.
 * 
 * @author Simona Paggio
 * @version 1.0.0 - 14/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneRendicontoRichiestaInsPrimaNotaIntegrataBaseAction<M extends GestioneRendicontoRichiestaPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<RendicontoRichiesta, ConsultaRegistrazioneMovFinRendicontoRichiestaHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3584038134823856934L;

	@Autowired private transient RichiestaEconomaleService richiestaEconomaleService;
	
	/** Nome del model del completamento e validazione del rendiconto richiesta economale. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_RENDICONTO_RICHIESTA_FIN = "CompletaValidaRendicontoRichiestaInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento e validazione del rendiconto richiesta economale. Modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_RENDICONTO_RICHIESTA_GSA = "CompletaValidaRendicontoRichiestaInsPrimaNotaIntegrataGSAModel";
	/** Nome del model del completamento del rendiconto richiesta economale, modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_RENDICONTO_RICHIESTA_FIN = "CompletaRendicontoRichiestaInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento del rendiconto richiesta economale, modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_RENDICONTO_RICHIESTA_GSA = "CompletaRendicontoRichiestaInsPrimaNotaIntegrataGSAModel";
	
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
		
		sessionHandler.setParametro(BilSessionParameter.RENDICONTO_RICHIESTA, null);
		
		RendicontoRichiesta rendicontoRichiesta = (RendicontoRichiesta) model.getRegistrazioneMovFin().getMovimento();
		model.setRendicontoRichiesta(rendicontoRichiesta);
		
		if(model.getPrimaNota() == null){
			model.setPrimaNota(new PrimaNota());
			model.impostaDatiNelModel();
		}
		computaStringheDescrizioneDaRegistrazione(model.getRegistrazioneMovFin());
		ricavaSoggettoDaMovimento(rendicontoRichiesta);

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
		
		getDatiDaRendicontoRichiesta(rendicontoRichiesta);
		return SUCCESS;
	}

	/**
	 * Ottiene i dati della richiesta economale
	 * @param rendicontoRichiesta la richiesta economale da cui ottenere i dati
	 */
	private void getDatiDaRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta){
		// Carico da servizio
		RicercaDettaglioRendicontoRichiesta req = model.creaRequestRicercaDettaglioRendicontoRichiesta(rendicontoRichiesta);
		logServiceRequest(req);
		
		RicercaDettaglioRendicontoRichiestaResponse res = richiestaEconomaleService.ricercaDettaglioRendicontoRichiesta(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return;
		}
		
		RendicontoRichiesta rendicontoRichiestaService = res.getRendicontoRichiesta();
		sessionHandler.setParametro(BilSessionParameter.RENDICONTO_RICHIESTA, rendicontoRichiestaService);
		
		model.setRendicontoRichiesta(rendicontoRichiestaService);
		model.setClassificatoreGerarchico(model.getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione().getMacroaggregato());
		
		// SIAC-5294
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinRendicontoRichiestaHelper(rendicontoRichiesta, model.isGestioneUEB()));
	}
	
	/**
	 * Ottiene il soggetto dal movimento.
	 * 
	 * @param rendicontoRichiesta la richiesta economale da cui ottenere il soggetto
	 */
	private void ricavaSoggettoDaMovimento(RendicontoRichiesta rendicontoRichiesta) {
		if (rendicontoRichiesta != null) {
			model.setSoggettoMovimentoFIN(rendicontoRichiesta.getRichiestaEconomale().getSoggetto());
		}
	}
	
	@Override
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin){
		StringBuilder movimento = new StringBuilder();
		StringBuilder descrizione = new StringBuilder();
		RendicontoRichiesta rendicontoRichiesta = (RendicontoRichiesta) registrazioneMovFin.getMovimento();
		
		descrizione.append("RichEcon")
			.append(rendicontoRichiesta.getMovimento().getNumeroMovimento());
		if(StringUtils.isNotBlank(rendicontoRichiesta.getRichiestaEconomale().getDescrizioneDellaRichiesta())) {
			descrizione.append(" ")
				.append(rendicontoRichiesta.getRichiestaEconomale().getDescrizioneDellaRichiesta());
		}
		movimento.append(rendicontoRichiesta.getMovimento().getNumeroMovimento())
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
		return model.getRendicontoRichiesta() != null
			&& model.getRendicontoRichiesta().getImpegno() != null
			&& model.getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione() != null
				? model.getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		return model.getRendicontoRichiesta() != null
			&& model.getRendicontoRichiesta().getRichiestaEconomale() != null
				? model.getRendicontoRichiesta().getRichiestaEconomale().getSoggetto()
				: null;
	}

}

