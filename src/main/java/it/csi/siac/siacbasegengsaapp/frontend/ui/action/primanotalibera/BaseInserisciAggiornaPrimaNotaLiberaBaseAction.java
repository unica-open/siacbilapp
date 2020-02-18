/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * 
 */
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLibera;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioModulareCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNoteResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Classe base di action per l'inserimento e l'aggiornamento della prima nota libera
 *  
 * @author Paggio Simona
 * @version 1.0.0 - 14/04/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 09/10/2015
 *
 * @param <M> la tipizzazione del model
 */

public abstract class BaseInserisciAggiornaPrimaNotaLiberaBaseAction <M extends BaseInserisciAggiornaPrimaNotaLiberaBaseModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 431625867487855560L;
	
	/** Nome del model dell'inserimento per la sessione, modulo FIN */
	public static final String MODEL_SESSION_NAME_INSERIMENTO_FIN = "InserisciPrimaNotaLiberaFINModel";
	/** Nome del model dell'aggiornamento per la sessione, modulo FIN */
	public static final String MODEL_SESSION_NAME_AGGIORNAMENTO_FIN = "AggiornaPrimaNotaLiberaFINModel";
	
	/** Nome del model dell'inserimento per la sessione, modulo GSA */
	public static final String MODEL_SESSION_NAME_INSERIMENTO_GSA = "InserisciPrimaNotaLiberaGSAModel";
	/** Nome del model dell'aggiornamento per la sessione, modulo GSA */
	public static final String MODEL_SESSION_NAME_AGGIORNAMENTO_GSA = "AggiornaPrimaNotaLiberaGSAModel";
	
	/** Nome del model dell'inserimento per l'integrata manuale per la sessione, modulo GSA */
	public static final String MODEL_SESSION_NAME_INTEGRATA_MANUALE_INSERIMENTO_GSA = "InserisciPrimaNotaIntegrataManualeGSAModel";
	/** Nome del model dell'aggiornamento per l'integrata manuale per la sessione, modulo GSA */
	public static final String MODEL_SESSION_NAME_INTEGRATA_MANUALE_AGGIORNAMENTO_GSA = "AggiornaPrimaNotaIntegrataManualeGSAModel";
	
	/** Serviz&icirc; della causale */
	@Autowired protected transient CausaleService causaleService;
	/** Serviz&icirc; della prima nota */
	@Autowired protected transient PrimaNotaService primaNotaService;
	/** Serviz&icirc; delle codifiche */
	@Autowired private transient CodificheService codificheService;
	/** Serviz&icirc; dei classificatori bil */
	@Autowired protected transient ClassificatoreBilService classificatoreBilService;
	
	/** 
	 *@return il parametro di sessione per la lista causali corrispondente ad ambito FIN o GSA
	 * */
	protected abstract BilSessionParameter getBilSessionParameterListeCausali();
	
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	/**
	 * Ottiene la lista delle prime note collegate.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaPrimeNoteCollegate(){
		return SUCCESS;
	}
	
	/**
	 * Collega la prima nota.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String collegaPrimaNota(){
		List<PrimaNota> primaNotaDaCollegare = ricercaPrimaNota();
		if(hasErrori()){
			return SUCCESS;
		}
		if(primaNotaDaCollegare == null || primaNotaDaCollegare.isEmpty()){
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Prima nota", "anno numero e tipo "));
			return SUCCESS;
		}
		if(primaNotaDaCollegare.size() > 1){
			addErrore(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore(": non e' possibile determinare la prima nota in modo univoco per i valori inseriti."));
			return SUCCESS;
		}
		PrimaNota pNotaDaCollegare = primaNotaDaCollegare.get(0);
		for(PrimaNota pn : model.getListaPrimeNoteDaCollegare()){
			if(pn.getUid() == pNotaDaCollegare.getUid()){
				addErrore(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore("la prima nota selezionata e' gia' stata collegata. "));
				return SUCCESS;
			}
		}
		TipoRelazionePrimaNota motivazione = ComparatorUtils.searchByUid(model.getListaMotivazioni(), model.getMotivazione());
		pNotaDaCollegare.setTipoRelazionePrimaNota(motivazione);
		pNotaDaCollegare.setNoteCollegamento(model.getNoteCollegamento());
		model.getListaPrimeNoteDaCollegare().add(pNotaDaCollegare);
		model.setMotivazione(null);
		model.setNoteCollegamento(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	/**
	 * Elimina il collegamento tra le prime note
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaCollegamento(){
		int idx = model.getIndicePrimaNota().intValue();
		model.getListaPrimeNoteDaCollegare().remove(idx);
		model.setIndicePrimaNota(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Ricerca della prima nota
	 * @return la lista delle prime note trovate
	 */
	private List<PrimaNota> ricercaPrimaNota() {
		RicercaPrimeNote req = model.creaRequestRicercaPrimeNote();
		RicercaPrimeNoteResponse res = primaNotaService.ricercaPrimeNote(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return new ArrayList<PrimaNota>();
		}
		return res.getPrimeNote();
	}

	/**
	 * Validazione per il metodo {@link #collegaPrimaNota()}.
	 */
	public void validateCollegaPrimaNota(){
		checkNotNull(model.getAnnoPrimaNota(), "anno prima nota");
		checkNotNull(model.getPrimaNotaDaCollegare().getTipoCausale(), "tipo prima nota");
		checkNotNull(model.getPrimaNotaDaCollegare().getNumeroRegistrazioneLibroGiornale(), "numero definitivo prima nota");
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio req = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse res = bilancioService.ricercaDettaglioBilancio(req);
		
		FaseBilancio faseBilancio = res.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		
		if (getFaseDiBilancioNonCompatibile(faseBilancio)) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	/**
	 * Controlla se la base di bilancio non sia compatibile
	 * @param faseBilancio la fase di bilancio
	 * @return se la fase non &eacute; compatibile
	 */
	protected boolean getFaseDiBilancioNonCompatibile(FaseBilancio faseBilancio) {
		return
		FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
		FaseBilancio.PREVISIONE.equals(faseBilancio) ||
		FaseBilancio.CHIUSO.equals(faseBilancio);
	}
	
	/**
	 * Caricamento delle liste per l'interazione utente.
	 * 
	 * @throws GenericFrontEndMessagesException in caso di eccezione nel caricamento delle liste
	 */
	protected void caricaListe() {
		try {
			caricaListaTipiEvento();
			filtraTipiEventoExtr();
			caricaListaEvento();
			filtraEventoExtr();
			caricaListaCausale();
			filtraCausale();
			caricaListaMotivazioni();
		} catch(WebServiceInvocationFailureException wsife) {
			throw new GenericFrontEndMessagesException(wsife);
		}
	}
	
	/**
	 * Filtra la causale EP ottenuta da servizio
	 * @throws WebServiceInvocationFailureException in caso di fallimento del filtro
	 */
	protected void filtraCausale() throws WebServiceInvocationFailureException {
		// Eventuale implementazione nelle sotto-classi
	}


	/**
	 * Caricamento delle liste del tipo relazione.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void caricaListaMotivazioni() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaMotivazioni";
		
		List<TipoRelazionePrimaNota> listaTipoRelazione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_RELAZIONE);
		if(listaTipoRelazione == null || listaTipoRelazione.isEmpty()) {
			log.debug(methodName, "Lista di Tipi relazione non presente in sessione. Caricamento da servizio");
			RicercaCodifiche req = model.creaRequestRicercaCodifiche(TipoRelazionePrimaNota.class);
			logServiceRequest(req);
			RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
			logServiceResponse(res);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				String msgErrore = createErrorInServiceInvocationString(req, res);
				throw new WebServiceInvocationFailureException(msgErrore);
			}
			listaTipoRelazione = new ArrayList<TipoRelazionePrimaNota>();
			for(TipoRelazionePrimaNota tr : res.getCodifiche(TipoRelazionePrimaNota.class)){
				if(Boolean.TRUE.equals(tr.getRelazioneUtilizzabile())){
					listaTipoRelazione.add(tr);
				}
			}
		}
		model.setListaMotivazioni(listaTipoRelazione);
		sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_RELAZIONE, listaTipoRelazione);
	}


	/**
	 * Caricamento delle liste del tipo evento.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void caricaListaTipiEvento() throws WebServiceInvocationFailureException{
		final String methodName = "caricaListaTipiEvento";
		
		List<TipoEvento> listaTipiEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		if(listaTipiEvento == null || listaTipiEvento.isEmpty()) {
			log.debug(methodName, "Lista di Tipi Evento non presente in sessione. Caricamento da servizio");
			RicercaCodifiche req = model.creaRequestRicercaCodifiche(TipoEvento.class);
			logServiceRequest(req);
			RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
			logServiceResponse(res);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				String msgErrore = createErrorInServiceInvocationString(req, res);
				throw new WebServiceInvocationFailureException(msgErrore);
			}
			listaTipiEvento = res.getCodifiche(TipoEvento.class);
		}
		model.setListaTipiEvento(listaTipiEvento);
		sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_EVENTO, listaTipiEvento);
	}


	/**
	 * Caricamento delle liste del tipo evento.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void caricaListaEvento() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaEvento";
		
		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
		if(listaEvento == null || listaEvento.isEmpty()) {
			log.debug(methodName, "Lista di Evento non presente in sessione. Caricamento da servizio");
			listaEvento = new ArrayList<Evento> ();
			RicercaCodifiche req = model.creaRequestRicercaCodifiche(Evento.class);
			logServiceRequest(req);
			RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
			logServiceResponse(res);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				String msgErrore = createErrorInServiceInvocationString(req, res);
				throw new WebServiceInvocationFailureException(msgErrore);
			}
			
			List<Evento> listaEventoTotale = res.getCodifiche(Evento.class);
			if (listaEventoTotale!=null) {
				//verifica tipo causale  tipo evento libera
				for (Evento e : listaEventoTotale) {
					if (e != null && e.getCodice() != null && (isEventoPrimaNotaLibera(e) || isEventoExtr(e))) {
						listaEvento.add(e);
					}
				}
			}
			
		}
		sessionHandler.setParametro(BilSessionParameter.LISTA_EVENTO, listaEvento);
		model.setListaEvento(listaEvento);
	}
	
	/**
	 * Controlla se l'evento sia di tipo causale LIBERA
	 * @param e l'evento
	 * @return se l'evento sia per una causale LIBERA
	 */
	private boolean isEventoPrimaNotaLibera(Evento e) {
		if(e.getTipoEvento() == null || e.getTipoEvento().getListaTipoCausaleEP().isEmpty()) {
			return false;
		}
		for (TipoCausale tc : e.getTipoEvento().getListaTipoCausaleEP()) {
			if (TipoCausale.Libera.equals(tc)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se l'evento sia EXTR
	 * @param e l'evento
	 * @return se l'evento sia EXTR
	 */
	private boolean isEventoExtr(Evento e) {
		return e.getCodice().startsWith(BilConstants.CODICE_EVENTO_EXTR.getConstant());
	}
	
	/**
	 * Filtro per i tipi evento EXTR
	 * @throws WebServiceInvocationFailureException nel caso in cui il filtro non vada a buon fine
	 */
	protected void filtraTipiEventoExtr() throws WebServiceInvocationFailureException {
		List<TipoEvento> listaTipiEvento = model.getListaTipiEvento();
		for(Iterator<TipoEvento> it = listaTipiEvento.iterator(); it.hasNext();) {
			TipoEvento te = it.next();
			if(te == null || BilConstants.TIPO_EVENTO_EXTR.getConstant().equals(te.getCodice())) {
				// Rimuovo l'EXTR
				it.remove();
			}
		}
	}
	
	/**
	 * Filtro per l'evento EXTR
	 * @throws WebServiceInvocationFailureException nel caso in cui il filtro non vada a buon fine
	 */
	protected void filtraEventoExtr() throws WebServiceInvocationFailureException {
		List<Evento> listaEvento = model.getListaEvento();
		for(Iterator<Evento> it = listaEvento.iterator(); it.hasNext();) {
			Evento ev = it.next();
			if(ev == null || (StringUtils.startsWith(ev.getCodice(), BilConstants.TIPO_EVENTO_EXTR.getConstant()))) {
				it.remove();
			}
		}
	}
	
	/**
	 * Caricamento delle liste del tipo evento.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void caricaListaCausale() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaCausale";
		
		List<CausaleEP> listaCausaliEp = sessionHandler.getParametro(getBilSessionParameterListeCausali());
		// Se non ho i dati, effettuo la ricerca
	
		if(listaCausaliEp == null) {
			//recuperare da servizio la lista causali
			RicercaSinteticaModulareCausale req = model.creaRequestRicercaSinteticaModulareCausale();
			logServiceRequest(req);
			RicercaSinteticaModulareCausaleResponse res = causaleService.ricercaSinteticaModulareCausale(req);
			logServiceResponse(res);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(req, res);
				log.warn(methodName, errorMsg);
				addErrori(res);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			listaCausaliEp = res.getCausali();
			// Aggiungo il risultato in sessione
			sessionHandler.setParametro(getBilSessionParameterListeCausali(), listaCausaliEp);
		}
		
		model.setListaCausaleEP(listaCausaliEp);
	}
	/**
	 * Caricamento della lista delle classi.
	 */
	protected void caricaListaClassi() {
		RicercaCodifiche reqRC = model.creaRequestRicercaClassi();
		logServiceRequest(reqRC);
		RicercaCodificheResponse resRC = codificheService.ricercaCodifiche(reqRC);
		logServiceResponse(resRC);
		
		if(!resRC.hasErrori()){
			model.setListaClassi(resRC.getCodifiche(ClassePiano.class));
		}else{
			addErrori(resRC);
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) {
		LeggiClassificatoriByTipoElementoBil req = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(req);
		LeggiClassificatoriByTipoElementoBilResponse res = classificatoreBilService.leggiClassificatoriByTipoElementoBil(req);
		logServiceResponse(res);
		return res;
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 */
	protected void caricaListaTitoli() {
		LeggiClassificatoriByTipoElementoBilResponse responseEntrata = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
		List<TitoloEntrata> listaTE = responseEntrata.getClassificatoriTitoloEntrata();
		
		model.setListaTitoloEntrata(listaTE);
		
		LeggiClassificatoriByTipoElementoBilResponse responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
		List<TitoloSpesa> listaTS = responseSpesa.getClassificatoriTitoloSpesa();
		List<Missione> missione = responseSpesa.getClassificatoriMissione();
		
		model.setListaTitoloSpesa(listaTS);
		model.setListaMissione(missione);
	}
	/**
	 * Preparazione per il metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		// Pulisco la causale EP
		model.setPrimaNotaLibera(null);

		// Pulisco i dati esterni
		model.setCausaleEP(null);
		model.setEvento(null);
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		// Se non ho la prima nota, qualcosa e' andato storto: esco subito
		checkNotNull(model.getPrimaNotaLibera(), "Prima Nota Libera ", true);
		
		if(model.isAggiornamento()) {
			CausaleEP causaleEP = ottieniCausaleEPDaPrimaNotaOriginale();
			model.setCausaleEP(causaleEP);
		}
		checkNotNullNorInvalidUid(model.getCausaleEP(), "CausaleEP");
		checkNotNull(model.getPrimaNotaLibera().getDataRegistrazione(), "Data registrazione ");
		checkNotNullNorEmpty(model.getPrimaNotaLibera().getDescrizione(), "Descrizione ");

	}
	
	/**
	 * Ottiene la causale EP dalla prima nota originale.
	 * 
	 * @return la causale della prima nota originale
	 */
	protected CausaleEP ottieniCausaleEPDaPrimaNotaOriginale() {
		// To override
		return null;
	}

	/**
	 * Preparazione per il metodo {@link #completeStep2()}.
	 */
	public void prepareCompleteStep2() {
		model.getPrimaNotaLibera().setListaMovimentiEP(model.getListaMovimentoEP());
		model.setListaMovimentoEP(new ArrayList<MovimentoEP>());
	}
	
	/**
	 * Completamento per lo step 2
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep2() {
		return SUCCESS;
	}
	
	/**
	 * Completamento per lo step 3.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	
	public String completeStep3() {
		final String methodName = "completeStep3";
		// Inserimento della causale
		ValidaPrimaNota req = model.creaRequestValidaPrimaNota();
		logServiceRequest(req);
		ValidaPrimaNotaResponse res = primaNotaService.validaPrimaNota(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nella validazione della Prima Nota Libera");
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Validata correttamente Prima Nota Libera con uid " + res.getPrimaNota().getUid());
		// Imposto i dati della causale restituitimi dal servizio
		model.setPrimaNotaLibera(res.getPrimaNota());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	/**
	 * Completamento per lo step 1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep1() {
		final String methodName = "completeStep1";
		CausaleEP causaleEP;
		try {
			causaleEP = ottieniCausaleEP();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		model.setCausaleEP(causaleEP);
		model.setContiCausale(causaleEP.getContiTipoOperazione() != null && !causaleEP.getContiTipoOperazione().isEmpty());
		model.setSingoloContoCausale(checkSingoloContoCausale(causaleEP));
		return SUCCESS;
	}
	
	/**
	 * Ottiene la causale EP dal servizio
	 * @return la causale EP
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private CausaleEP ottieniCausaleEP() throws WebServiceInvocationFailureException {
		RicercaDettaglioModulareCausale req = model.creaRequestRicercaDettaglioModulareCausale();
		RicercaDettaglioModulareCausaleResponse res = causaleService.ricercaDettaglioModulareCausale(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		return res.getCausaleEP();
	}
	
	/**
	 * Controlla se la causale EP abbia un unico conto
	 * @param causaleEP la causale
	 * @return <code>true</code> se la causale ha un unico conto 
	 */
	private boolean checkSingoloContoCausale(CausaleEP causaleEP) {
		if(causaleEP == null) {
			return false;
		}
		return causaleEP.getContiTipoOperazione() != null && causaleEP.getContiTipoOperazione().size() <= 1;
		// Per la SIAC-3776?
//		boolean unUnicoConto = causaleEP.getContiTipoOperazione() != null && causaleEP.getContiTipoOperazione().size() <= 1;
//		boolean almenoUnContoDiTipoEP = false;
//		for(Evento e : cEP.getEventi()) {
//			// C'e' almeno un conto di tipo epilogativo
//			almenoUnContoDiTipoEP = almenoUnContoDiTipoEP || BilConstants.TIPO_EVENTO_SCRITTURA_EPILOGATIVA.equals(e.getTipoEvento());
//		}
//		
//		return unUnicoConto && almenoUnContoDiTipoEP;
	}
	
	/**
	 * Ritorno allo step 1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String backToStep1() {
		return SUCCESS;
	}
	
	/**
	 * Ingresso nello step 1.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String step1() {
		return SUCCESS;
	}
	
	/**
	 * Annullamento dei dati relativi allo step 1.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep1() {
		return SUCCESS;
	}
	
	
	/**
	 * Ingresso nello step 2.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String step2() {
		return SUCCESS;
	}
	
	/**
	 * Annullamento dei dati relativi allo step 2.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep2() {
		return SUCCESS;
	}
	
	/**
	 * Ingresso nello step 3.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String step3() {
		leggiEventualiInformazioniAzionePrecedente();
		// SIAC-5853
		if(model.isValidazione() && model.getPrimaNotaLibera() != null && model.getPrimaNotaLibera().getDataRegistrazioneLibroGiornale() == null) {
			model.getPrimaNotaLibera().setDataRegistrazioneLibroGiornale(new Date());
		}
		return SUCCESS;
	}
	
	/**
	 * Prima Nota Libera ha solo un movimentoEP
	 * 
	 * @param primaNotaLibera la prima nota libera
	 * 
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEPDaPrimaNota(PrimaNota primaNotaLibera) {
		return primaNotaLibera != null
			&& primaNotaLibera.getListaMovimentiEP() != null
			&& !primaNotaLibera.getListaMovimentiEP().isEmpty()
			&& primaNotaLibera.getListaMovimentiEP().get(0) != null
				? primaNotaLibera.getListaMovimentiEP().get(0).getCausaleEP()
				: null;
		
	}
	
	/**
	 * Controlla che le scritture siano corrette. Devono essere:
	 * <ul>
	 *     <li>
	 *         presenti almeno 2 scritture su conti con segni differenti (pu&ograve; essere anche lo stesso conto devono essere diversi i segni),
	 *         altrimenti viene visualizzato il messaggio
	 *         <code>&lt;COR_ERR_0044 - Operazione non consentita ('Devono essere presenti almeno due conti con segni differenti.')&gt;</code>
	 *     </li>
	 *     <li>
	 *         il totale dare deve essere uguale al totale avere: altrimenti viene visualizzato il messaggio
	 *         <code>&lt;COR_ERR_0044 - Operazione non consentita ('Il totale DARE deve essere UGUALE al totale AVERE.')&gt;</code>
	 *     </li>
	 * </ul>
	 * 
	 * @return la lista dei movimenti di dettaglio finali
	 */
	protected List<MovimentoDettaglio> checkScrittureCorrette() {
		int numeroScrittureDare = 0;
		int numeroScrittureAvere = 0;
		
		BigDecimal totaleDare = BigDecimal.ZERO;
		BigDecimal totaleAvere = BigDecimal.ZERO;
		
		List<ElementoScritturaPrimaNotaLibera> elaborata = model.getListaElementoScritturaPerElaborazione();
		List<MovimentoDettaglio> listaMovimentiDettaglioFinal = new ArrayList<MovimentoDettaglio>();
		
		for (ElementoScritturaPrimaNotaLibera elementoScrittura : elaborata){
			if (elementoScrittura != null && elementoScrittura.getMovimentoDettaglio() != null){
				BigDecimal importo = elementoScrittura.getMovimentoDettaglio().getImporto();
				
				if(importo != null && importo.signum() != 0) {
					// Aggiungo i dati al segno dare o avere
					if(elementoScrittura.isSegnoDare()) {
						numeroScrittureDare++;
						totaleDare = totaleDare.add(importo);
					} else if(elementoScrittura.isSegnoAvere()) {
						numeroScrittureAvere++;
						totaleAvere = totaleAvere.add(importo);
					}
					
					listaMovimentiDettaglioFinal.add(elementoScrittura.getMovimentoDettaglio());
				} else {
					checkCondition(false,
						ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Conto " + elementoScrittura.getMovimentoDettaglio().getConto().getCodice()
							+ " " + elementoScrittura.getMovimentoDettaglio().getSegno()));
				}
				
			}
		}
		
		checkCondition(numeroScrittureDare > 0 && numeroScrittureAvere > 0,
			ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Devono essere presenti almeno due conti con segni differenti"));
		checkCondition(totaleDare.subtract(totaleAvere).signum() == 0,
			ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il totale DARE deve essere UGUALE al totale AVERE"));
		
		// SIAC-5690: Ripristino eventuale della missione
		ripristinaMissione(listaMovimentiDettaglioFinal);
		
		return listaMovimentiDettaglioFinal;
	}
	
	/**
	 * Ripristino della missione. La missione talvolta si perde, per motivazioni non chiare.
	 * @param listaMovimentiDettaglio la lista dei movimenti di dettaglio
	 */
	private void ripristinaMissione(List<MovimentoDettaglio> listaMovimentiDettaglio) {
		for(MovimentoDettaglio md : listaMovimentiDettaglio) {
			if(md.getMissione() != null && StringUtils.isNotBlank(md.getMissione().getCodice())) {
				// Ho il codice. Per sicurezza riprendo il dato
				Missione m = ComparatorUtils.findByCodice(model.getListaMissione(), md.getMissione().getCodice());
				md.setMissione(m);
			}
		}
	}


	/**
	 * Aggiornamento del numero di riga per i movimenti
	 */
	protected void aggiornaNumeroRiga() {
		for(MovimentoEP mep : model.getListaMovimentoEP()) {
			int numeroRiga = 0;
			for(MovimentoDettaglio md : mep.getListaMovimentoDettaglio()) {
				md.setNumeroRiga(numeroRiga);
				numeroRiga++;
			}
		}
	}

}
