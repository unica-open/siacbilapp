/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.ordinativo.pagamento;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento.GestioneOrdinativoPagamentoPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinOrdinativoPagamentoHelper;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfinser.frontend.webservice.OrdinativoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, ordinativo di pagamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneOrdinativoPagamentoInsPrimaNotaIntegrataBaseAction<M extends GestioneOrdinativoPagamentoPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<OrdinativoPagamento, ConsultaRegistrazioneMovFinOrdinativoPagamentoHelper, M>  {

	/** Per la serializzazione **/
	private static final long serialVersionUID = 4357034911987810846L;

	@Autowired private transient OrdinativoService ordinativoService;
	
	/** Nome del model del completamento e validazione della reg con nuova primanota x la sessione */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_ORDINATIVO_PAGAMENTO_FIN = "CompletaOrdinativoPagamentoInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento e validazione della reg con nuova primanota x la sessione */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_ORDINATIVO_PAGAMENTO_GSA = "CompletaOrdinativoPagamentoInsPrimaNotaIntegrataGSAModel";
	/** Nome del model del completamento della reg con nuova primanota per la sessione */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_ORDINATIVO_PAGAMENTO_FIN = "CompletaValidaOrdinativoPagamentoInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento della reg con nuova primanota per la sessione */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_ORDINATIVO_PAGAMENTO_GSA = "CompletaValidaOrdinativoPagamentoInsPrimaNotaIntegrataGSAModel";
	
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
		
		sessionHandler.setParametro(BilSessionParameter.ORDINATIVO_PAGAMENTO, null);
		
		Ordinativo ordinativo = (Ordinativo) model.getRegistrazioneMovFin().getMovimento();
		
		if(model.getPrimaNota() == null){
			model.setPrimaNota(new PrimaNota());
			model.impostaDatiNelModel();
		}
		ricavaSoggettoDaMovimento(ordinativo);

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
		
		getDatiDaOrdinativo(ordinativo);
		computaStringheDescrizioneDaRegistrazione(model.getRegistrazioneMovFin());
		return SUCCESS;
	}

	/**
	 * Ottiene i dati dell'ordinativo di incasso
	 * @param ordinativo l'ordinativo da cui ottenere i dati
	 */
	private void getDatiDaOrdinativo(Ordinativo ordinativo){
		// Carico da servizio
		RicercaOrdinativoPagamentoPerChiave req = model.creaRequestRicercaOrdinativoPagamentoPerChiave(ordinativo);
		logServiceRequest(req);
		
		RicercaOrdinativoPagamentoPerChiaveResponse res = ordinativoService.ricercaOrdinativoPagamentoPerChiave(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return;
		}
		
		OrdinativoPagamento ordinativoServizio = res.getOrdinativoPagamento();
		List<SubOrdinativoPagamento> listaSubOrdinativo = defaultingList(ordinativoServizio.getElencoSubOrdinativiDiPagamento());
		sessionHandler.setParametro(BilSessionParameter.ORDINATIVO_PAGAMENTO, ordinativoServizio);
		
		model.setOrdinativoPagamento(ordinativoServizio);
		model.setClassificatoreGerarchico(model.getOrdinativoPagamento().getCapitoloUscitaGestione().getMacroaggregato());
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinOrdinativoPagamentoHelper(ordinativoServizio, listaSubOrdinativo, model.isGestioneUEB()));
	}

	/**
	 * Ottiene il soggetto dal movimento.
	 * 
	 * @param ordinativo l'ordinativo da cui ottenere il soggetto
	 */
	private void ricavaSoggettoDaMovimento(Ordinativo ordinativo) {
		if (ordinativo != null) {
			model.setSoggettoMovimentoFIN(ordinativo.getSoggetto());
		}
	}
	
	@Override
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin){
		StringBuilder movimento = new StringBuilder();
		StringBuilder descrizione = new StringBuilder();
		Ordinativo ordinativoIncasso = (Ordinativo) registrazioneMovFin.getMovimento();
		
		descrizione.append("Ord Pagamento ")
			.append(ordinativoIncasso.getAnno())
			.append("/")
			.append(ordinativoIncasso.getNumero());
		if(StringUtils.isNotBlank(ordinativoIncasso.getDescrizione())) {
			descrizione.append(" ")
				.append(ordinativoIncasso.getDescrizione());
		}
		movimento.append(ordinativoIncasso.getAnno())
			.append("/")
			.append(ordinativoIncasso.getNumero())
			.append(" (ord Pagamento)");

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
		return model.getOrdinativoPagamento() != null
			&& model.getOrdinativoPagamento().getCapitoloUscitaGestione() != null
				? model.getOrdinativoPagamento().getCapitoloUscitaGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		return model.getOrdinativoPagamento() != null ? model.getOrdinativoPagamento().getSoggetto() : null;
	}
}
