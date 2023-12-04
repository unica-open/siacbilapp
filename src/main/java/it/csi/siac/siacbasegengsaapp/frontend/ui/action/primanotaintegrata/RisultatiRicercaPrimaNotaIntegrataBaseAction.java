/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.CollegaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.CollegaPrimeNoteResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCollegamentoPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCollegamentoPrimeNoteResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNoteResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoEvento;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;
/**
 * Classe abse di action per i risultati di ricerca della prima nota integrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class RisultatiRicercaPrimaNotaIntegrataBaseAction<M extends RisultatiRicercaPrimaNotaIntegrataBaseModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3468061915930921975L;
	
	/** Nome di sessione per i risultati di ricerca del modulo GEN */
	public static final String MODEL_SESSION_NAME_RIS_RICERCA_FIN = "RisultatiRicercaPrimaNotaIntegrataFINModel";
	/** Nome di sessione per i risultati di ricerca del modulo GSA */
	public static final String MODEL_SESSION_NAME_RIS_RICERCA_GSA = "RisultatiRicercaPrimaNotaIntegrataGSAModel";
	
	@Autowired private transient PrimaNotaService primaNotaService;
	/** Serviz&icirc; delle codifiche */
	@Autowired private transient CodificheService codificheService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		// Imposto la posizione die start
		Integer posizioneStart = ottieniPosizioneStartDaSessione();
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart);
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public String execute() throws Exception {
		// Leggo i messaggi delle azioni precedenti
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		caricaListe();
		impostaFaseBilancioPrecedente();
		impostaMessaggioDiRiepilogoRicerca();
		return SUCCESS;
	}
	
	/**
	 * Ottiene il parametro di sessione del riepilogo
	 * @return il parametro di sessione
	 */
	protected abstract BilSessionParameter getBilSessionParameterRiepilogo();
	
	private void impostaMessaggioDiRiepilogoRicerca() {
		String riepilogo = sessionHandler.getParametro(getBilSessionParameterRiepilogo());
		sessionHandler.setParametro(getBilSessionParameterRiepilogo(), null);
		model.setRiepilogoRicerca("");
		if(riepilogo != null) {
			model.setRiepilogoRicerca(" per " + riepilogo);
		}
	}

	/**
	 * Aggiornamento della prima nota.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Uid della prima nota da aggiornare: " + model.getPrimaNota().getUid());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Aggiornamento della prima nota.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaPrimaNotaDocumento() {
		return aggiorna();
	}
	
	/**
	 * Consultazione della prima nota.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid della prima nota da consultare: " + model.getPrimaNota().getUid());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #annulla()}.
	 */
	public void validateAnnulla() {
		checkNotNullNorInvalidUid(model.getPrimaNota(), "Prima nota integrata da annullare");
	}
	
	/**
	 * Annullamento della causale EP.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		addErrore(ErroreCore.TIPO_AZIONE_NON_SUPPORTATA.getErrore("annullamento della prima nota integrata"));
		return INPUT;
	}
	
	/**
	 * Validazione per il metodo {@link #valida()}.
	 */
	public void validateValida() {
		checkNotNullNorInvalidUid(model.getPrimaNota(), "Prima nota integrata da validare");
	}
	
	/**
	 * Validazione della causale EP.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String valida() {
		final String methodName = "valida";
		
		ValidaPrimaNota request = model.creaRequestValidaPrimaNota();
		logServiceRequest(request);
		ValidaPrimaNotaResponse response = primaNotaService.validaPrimaNota(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(ValidaPrimaNota.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Validata la causale con uid " + model.getPrimaNota().getUid());
		
		// Imposto il parametro di rientro, si' da ricalcolare i dati
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	
	//-------Gestione collegamento Prima Nota
	
	/**
	 * Caricamento della lista delle prime note collegate
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaPrimeNoteCollegate(){
		caricaDettaglioPrimaNota();
		if(hasErrori()){
			return SUCCESS;
		}
		model.setListaPrimeNoteCollegate(model.getPrimaNota().getListaPrimaNotaFiglia());
		return SUCCESS;
	}

	/**
	 * Collegamento di una prima nota
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String collegaPrimaNota(){
		//ricerca la prima nota da collegare a partire da anno-numero-tipo
		List<PrimaNota> primaNotaDaCollegare = ricercaPrimaNota();
		if(hasErrori()){
			return SUCCESS;
		}
		//se non la trovo
		if(primaNotaDaCollegare == null || primaNotaDaCollegare.isEmpty()){
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Prima nota", "anno " + " numero " + " e tipo "));
			return SUCCESS;
		}
		//se ne trovo piu' di 1
		if(primaNotaDaCollegare.size() > 1){
			addErrore(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore(": non e' possibile determinare la prima nota in modo univoco per i valori inseriti."));
			return SUCCESS;
		}
		//ne ho trovata esattamente 1, la collego
		PrimaNota pNotaDaCollegare = primaNotaDaCollegare.get(0);
		for(PrimaNota pn : model.getListaPrimeNoteCollegate()){
			if(pn.getUid() == pNotaDaCollegare.getUid()){
				addErrore(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore("la prima nota selezionata e' gia' stata collegata. "));
				return SUCCESS;
			}
		}
		TipoRelazionePrimaNota motivazione = ComparatorUtils.searchByUid(model.getListaMotivazioni(), model.getMotivazione());
		pNotaDaCollegare.setTipoRelazionePrimaNota(motivazione);
		pNotaDaCollegare.setNoteCollegamento(model.getNoteCollegamento());
		collegaPrimeNote(pNotaDaCollegare);
		if(hasErrori()){
			return SUCCESS;
		}
		model.getListaPrimeNoteCollegate().add(pNotaDaCollegare);
		model.setMotivazione(null);
		model.setNoteCollegamento(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	/**
	 * Eliminazione di una prima nota collegata
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaCollegamento(){
		int idx = model.getIndicePrimaNota().intValue();
		PrimaNota primaNotaDaScollegare = model.getListaPrimeNoteCollegate().get(idx);
		EliminaCollegamentoPrimeNote reqECPM = model.creaRequestEliminaCollegamentoPrimeNote(primaNotaDaScollegare);
		EliminaCollegamentoPrimeNoteResponse resECPN = primaNotaService.eliminaCollegamentoPrimeNote(reqECPM);
		if(resECPN.hasErrori()){
			addErrori(resECPN);
			model.setIndicePrimaNota(null);
			return SUCCESS;
		}
		model.getListaPrimeNoteCollegate().remove(idx);
		model.setIndicePrimaNota(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validate per il metodo {@link #collegaPrimaNota()}.
	 */
	public void validateCollegaPrimaNota(){
		checkNotNull(model.getAnnoPrimaNota(), "anno prima nota");
		checkNotNull(model.getPrimaNotaDaCollegare().getTipoCausale(), "tipo prima nota");
		checkNotNull(model.getPrimaNotaDaCollegare().getNumeroRegistrazioneLibroGiornale(), "numero definitivo prima nota");
	}
	
	/**
	 * Caricamento del dettaglio della prima nota.
	 */
	private void caricaDettaglioPrimaNota() {
		RicercaDettaglioPrimaNota reqRDPN = model.creaRequestRicercaDettaglioPrimaNota();
		RicercaDettaglioPrimaNotaResponse resRDPN = primaNotaService.ricercaDettaglioPrimaNota(reqRDPN);
		if(resRDPN.hasErrori()){
			addErrori(resRDPN);
		}
		model.setPrimaNota(resRDPN.getPrimaNota());
	}
	/**
	 * Ricerca della prima nota
	 * @return le prime note ottenute
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
	 * Collegamento delle prime note
	 * @param pNotaDaCollegare la prima nota da collegare
	 */
	private void collegaPrimeNote(PrimaNota pNotaDaCollegare) {
		CollegaPrimeNote reqCPN = model.creaRequestCollegaPrimeNote(pNotaDaCollegare);
		CollegaPrimeNoteResponse resCPN = primaNotaService.collegaPrimeNote(reqCPN);
		if(resCPN.hasErrori()){
			addErrori(resCPN);
		}
	}

	/**
	 * Caricamento delle liste
	 */
	private void caricaListe(){
		try {
			caricaListaTipiEvento();
			caricaListaMotivazioni();
		} catch(WebServiceInvocationFailureException wsife) {
			throw new GenericFrontEndMessagesException(wsife);
		}
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
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(TipoEvento.class);
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String msgErrore = createErrorInServiceInvocationString(RicercaCodifiche.class, response);
				throw new WebServiceInvocationFailureException(msgErrore);
			}
			listaTipiEvento = response.getCodifiche(TipoEvento.class);
		}
		model.setListaTipiEvento(listaTipiEvento);
		sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_EVENTO, listaTipiEvento);
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
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(TipoRelazionePrimaNota.class);
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String msgErrore = createErrorInServiceInvocationString(RicercaCodifiche.class, response);
				throw new WebServiceInvocationFailureException(msgErrore);
			}
			listaTipoRelazione = new ArrayList<TipoRelazionePrimaNota>();
			for(TipoRelazionePrimaNota tr : response.getCodifiche(TipoRelazionePrimaNota.class)){
				if(Boolean.TRUE.equals(tr.getRelazioneUtilizzabile())){
					listaTipoRelazione.add(tr);
				}
			}
		}
		model.setListaMotivazioni(listaTipoRelazione);
		sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_RELAZIONE, listaTipoRelazione);
	}
	
	/**
	 * caricamento del bilancio relativo all'anno precedente
	 */
	private void impostaFaseBilancioPrecedente() {
		RicercaDettaglioBilancio ricercaBilancio = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse ricercaBilancioResponse = bilancioService.ricercaDettaglioBilancio(ricercaBilancio);
		FaseBilancio fase = ricercaBilancioResponse.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO_PRECEDENTE, fase);
		
	}
	
	//SIAC-5176
	/**
	 * Aggiornamento della prima nota.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String gestioneRateiDocumento() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Uid della prima nota a cui collegare il rateo o il risconto: " + model.getPrimaNota().getUid());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	//SIAC-5176
		/**
		 * Aggiornamento della prima nota.
		 * 
		 * @return una stringa corrispondente al risultato dell'invocazione
		 */
		public String gestioneRiscontiDocumento() {
			final String methodName = "aggiorna";
			log.debug(methodName, "Uid della prima nota a cui collegare il rateo o il risconto: " + model.getPrimaNota().getUid());
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
			return SUCCESS;
		}
	
}
