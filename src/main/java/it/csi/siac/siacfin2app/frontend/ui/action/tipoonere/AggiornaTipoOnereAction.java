/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.tipoonere;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di Action per l'aggiornamento del tipoOnere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GenericAggiornaTipoOnereAction.MODEL_SESSION_NAME)
public class AggiornaTipoOnereAction extends GenericAggiornaTipoOnereAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8596555825551812040L;
	/** Serviz&icirc; generici */
	@Autowired protected transient GenericService genericService;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile(model.getTitolo());
		
		try {
			caricaListe();
			// Caricamento del tipoOnere dal servizio
			caricaTipoOnere();
		} catch(WebServiceInvocationFailureException wsife) {
			log.error(methodName, wsife.getMessage());
			throw wsife;
		}
		
		return SUCCESS;
	}


	private void caricaListe() throws WebServiceInvocationFailureException {
		caricaNaturaOnere();
		caricaCausale770();
		caricaCodiceSommaNonSoggetta();
		caricaAttivitaOnere();
		caricaListaClasseSoggetto();
		caricaListaTipoIvaSplitReverse();
		// TODO: tipoFinanziamento?
		caricaDistintaEntrata() ;
	}
	
	/**
	 * Caricamento del tipo onere.
	 */
	private void caricaTipoOnere() {
		final String methodName = "caricaTipoOnere";
		RicercaDettaglioTipoOnere request = model.creaRequestRicercaDettaglioTipoOnere();
		logServiceRequest(request);
		RicercaDettaglioTipoOnereResponse response = tipoOnereService.ricercaDettaglioTipoOnere(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioTipoOnere.class, response));
			throwExceptionFromErrori(response.getErrori());
		}
		
		// Caricamento dei dati nel model
		TipoOnere to = response.getTipoOnere();
		model.setTipoOnere(to);
		model.setCausali770(to.getCausali770());
		model.setSommeNonSoggette(to.getCodiciSommaNonSoggetta());
		
		// Filtrare le causali di spesa e di entrata a seconda della presenza del movimento di gestione
		List<CausaleSpesa> listSpesa = filterSpesa(to.getCausaliSpesa());
		List<CausaleEntrata> listEntrata = filterEntrata(to.getCausaliEntrata());
		model.setListaCausaleSpesa(listSpesa);
		model.setListaCausaleEntrata(listEntrata);
		model.setAttivitaOnere(to.getAttivitaOnere());
		
		CapitoloUscitaGestione capitoloUscitaGestione = findCapitoloByCausaleSpesa(to.getCausaliSpesa());
		CapitoloEntrataGestione capitoloEntrataGestione = findCapitoloByCausaleEntrata(to.getCausaliEntrata());
		Soggetto soggetto = findSoggettoByCausaleSpesa(to.getCausaliSpesa());
		SedeSecondariaSoggetto sedeSecondariaSoggetto = findSedeSecondariaSoggettoByCausaleSpesa(to.getCausaliSpesa());
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = findModalitaPagamentoSoggettoByCausaleSpesa(to.getCausaliSpesa());
		model.setCapitoloUscitaGestione(capitoloUscitaGestione);
		model.setCapitoloEntrataGestione(capitoloEntrataGestione);
		model.setSoggetto(soggetto);
		model.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
		model.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
	}

	/**
	 * Filtra le causali di spesa per presenza dell'impegno.
	 * 
	 * @param causaliSpesa le causali da filtrare
	 * 
	 * @return le causali filtrate
	 */
	private List<CausaleSpesa> filterSpesa(List<CausaleSpesa> causaliSpesa) {
		List<CausaleSpesa> listSpesa = new ArrayList<CausaleSpesa>();
		model.setCapitoloUscitaNonEditabile(false);
		for(CausaleSpesa cs : causaliSpesa){
			if(cs.getImpegno() != null && cs.getImpegno().getUid() != 0){
				listSpesa.add(cs);
				model.setCapitoloUscitaNonEditabile(true);
				model.setSoggettoNonEditabile(cs.getImpegno().getSoggetto() != null && StringUtils.isNotBlank(cs.getImpegno().getSoggetto().getCodiceSoggetto()));
			}else if(cs.getCapitoloUscitaGestione() != null){
				model.setCausaleSpesaSoloCapitolo(cs);
			}else if(cs.getSoggetto() != null){
				model.setCausaleSpesaSoloSoggetto(cs);
			}
		}
		return listSpesa;
	}

	/**
	 * Filtra le causali di entrata per presenza dell'accertamento.
	 * 
	 * @param causaliEntrata le causali da filtrare
	 * 
	 * @return le causali filtrate
	 */
	private List<CausaleEntrata> filterEntrata(List<CausaleEntrata> causaliEntrata) {
		List<CausaleEntrata> listEntrata = new ArrayList<CausaleEntrata>();
		model.setCapitoloEntrataNonEditabile(false);
		for(CausaleEntrata ce : causaliEntrata){
			if(ce.getAccertamento() != null && ce.getAccertamento().getUid() != 0){
				listEntrata.add(ce);
				model.setCapitoloEntrataNonEditabile(true);
			}else if(ce.getCapitoloEntrataGestione() != null){
				model.setCausaleEntrataSoloCapitolo(ce);
			}
		}
		return listEntrata;
	}
	
	/**
	 * Ottiene il capitolo a partire dalla lista delle causali
	 * 
	 * @param list la lista delle causali
	 * 
	 * @return il capitolo associato alle causali, se presente
	 */
	private CapitoloUscitaGestione findCapitoloByCausaleSpesa(List<CausaleSpesa> list) {
		for(CausaleSpesa cs : list) {
			if(cs.getCapitoloUscitaGestione() != null && cs.getCapitoloUscitaGestione().getUid() != 0) {
				return cs.getCapitoloUscitaGestione();
			}
		}
		return null;
	}

	/**
	 * Ottiene il capitolo a partire dalla lista delle causali
	 * 
	 * @param list la lista delle causali
	 * 
	 * @return il capitolo associato alle causali, se presente
	 */
	private CapitoloEntrataGestione findCapitoloByCausaleEntrata(List<CausaleEntrata> list) {
		for(CausaleEntrata ce : list) {
			if(ce.getCapitoloEntrataGestione() != null && ce.getCapitoloEntrataGestione().getUid() != 0) {
				return ce.getCapitoloEntrataGestione();
			}
		}
		return null;
	}

	/**
	 * Ottiene il soggetto a partire dalla lista delle causali
	 * 
	 * @param list la lista delle causali
	 * 
	 * @return il soggetto associato alle causali, se presente
	 */
	private Soggetto findSoggettoByCausaleSpesa(List<CausaleSpesa> list) {
		for(CausaleSpesa cs : list) {
			if(cs.getSoggetto() != null && cs.getSoggetto().getUid() != 0) {
				return cs.getSoggetto();
			}
		}
		return null;
	}
	
	/**
	 * Ottiene la sede secondaria del soggetto a partire dalla lista delle causali
	 * 
	 * @param list la lista delle causali
	 * 
	 * @return il soggetto associato alle causali, se presente
	 */
	private SedeSecondariaSoggetto findSedeSecondariaSoggettoByCausaleSpesa(List<CausaleSpesa> list) {
		for(CausaleSpesa cs : list) {
			if(cs.getSedeSecondariaSoggetto() != null && cs.getSedeSecondariaSoggetto().getUid() != 0) {
				return cs.getSedeSecondariaSoggetto();
			}
		}
		return null;
	}

	/**
	 * Ottiene la modalita di pagamento del soggetto a partire dalla lista delle causali
	 * 
	 * @param list la lista delle causali
	 * 
	 * @return il soggetto associato alle causali, se presente
	 */
	private ModalitaPagamentoSoggetto findModalitaPagamentoSoggettoByCausaleSpesa(List<CausaleSpesa> list) {
		for(CausaleSpesa cs : list) {
			if(cs.getModalitaPagamentoSoggetto() != null && cs.getModalitaPagamentoSoggetto().getUid() != 0) {
				return cs.getModalitaPagamentoSoggetto();
			}
		}
		return null;
	}
	
	/**
	 * Carica la lista di distinta entrata.
	 *
	 * @throws WebServiceInvocationFailureException the web service invocation failure exception
	 */
	//SIAC-5060
	private void caricaDistintaEntrata() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaDistinta = sessionHandler.getParametro(BilSessionParameter.LISTA_DISTINTA);
		if(listaDistinta == null) {
			Liste request = model.creaRequestListe(TipiLista.DISTINTA_ENTRATA);
			logServiceRequest(request);
			ListeResponse response = genericService.liste(request);
			logServiceResponse(response);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(Liste.class, response));
			}
			listaDistinta = new ArrayList<CodificaFin>(response.getDistintaEntrata());
			ComparatorUtils.sortByCodiceFin(listaDistinta);
			sessionHandler.setParametro(BilSessionParameter.LISTA_DISTINTA, listaDistinta);
		}
		model.setListaDistinta(listaDistinta);
	}

	/**
	 * Preparazione per il metodo {@link #aggiornamento()}.
	 */
	public void prepareAggiornamento() {
		model.getTipoOnere().setAliquotaCaricoSoggetto(null);
		model.getTipoOnere().setAliquotaCaricoEnte(null);
		model.getTipoOnere().setDataFineValidita(null);
		
		model.setCausali770(new ArrayList<Causale770>());
		model.setAttivitaOnere(new ArrayList<AttivitaOnere>());
		model.setSommeNonSoggette(new ArrayList<CodiceSommaNonSoggetta>());
		
		model.setCapitoloEntrataGestione(null);
		model.setCapitoloUscitaGestione(null);
		// Pulisco il soggetto
		model.setSoggetto(null);
		// TODO: Le ripopolo?
		model.setSedeSecondariaSoggetto(null);
		model.setModalitaPagamentoSoggetto(null);
		model.setModalitaPagamentoSoggettoCessione(null);
		
		// Lotto L
		model.getTipoOnere().setTipoIvaSplitReverse(null);
	}
	
	/**
	 * Aggiornamento del tipoOnere.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		AggiornaTipoOnere request = model.creaRequestAggiornaTipoOnere();
		logServiceRequest(request);
		AggiornaTipoOnereResponse response = tipoOnereService.aggiornaTipoOnere(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaTipoOnere.class, response));
			addErrori(response);
			return INPUT;
		}
		model.setTipoOnere(response.getTipoOnere());
		// Imposto l'informazione di rientro in sessione
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		ripopolaSoggettoSediModalitaPagamentoSeDisponibile();
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Se nel model ho il soggetto correttamente valorizzato, scorro le liste delle sedi secondarie e delle modalit&agrave; di pagamento
	 * per popolare i dati del model, che saranno utilizzati successivamente nelle tabelle di riepilogo.
	 */
	private void ripopolaSoggettoSediModalitaPagamentoSeDisponibile() {
		
		if (model.getSoggetto() == null || model.getSoggetto().getUid() == 0) {
			return;
		}
			
		SedeSecondariaSoggetto sedeSecondariaSoggetto = ComparatorUtils.searchByUidEventuallyNull(model.getSoggetto().getSediSecondarie(), model.getSedeSecondariaSoggetto());
		if(sedeSecondariaSoggetto != null && sedeSecondariaSoggetto.getUid() != 0) {
			model.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
		}
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = ComparatorUtils.searchByUidEventuallyNull(model.getSoggetto().getModalitaPagamentoList(), model.getModalitaPagamentoSoggetto());
		if(modalitaPagamentoSoggetto != null && modalitaPagamentoSoggetto.getUid() != 0) {
			model.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		}
	}

	/**
	 * Validazione per il metodo {@link #aggiornamento()}.
	 */
	public void validateAggiornamento() {
		final String methodName = "validateAggiornamento";
		TipoOnere to = model.getTipoOnere();
		checkCondition(to.getAliquotaCaricoSoggetto() == null || to.getAliquotaCaricoSoggetto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota a carico soggetto", ": deve essere positiva"));
		checkCondition(to.getAliquotaCaricoEnte() == null || to.getAliquotaCaricoEnte().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota a carico ente", ": deve essere positiva"));
		
		// La somma delle aliquote NON deve superare il 100
		checkCondition(to.getAliquotaCaricoSoggettoNotNull().add(to.getAliquotaCaricoEnteNotNull()).compareTo(BilUtilities.BIG_DECIMAL_ONE_HUNDRED) <= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota", ":la somma delle aliquote non deve superare 100"));
		
		// La data di annullamento deve essere > = alla data corrente
		Date now = new Date();
		checkCondition(to.getDataFineValidita() == null || DateUtils.isSameDay(now, to.getDataFineValidita()) || !to.getDataFineValidita().before(now),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("data annullamento", "non deve essere precedente alla data odierna"));
		
		try {
			controlloCoerenzaSoggetto();
			controlloCoerenzaCapitolo();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione dei servizi: " + wsife.getMessage());
			throw new ParamValidationException(wsife);
		}
		
		// Lotto L
		controlloSplitReverse();
	}
	
	/**
	 * Controlla la coerenza del soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nella validazione del servizio
	 */
	private void controlloCoerenzaSoggetto() throws WebServiceInvocationFailureException {
		final String methodName = "controlloCoerenzaSoggetto";
		Soggetto soggetto = null;
		
		if(isValorizzatoSoggetto(model.getSoggetto())) {
			// Controllo se in pagina sia stato impostato il soggetto. In caso contrario, esco
			soggetto = ricercaSoggettoByCodice();
		}else{
			model.setSoggetto(null);
			return;
		}
		
		log.debug(methodName, "Soggetto " + (soggetto == null ? "non presente" : "trovato con uid " + soggetto.getUid()));
		// Imposto tale soggetto nel model
		model.setSoggetto(soggetto);
		
		// Controllo per le spese
		for(CausaleSpesa cs : model.getListaCausaleSpesa()) {
			controlloSoggettoCausaleSpesa(soggetto, cs);
		}
		
		for(CausaleEntrata ce : model.getListaCausaleEntrata()) {
			controlloSoggettoCausaleEntrata(soggetto, ce);
		}
		
		controlloValiditaModalitaPagamentoSoggetto(soggetto);
	}

	/**
	 * Effettua il controllo del soggetto per la causale di spesa.
	 * 
	 * @param soggetto     il soggetto da controllare
	 * @param causaleSpesa la causale di spesa da controllare
	 */
	private void controlloSoggettoCausaleSpesa(Soggetto soggetto, CausaleSpesa causaleSpesa) {
		if(soggetto == null) {
			// Il soggetto e' stato cancellato: lo elimino anche dalle causali
			causaleSpesa.setSoggetto(null);
			return;
		}
		
		// Il soggetto e' presente: effettuo il controllo di coerenza
		Soggetto scs = causaleSpesa.getSoggetto();
		if(scs == null) {
			causaleSpesa.setSoggetto(soggetto);
			scs = soggetto;
		}
		// Se non ho l'impegno esco
		if (causaleSpesa.getImpegno() == null) {
			return;
		}
		checkCondition(scs.getUid() == soggetto.getUid(),
			ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'impegno "
				+ causaleSpesa.getImpegno().getAnnoMovimento() + "/" + causaleSpesa.getImpegno().getNumeroBigDecimal() + (causaleSpesa.getSubImpegno() != null ? "-" + causaleSpesa.getSubImpegno().getNumeroBigDecimal() : "")
				+ " non appartiene alla classificazione del soggetto del tipo onere"));
	}
	
	/**
	 * Effettua il controllo del soggetto per la causale di entrata.
	 * 
	 * @param soggetto       il soggetto da controllare
	 * @param causaleEntrata la causale di entrata da controllare
	 */
	private void controlloSoggettoCausaleEntrata(Soggetto soggetto, CausaleEntrata causaleEntrata) {
		if(soggetto == null) {
			// Il soggetto e' stato cancellato: lo elimino anche dalle causali
			causaleEntrata.setSoggetto(null);
			return;
		}
		
		// Il soggetto e' presente: effettuo il controllo di coerenza
		Soggetto sce = causaleEntrata.getSoggetto();
		if(sce == null) {
			causaleEntrata.setSoggetto(soggetto);
			sce = soggetto;
		}
		// Se non ho l'accertamento esco
		if (causaleEntrata.getAccertamento() == null) {
			return;
		}
		checkCondition(sce.getUid() == soggetto.getUid(),
			ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'accertamento "
				+ causaleEntrata.getAccertamento().getAnnoMovimento() + "/" + causaleEntrata.getAccertamento().getNumeroBigDecimal() + (causaleEntrata.getSubAccertamento() != null ? "-" + causaleEntrata.getSubAccertamento().getNumeroBigDecimal() : "")
				+ " ha un soggetto distinto da quello del tipo onere"));
	}

	/**
	 * Controlla che la modalit&agrave; di pagamento soggetto sia valorizzata correttamente per il soggetto.
	 * 
	 * @param soggetto il soggetto da controllare
	 */
	private void controlloValiditaModalitaPagamentoSoggetto(Soggetto soggetto) {
		final String methodName = "controlloValiditaModalitaPagamentoSoggetto";
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = model.getModalitaPagamentoSoggetto();
		ModalitaPagamentoSoggetto modalitaPagamentoSoggettoCessione = null;
		// Non ho impostato la modalita di pagamento
		if(modalitaPagamentoSoggetto == null || modalitaPagamentoSoggetto.getUid() == 0 || soggetto == null) {
			return;
		}
		
		int uid = modalitaPagamentoSoggetto.getUid();
		for(ModalitaPagamentoSoggetto mps : soggetto.getModalitaPagamentoList()) {
			//modifica sonar
			boolean isModalitaPagamentoCessione = mps.getModalitaPagamentoSoggettoCessione2() != null && mps.getModalitaPagamentoSoggettoCessione2().getUid() == uid;
			boolean isModalitaPagamento = mps.getUid() == uid;
			if(isModalitaPagamentoCessione || isModalitaPagamento) {
				StringBuilder sb = new StringBuilder()
					.append("Trovata modalita di pagamento ")
					.append(isModalitaPagamentoCessione? "cessione " : "")
					.append("con uid ")
					.append(uid);
				log.debug(methodName, sb.toString());
				modalitaPagamentoSoggetto = mps;
				modalitaPagamentoSoggettoCessione = isModalitaPagamentoCessione? mps.getModalitaPagamentoSoggettoCessione2() : null;
				break;
			}
	//lascio commentato quanto presente prima per un eventuale revert (data: 18 aprile 2018)		
//			if(mps.getUid() == uid) {
//				log.debug(methodName, "Trovata modalita di pagamento con uid " + uid);
//				modalitaPagamentoSoggetto = mps;
//				break;
//			}
//			if(mps.getModalitaPagamentoSoggettoCessione2() != null && mps.getModalitaPagamentoSoggettoCessione2().getUid() == uid) {
//				log.debug(methodName, "Trovata modalita di pagamento cessione con uid " + uid);
//				modalitaPagamentoSoggetto = mps;
//				modalitaPagamentoSoggettoCessione = mps.getModalitaPagamentoSoggettoCessione2();
//				break;
//			}
		}
		
		model.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		model.setModalitaPagamentoSoggettoCessione(modalitaPagamentoSoggettoCessione);
	}
	
	/**
	 * Controlla se il soggetto del model sia valorizzato.
	 * 
	 * @param soggetto il soggetto da controllare 
	 * 
	 * @return <code>true</code> se il soggetto del model &eacute; valorizzato; <code>false</code> in caso contrario
	 */
	private boolean isValorizzatoSoggetto(Soggetto soggetto) {
		return soggetto != null && StringUtils.isNotBlank(soggetto.getCodiceSoggetto());
	}

	/**
	 * Effettua una ricerca del soggetto a partire dal codice fornito dall'utente.
	 * 
	 * @return il soggetto trovato
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private Soggetto ricercaSoggettoByCodice() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaSoggettoByCodice";
		final String codiceSoggetto = model.getSoggetto().getCodiceSoggetto();
		log.debug(methodName, "Ricerca soggetto per codice " + codiceSoggetto);
		
		Soggetto s = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		if(s == null || !model.getSoggetto().getCodiceSoggetto().equals(s.getCodiceSoggetto())) {
			// Il soggetto in sessione non va bene: lo ricerca
			RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
			logServiceRequest(request);
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response));
			}
			if(response.getSoggetto() == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", codiceSoggetto));
				return null;
			}
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, s);
			s = response.getSoggetto();
		}
		
		log.debug(methodName, "Soggetto trovato per codice " + codiceSoggetto + " con uid " + s.getUid());
		return s;
	}

	/**
	 * Controlla la coerenza del capitolo.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nella validazione del servizio
	 */
	private void controlloCoerenzaCapitolo() throws WebServiceInvocationFailureException {
		controlloCoerenzaCapitoloEntrata();
		controlloCoerenzaCapitoloSpesa();
	}

	/**
	 * Controlla la coerenza del capitolo di entrata.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nella validazione del servizio
	 */
	private void controlloCoerenzaCapitoloEntrata() throws WebServiceInvocationFailureException {
		final String methodName = "controlloCoerenzaCapitoloEntrata";
		CapitoloEntrataGestione ceg = null;
		for(CausaleEntrata ce : model.getListaCausaleEntrata()) {
			if(ce.getCapitoloEntrataGestione() != null) {
				ceg = ce.getCapitoloEntrataGestione();
				log.debug(methodName, "ho trovato almeno una causale con il capitolo popolato");
				break;
			}
		}
		
		if(ceg == null) {
			log.debug(methodName, "non ho trovato un capitolo dalle causali, prendo quello del model");
			// Controllo se in pagina sia stato impostato il capitolo. In caso contrario, esco
			ceg = checkCapitoloEntrataGestione(model.getCapitoloEntrataGestione());
		}
		if(ceg == null) {
			log.debug(methodName, "Nessun capitolo trovato");
			model.setCapitoloEntrataGestione(null);
			return;
		}
		
		log.debug(methodName, "Capitolo trovato con uid " + ceg.getUid());
		// Imposto tale capitolo nel model
		model.setCapitoloEntrataGestione(ceg);
		
		for(CausaleEntrata ce : model.getListaCausaleEntrata()) {
			CapitoloEntrataGestione cegce = ce.getCapitoloEntrataGestione();
			if(cegce == null) {
				ce.setCapitoloEntrataGestione(ceg);
				cegce = ceg;
			}
			checkCondition(cegce.getUid() == ceg.getUid(),
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'accertamento "
					+ ce.getAccertamento().getAnnoMovimento() + "/" + ce.getAccertamento().getNumeroBigDecimal()
						+ (ce.getSubAccertamento() != null ? "-" + ce.getSubAccertamento().getNumeroBigDecimal() : "")
					+ " ha un capitolo differente da quello del tipo onere"));
		}
	}

	/**
	 * Controlla la coerenza del capitolo di spesa.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nella validazione del servizio
	 */
	private void controlloCoerenzaCapitoloSpesa() throws WebServiceInvocationFailureException {
		final String methodName = "controlloCoerenzaCapitoloSpesa";
		CapitoloUscitaGestione cug = null;
		for(CausaleSpesa ce : model.getListaCausaleSpesa()) {
			if(ce.getCapitoloUscitaGestione() != null) {
				cug = ce.getCapitoloUscitaGestione();
				break;
			}
		}
		
		if(cug == null) {
			// Controllo se in pagina sia stato impostato il capitolo di spesa. In caso contrario, esco
			cug = checkCapitoloUscitaGestione(model.getCapitoloUscitaGestione());
		}
		if(cug == null) {
			log.debug(methodName, "Nessun capitolo trovato");
			model.setCapitoloUscitaGestione(null);
			return;
		}
		
		log.debug(methodName, "Capitolo trovato con uid " + cug.getUid());
		// Imposto tale soggetto nel model
		model.setCapitoloUscitaGestione(cug);
		
		for(CausaleSpesa cs : model.getListaCausaleSpesa()) {
			CapitoloUscitaGestione cugcs = cs.getCapitoloUscitaGestione();
			if(cugcs == null) {
				cs.setCapitoloUscitaGestione(cug);
				cugcs = cug;
			}
			checkCondition(cugcs.getUid() == cug.getUid(),
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'impegno "
						+ cs.getImpegno().getAnnoMovimento() + "/" + cs.getImpegno().getNumeroBigDecimal()
						+ (cs.getSubImpegno() != null ? "-" + cs.getSubImpegno().getNumeroBigDecimal() : "")
					+ " ha un capitolo differente da quello del tipo onere"));
		}
	}

	/**
	 * Redirezione all'aggiornamento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String redirectAggiornamento() {
		final String methodName = "redirectAggiornamento";
		log.debug(methodName, "Redirezione all'aggiornamento per il tipoOnere con uid " + model.getTipoOnere().getUid());
		return SUCCESS;
	}
	
}
