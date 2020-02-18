/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;
import org.softwareforge.struts2.breadcrumb.Crumb;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Preparable;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.BilancioService;
import it.csi.siac.siaccommonapp.action.GenericAction;
import it.csi.siac.siaccommonapp.handler.session.CommonSessionParameter;
import it.csi.siac.siaccommonapp.interceptor.anchor.Anchor;
import it.csi.siac.siaccommonapp.interceptor.anchor.AnchorStack;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.UtenteNonLoggatoException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.FindAzione;
import it.csi.siac.siaccorser.frontend.webservice.msg.FindAzioneResponse;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.errore.TipoErrore;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * 
 * Specializza le Action di bilancio che utilizzano GenericBilancioModel.
 * Prepara il Model con i parametri comuni di bilancio.
 * 
 * @author Domenico
 *
 * @param <M> Model di riferimento
 */
public abstract class GenericBilancioAction<M extends GenericBilancioModel> extends GenericAction<M> {
	
	private static final long serialVersionUID = -5022368968877953816L;
	
	/** Stringa per il risultato di richiesta */
	protected static final String ASK = "ask";
	/** Il titolo del model, per la gestione con i Crumb */
	protected static final String MODEL_TITOLO = "%{model.titolo}";
	
	/** Serviz&icirc; del bilancio */
	@Autowired protected transient BilancioService bilancioService;

	@Autowired private transient CoreService coreService;
//	//SIAC-6884
//		private boolean isDecentrato;
//			
//		public boolean isDecentrato() {
//			return isDecentrato;
//		}
//
//		public void setDecentrato(boolean isDecentrato) {
//			this.isDecentrato = isDecentrato;
//		}
//		//END SIAC-6884
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		//Forzo
		getCduChiamante();
		// Imposto il CDU se e solo se non presente nel model
		if(model.getCdu() == null) {
			creaCdu();
		}
		
		final String methodName = "prepare";
		
		try {
			// Imposta nel model il richiedente, l'account, l'ente e l'anno di esercizio dalla sessione
			log.debug(methodName, "Inizializzazione del richiedente");
			model.setRichiedente(sessionHandler.getRichiedente());
			log.debug(methodName, "Inizializzazione dell'account");
			model.setAccount(sessionHandler.getAccount());
			log.debug(methodName, "Inizializzazione dell'ente");
			model.setEnte(sessionHandler.getAccount().getEnte());
			log.debug(methodName, "Inizializzazione dell'anno di esercizio");
			model.setAnnoEsercizio(sessionHandler.getAnnoEsercizio());
			log.debug(methodName, "Inizializzazione della descrizione del bilancio");
			model.setDescrizioneAnnoBilancio(sessionHandler.getDescrizioneAnnoBilancio());
			log.debug(methodName, "Inizializzazione del bilancio");
			model.setBilancio(sessionHandler.getBilancio());
			log.debug(methodName, "Inizializzazione dell'anno di bilancio per il Bilancio");
			model.impostaAnnoBilancio();
			log.debug(methodName, "Inizializzazione dell'azione richiesta");
			model.setAzioneRichiesta(sessionHandler.getAzioneRichiesta());

			//SIAC-6884
			/*Account account = sessionHandler.getAccount();
			boolean decentrato = AzioniConsentiteFactory.isConsentito(AzioniConsentite.INSERISCI_VARIAZIONE_DECENTRATA, sessionHandler.getAzioniConsentite());
			setDecentrato(decentrato);
			model.setIsDecentrato(decentrato);
			//END SIAC-6884
			 * 
			 */
		} catch(Exception exception) {
			// Errore nell'impostazione dei dati. La sessione era terminata
			log.error(methodName, "Errore nella configurazione dei campi comuni");
			throw new UtenteNonLoggatoException("Errore nella configurazione dei campi comuni", exception);
		}
	}
	
	/**
	 * @see Preparable#prepare()
	 * @throws Exception in caso di errore nell'invocazione del metodo
	 */
	public void prepareExecute() throws Exception {
		// Empty
	}
	
	/**
	 * Metodo per tornare alla precedente azione.
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String back() {
		// Non effettuo operazioni
		return SUCCESS;
	}
	
	/**
	 * Effettua un log degli actionErrors e degli actionMessages.
	 */
	protected void logActionErrorsAndMessages() {
		final String methodName= "logActionErrorsAndMessages";
		
		// Log degli ActionErrors
		for(String errore : getActionErrors()){
			log.info(methodName, "Errore action: " + errore);
		}
		// Log dei FieldErrors
		for(Entry<String, List<String>> errore : getFieldErrors().entrySet()) {
			for(String error : errore.getValue()) {
				log.info(methodName, "Errore field: " + errore.getKey() + " - " + error);
			}
		}
		// Log degli ActionMessages
		for(String errore : getActionMessages()){
			log.info(methodName, "Messaggio action: " + errore);
		}
		// Log degli ActionMessages
		for(String errore : getActionMessages()){
			log.info(methodName, "Messaggio action: " + errore);
		}
		// Log delle informazioni
		for(Informazione informazione : model.getInformazioni()) {
			log.info(methodName, "Informazione action: " + informazione.getTesto());
		}
	}
	
	
	
	/**
	 * Controlla se l'id dell'{@link Entita} sia presente.
	 * @param e l'Entita di cui controllare l'id
	 * @return <code>true</code> se l'Entita ha un id valorizzato correttamente; <code>false</code> altrimenti
	 */
	protected boolean idEntitaPresente(Entita e) {
		// Delego al metodo del model
		return model.idEntitaPresente(e);
	}
	
	/**
	 * Controlla se l'id dell'{@link Entita} sia presente. In tal caso, comunica con un log la classe e la presenza di tale uid.
	 * @param e l'Entita di cui controllare l'id
	 * @return <code>true</code> se l'Entita ha un id valorizzato correttamente; <code>false</code> altrimenti
	 */
	protected boolean checkPresenzaIdEntita(Entita e){
		// Delego al metodo del model
		return model.checkPresenzaIdEntita(e);
	}
	
	/**
	 * Controlla se la Stringa &eacute; stata valorizzata in modo corretto.
	 * @param s la stringa da controllare
	 * @return <code>true</code> se la Stringa &eacute; non-null e non vuota; <code>false</code> altrimenti
	 */
	protected boolean stringaValorizzata(String s){
		// Delego al metodo del model
		return model.stringaValorizzata(s);
	}
	
	/**
	 * Controlla se la Stringa &eacute; stata valorizzata in modo corretto. In tal caso, comunica con un log il nome della stringa.
	 * @param s         la stringa da controllare
	 * @param nomeCampo il nome del campo riferentesi
	 * @return <code>true</code> se la Stringa &eacute; non-null e non vuota; <code>false</code> altrimenti
	 */
	protected boolean checkStringaValorizzata(String s, String nomeCampo){
		// Delego al metodo del model
		return model.checkStringaValorizzata(s, nomeCampo);
	}
	
	/**
	 * Controlla se il campo passato in input &eacute; valorizzato. In tal caso, comunica con un log il nome del campo.
	 * @param campo     il campo da controllare
	 * @param nomeCampo il nome del campo riferentesi
	 * @return <code>true</code> se il campo &eacute; non-null; <code>false</code> altrimenti
	 */
	protected boolean checkCampoValorizzato(Object campo, String nomeCampo) {
		// Delego al metodo del model
		return model.checkCampoValorizzato(campo, nomeCampo);
	}
	
	/**
	 * Controlla se la condizione fornita sia rispettata. In tal caso, comunica con un log il nome del campo.
	 * @param condition la condizione da controllare
	 * @param nomeCampo il nome del campo riferentesi
	 * @return <code>true</code> se la condizione &eacute; valida; <code>false</code> altrimenti
	 */
	protected boolean checkCondizioneValida(boolean condition, String nomeCampo) {
		// Delego al metodo del model
		return model.checkCondizioneValida(condition, nomeCampo);
	}
	
	/**
	 * Ottiene il cdu chiamante.
	 * @return the cduChiamante
	 */
	public String getCduChiamante() {
		final String methodName = "getCduChiamante";
		// Ottengo la precedente ancora
		Anchor previousAnchor = getPreviousAnchor();
		String result = "";
		try {
			// Ottengo lo use-case precedente
			result = previousAnchor.getUseCase();
			// Imposto il dato nel model
			model.setCduChiamante(result);
		} catch(Exception e) {
			// Ancore non presenti
			log.debug(methodName, "Non vi sono ancore precedenti");
		}
		return result;
	}
	
	/**
	 * Controlla se si ha un CDU chiamante.
	 * @param cduPossibili i possibili CDU da controllare
	 * @return <code>true</code> se si &eacute; nel caso di un CDU chiamante tra quelli indicati; <code>false</code> in caso contrario
	 */
	protected boolean daCduChiamante(String... cduPossibili) {
		// Fallback in caso di null
		if(cduPossibili == null) {
			return false;
		}
		boolean result = false;
		String cduChiamante = getCduChiamante();
		// Controlla se si e' nel caso di un CDU possibile
		for(int i = 0; i < cduPossibili.length && !result; i++) {
			result = cduChiamante.contains(cduPossibili[i]);
		}
		
		return result;
	}
	
	/**
	 * Definisce il Cdu.
	 */
	private void creaCdu() {
		// Prende il dato del chiamante
		String result = getCduChiamante();
		// Imposta il dato nel model
		model.setCdu(result);
	}
	
	/**
	 * Metodo per il riavvolgimento dell'azione al chiamante
	 * @return Una stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "", rewind = true)
	public String rewind() {
		// Imposta il cduChiamante nel model
		getCduChiamante();
		// Pulisce la sessione
		sessionHandler.cleanAllSafely();
		return SUCCESS;
	}
	
	
	/**
	 * Ottiene l'ancora precedente a partire dallo stack delle ancore.
	 * @return the previousAnchor
	 */
	public Anchor getPreviousAnchor() {
		final String methodName = "getPreviousAnchor";
		// Il valore di default quando non vi sono precedenti actions è null
		Anchor previousAnchor = null;
		// Ottiene lo stack dalla sessione
		AnchorStack stack = sessionHandler.getParametro(CommonSessionParameter.ANCHOR_STACK, AnchorStack.class);
		try {
			// La dimensione dello stack
			int anchorNumber = stack.getAnchorStack().size();
			// Ottengo l'ancora precedente a quella attuale
			previousAnchor = stack.getAnchorStack().get(anchorNumber - 2);
		} catch (Exception e) {
			// Lo stack non ha sufficienti elementi
			log.debug(methodName, "Il trail delle ancore precedenti non contiene sufficienti ancore");
		}
		return previousAnchor;
	}
	
	/**
	 * Ottiene il crumb corrente..
	 * @return the currentCrumb
	 */
	public Crumb getCurrentCrumb() {
		final String methodName = "getCurrentCrumb";
		// Ottengo il crumb corrente
		Crumb currentCrumb = null;
		// Il trail dei breadcrumb
		BreadCrumbTrail trail = sessionHandler.getParametro(CommonSessionParameter.BREADCRUMB_TRAIL, BreadCrumbTrail.class);
		try {
			// La dimensione del trail
			int numeroCrumbs = trail.getCrumbs().size();
			// Ottengo il crumb precedente a quello attuale
			currentCrumb = trail.getCrumbs().get(numeroCrumbs - 1);
		} catch (Exception e) {
			// Il trail non ha sufficienti elementi
			log.debug(methodName, "Il trail delle azioni precedenti non contiene sufficienti crumbs");
		}
		return currentCrumb;
	}

	/**
	 * Ottiene il numero delle precedenti ancore.
	 * @return the numeroPrecedentiAncore
	 */
	public Integer getNumeroPrecedentiAncore() {
		final String methodName = "getNumeroPrecedentiAncore";
		// Inizializzo a null
		Integer result = null;
		// Lo stack delle ancore
		AnchorStack stack = sessionHandler.getParametro(CommonSessionParameter.ANCHOR_STACK, AnchorStack.class);
		try {
			// Recupera la dimensione dello stack
			result = Integer.valueOf(stack.getAnchorStack().size());
		} catch (Exception e) {
			// Lo stack non e' popolato
			log.debug(methodName, "Il trail delle ancore precedenti non contiene sufficienti crumbs");
		}
		return result;
	}
	
	/**
	 * Toglie il crumb corrente dal trail.
	 */
	protected void dissolveCurrentCrumb() {
		String methodName = "dissolveCurrentCrumb";
		// Ottengo il trail dei breadcrumb
		BreadCrumbTrail trail = sessionHandler.getParametro(CommonSessionParameter.BREADCRUMB_TRAIL, BreadCrumbTrail.class);
		try {
			// Ottengo l'ultimo crumb
			Crumb crumb = trail.getCrumbs().pop();
			log.debug(methodName, "Crumb attuale: " + crumb.getName());
			// Imposto il trail in sessione
			sessionHandler.setParametro(CommonSessionParameter.BREADCRUMB_TRAIL, trail);
		} catch (Exception e) {
			// Non ci sono sufficienti crumb
			this.log.debug(methodName, "Il trail delle azioni precedenti non contiene sufficienti crumbs");
		}
	}
	
	/**
	 * Controlla se la response abbia errori, e in tal caso popola gli errori nella action.
	 * @param res        la response i cui errori devono essere controllati
	 * @param methodName il nome del metodo chiamante
	 * @return <code>true</code> se vi sono stati errori; <code>false</code> in caso contrario
	 */
	protected boolean checkErroriResponse(ServiceResponse res, String methodName) {
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nella response");
			addErrori(res);
			return true;
		}
		return false;
	}
	
	/**
	 * Controlla che il caso d'uso sia applicabile.
	 * <br>
	 * In caso contrario, lancia un'eccezione.
	 * @param cdu la definizione del CDU
	 * @throws GenericFrontEndMessagesException nel caso in cui il caso d'uso non sia applicabile
	 */
	protected void checkCasoDUsoApplicabile(String cdu) {
		// Empty
	}
	
	/**
	 * Controlla che il caso d'uso sia applicabile.
	 * <br>
	 * In caso contrario, lancia un'eccezione.
	 * @throws GenericFrontEndMessagesException nel caso in cui il caso d'uso non sia applicabile
	 * @see #checkCasoDUsoApplicabile(String)
	 */
	protected void checkCasoDUsoApplicabile() {
		// Redirige verso l'implementazione con il CDU
		checkCasoDUsoApplicabile("");
	}
	
	/**
	 * Imposta in sessione un parametro relativo al successo dell'azione attuale per una redirezione ad un'azione futura.
	 */
	protected void impostaInformazioneSuccessoAzioneInSessionePerRedirezione() {
		// Impostazione dell'informazione di successo senza dati opzionali
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione("");
	}
	
	/**
	 * Imposta in sessione un parametro relativo al successo dell'azione attuale per una redirezione ad un'azione futura.
	 * 
	 * @param optional le eventuali informazioni aggiuntive
	 */
	protected void impostaInformazioneSuccessoAzioneInSessionePerRedirezione(String optional) {
		// Creo la lista delle informazioni
		List<Informazione> lista = new ArrayList<Informazione>();
		// Imposto l'informazione di sucesso
		lista.add(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo" + optional));
		// Imposto la lista in sessione
		sessionHandler.setParametro(BilSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE, lista);
	}
	
	/**
	 * Aggiunge l'informazione.
	 * 
	 * @param errore l'informazione da aggiungere
	 */
	protected void addInformazione(Errore errore) {
		// Aggiunge l'informazione fornita
		addInformazione(errore == null ? null : new Informazione(errore.getCodice(), errore.getDescrizione()));
	}
	
	/**
	 * Controlla se in sessione si abbiano informazioni relative ad un eventuale successo di un'azione precedente.
	 * <br>
	 * In tal caso, pulisce la sessione e comunica all'utente tale successo.
	 */
	protected void leggiEventualiInformazioniAzionePrecedente() {
		// Recupera le informazioni da sessione
		List<Informazione> listaInformazioniPrecedenti = sessionHandler.getParametro(BilSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE);
		if(listaInformazioniPrecedenti != null) {
			// Pulisco la sessione
			sessionHandler.setParametro(BilSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE, null);
			for(Informazione info : listaInformazioniPrecedenti) {
				// Aggiungo ogni informazione nel model
				addInformazione(info);
			}
		}
	}
	
	/**
	 * Controlla se in sessione si abbiano messagg&icirc; relativi a un'azione precedente.
	 * <br>
	 * In tal caso, pulisce la sessione e comunica all'utente tali messaggi.
	 */
	protected void leggiEventualiMessaggiAzionePrecedente() {
		// Recupera i messaggi da sessione
		List<Messaggio> listaMessaggiPrecedenti = sessionHandler.getParametro(BilSessionParameter.MESSAGGI_AZIONE_PRECEDENTE);
		if(listaMessaggiPrecedenti != null) {
			// Pulisco la sessione
			sessionHandler.setParametro(BilSessionParameter.MESSAGGI_AZIONE_PRECEDENTE, null);
			for(Messaggio mess : listaMessaggiPrecedenti) {
				// Aggiungo ogni messaggio nel model
				addMessaggio(mess);
			}
		}
	}
	
	/**
	 * Controlla se in sessione si abbiano errori relativi a un'azione precedente.
	 * <br>
	 * In tal caso, pulisce la sessione e comunica all'utente tali errori.
	 */
	protected void leggiEventualiErroriAzionePrecedente() {
		// Recupera gli errori da sessione
		List<Errore> listaErroriPrecedenti = sessionHandler.getParametro(BilSessionParameter.ERRORI_AZIONE_PRECEDENTE);
		if(listaErroriPrecedenti != null) {
			// Pulisco la sessione
			sessionHandler.setParametro(BilSessionParameter.ERRORI_AZIONE_PRECEDENTE, null);
			for(Errore err : listaErroriPrecedenti) {
				// Aggiungo ogni errore nel model
				addErrore(err);
			}
		}
	}
	
	/**
	 * Controlla se in sessione si abbiano errori, messaggi e informazioni relativi a un'azione precedente.
	 */
	protected void leggiEventualiErroriMessaggiInformazioniAzionePrecedente() {
		// Wrapper per la lettura di errori, messaggi e informazioni
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
	}
	
	
	
	
	
	
	
	
	/**
	 * Metodo di utilit&agrave; per il controllo di validit&agrave; di una condizione.
	 * <br>
	 * Nel caso in cui la condizione non sia rispettata, aggiunge l'errore all'interno della action.
	 * @param condition la condizione da verificare
	 * @param errore    l'errore da injettare
	 */
	protected void checkCondition(boolean condition, Errore errore) {
		// Non rilancia l'eccezione
		checkCondition(condition, errore, false);
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di validit&agrave; di una condizione.
	 * <br>
	 * Nel caso in cui la condizione non sia rispettata, aggiunge il messaggio all'interno della action.
	 * @param condition la condizione da verificare
	 * @param messaggio il messaggio da injettare
	 */
	protected void warnCondition(boolean condition, Messaggio messaggio) {
		// Non rilancia l'eccezione
		warnCondition(condition, messaggio, false);
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di validit&agrave; di una condizione.
	 * <br>
	 * Nel caso in cui la condizione non sia rispettata, aggiunge l'errore all'interno della action.
	 * @param condition la condizione da verificare
	 * @param errore    l'errore da injettare
	 */
	protected void warnCondition(boolean condition, Errore errore) {
		// Non rilancia l'eccezione
		warnCondition(condition, errore, false);
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di validit&agrave; di una condizione e per l'opzionale lancio di un'eccezione.
	 * <br>
	 * Nel caso in cui la condizione non sia rispettata, aggiunge l'errore all'interno della action.
	 * @param condition      la condizione da verificare
	 * @param errore         l'errore da injettare nel caso in cui la condizione sia violata
	 * @param throwException se l'eccezione sia da sollevare
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione 
	 */
	protected void checkCondition(boolean condition, Errore errore, boolean throwException) {
		// Controllo della condizione
		if(!condition) {
			// Aggiunge l'errore
			addErrore(errore);
			if(throwException) {
				// Lancia l'errore se richiesto
				throw new ParamValidationException("Error found: " + errore.getTesto());
			}
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di validit&agrave; di una condizione e per l'opzionale lancio di un'eccezione.
	 * <br>
	 * Nel caso in cui la condizione non sia rispettata, aggiunge l'errore come messaggio all'interno della action.
	 * @param condition      la condizione da verificare
	 * @param errore         l'errore da injettare nel caso in cui la condizione sia violata
	 * @param throwException se l'eccezione sia da sollevare
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione 
	 */
	protected void warnCondition(boolean condition, Errore errore, boolean throwException) {
		// Controlla la condizione
		if(!condition) {
			// Aggiunge il messaggio
			addMessaggio(errore);
			if(throwException) {
				// Lancia l'errore se richiesto
				throw new ParamValidationException("Errore verificatosi: " + errore.getTesto());
			}
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di validit&agrave; di una condizione e per l'opzionale lancio di un'eccezione.
	 * <br>
	 * Nel caso in cui la condizione non sia rispettata, aggiunge il messaggio all'interno della action.
	 * 
	 * @param condition      la condizione da verificare
	 * @param messaggio      il messaggio da injettare nel caso in cui la condizione sia violata
	 * @param throwException se l'eccezione sia da sollevare
	 * 
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione
	 */
	protected void warnCondition(boolean condition, Messaggio messaggio, boolean throwException) {
		// Controlla la condizione
		if(!condition) {
			// Aggiunge il messaggio
			addMessaggio(messaggio);
			if(throwException) {
				// Lancia l'errore se richiesto
				throw new ParamValidationException("Errore verificatosi: " + messaggio.getCodice() + " "+ messaggio.getDescrizione());
			}
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; per un dato campo.
	 * <br>
	 * Nel caso in cui il campo sia <code>null</code>, aggiunge un errore alla action.
	 * @param campo			il campo da controllare
	 * @param nomeCampo		il nome del campo
	 */
	protected void checkNotNull(Object campo, String nomeCampo) {
		// Non rilancia l'eccezione
		checkNotNull(campo, nomeCampo, false);
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; per un dato campo.
	 * <br>
	 * Nel caso in cui il campo sia <code>null</code>, aggiunge un errore alla action.
	 * @param campo          il campo da controllare
	 * @param nomeCampo      il nome del campo
	 * @param throwException se sia da rilanciare un'eccezione
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione
	 */
	protected void checkNotNull(Object campo, String nomeCampo, boolean throwException) {
		// Controlla che il campo non sia null
		checkCondition(campo != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo), throwException);
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; per un dato campo.
	 * <br>
	 * Nel caso in cui il campo sia <code>null</code>, aggiunge un messaggio alla action.
	 * @param campo			il campo da controllare
	 * @param nomeCampo		il nome del campo
	 */
	protected void warnNotNull(Object campo, String nomeCampo) {
		// Non rilancia l'eccezione
		warnNotNull(campo, nomeCampo, false);
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; per un dato campo.
	 * <br>
	 * Nel caso in cui il campo sia <code>null</code>, aggiunge un messaggio alla action.
	 * @param campo          il campo da controllare
	 * @param nomeCampo      il nome del campo
	 * @param throwException se sia da rilanciare un'eccezione
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione
	 */
	protected void warnNotNull(Object campo, String nomeCampo, boolean throwException) {
		// Controlla che il campo non sia null
		warnCondition(campo != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo), throwException);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; vuoto.
	 * <br>
	 * In caso contrario, aggiunge un errore alla action.
	 * @param campo			il campo da controllare
	 * @param nomeCampo		il nome del campo
	 */
	protected void checkNotNullNorEmpty(String campo, String nomeCampo) {
		// Non rilancio l'eccezione
		checkNotNullNorEmpty(campo, nomeCampo, false);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; vuoto.
	 * <br>
	 * In caso contrario, aggiunge un errore alla action.
	 * @param campo          il campo da controllare
	 * @param nomeCampo      il nome del campo
	 * @param throwException se sia da rilanciare un'eccezione
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione
	 */
	protected void checkNotNullNorEmpty(String campo, String nomeCampo, boolean throwException) {
		// Controllo che il campo non sia vuoto
		checkCondition(StringUtils.isNotBlank(campo), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo), throwException);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; vuoto.
	 * <br>
	 * In caso contrario, aggiunge un messaggio alla action.
	 * @param campo			il campo da controllare
	 * @param nomeCampo		il nome del campo
	 */
	protected void warnNotNullNorEmpty(String campo, String nomeCampo) {
		// Non rilancio l'eccezione
		warnNotNullNorEmpty(campo, nomeCampo, false);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; vuoto.
	 * <br>
	 * In caso contrario, aggiunge un messaggio alla action.
	 * @param campo          il campo da controllare
	 * @param nomeCampo      il nome del campo
	 * @param throwException se sia da rilanciare un'eccezione
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione
	 */
	protected void warnNotNullNorEmpty(String campo, String nomeCampo, boolean throwException) {
		// Controllo che il campo non sia vuoto
		warnCondition(StringUtils.isNotBlank(campo), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo), throwException);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; abbia un uid invalido.
	 * <br>
	 * In caso contrario, aggiunge un errore alla action.
	 * @param campo			il campo da controllare
	 * @param nomeCampo		il nome del campo
	 */
	protected void checkNotNullNorInvalidUid(Entita campo, String nomeCampo) {
		// Non rilancio l'eccezione
		checkNotNullNorInvalidUid(campo, nomeCampo, false);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; abbia un uid invalido.
	 * <br>
	 * In caso contrario, aggiunge un errore alla action.
	 * @param campo          il campo da controllare
	 * @param nomeCampo      il nome del campo
	 * @param throwException se sia da rilanciare un'eccezione
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione
	 */
	protected void checkNotNullNorInvalidUid(Entita campo, String nomeCampo, boolean throwException) {
		// Controllo che il campo sia valorizzato con un uid
		checkCondition(campo != null && campo.getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo), throwException);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; abbia un uid invalido.
	 * <br>
	 * In caso contrario, aggiunge un messaggio alla action.
	 * @param campo			il campo da controllare
	 * @param nomeCampo		il nome del campo
	 */
	protected void warnNotNullNorInvalidUid(Entita campo, String nomeCampo) {
		// Non rilancio l'eccezione
		warnNotNullNorInvalidUid(campo, nomeCampo, false);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; abbia un uid invalido.
	 * <br>
	 * In caso contrario, aggiunge un messaggio alla action.
	 * @param campo          il campo da controllare
	 * @param nomeCampo      il nome del campo
	 * @param throwException se sia da rilanciare un'eccezione
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione
	 */
	protected void warnNotNullNorInvalidUid(Entita campo, String nomeCampo, boolean throwException) {
		// Controllo che il campo sia valorizzato con un uid
		warnCondition(campo != null && campo.getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo), throwException);
	}
	
	/**
	 * Controlla se l'entita sia presente nella lista fornita.
	 * @param <E> la tipizzazione dell'entita
	 * @param entita     l'entit&agrave; da controllare
	 * @param lista      la lista in cui deve essere presente
	 * @param entityName il nome dell'entit&agrave;
	 */
	protected <E extends Entita> void checkPresentInList(E entita, List<E> lista, String entityName) {
		// Non rilancio l'eccezione
		checkPresentInList(entita, lista, entityName, false, false);
	}
	
	/**
	 * Controlla se l'entita sia presente nella lista fornita.
	 * @param <E> la tipizzazione dell'entita
	 * @param entita     l'entit&agrave; da controllare
	 * @param lista      la lista in cui deve essere presente
	 * @param entityName il nome dell'entit&agrave;
	 * @param throwError se l'errore sia da rilanciare
	 * @param force      se il controllo sia da forzare
	 * @throws ParamValidationException in caso di errore nella validazione
	 */
	protected <E extends Entita> void checkPresentInList(E entita, List<E> lista, String entityName, boolean throwError, boolean force) {
		// Se forzo, o se l'entita' e' presente
		if(force || (entita != null && entita.getUid() != 0)) {
			// Controllo se l'entita' sia presente
			E foundEntity = ComparatorUtils.searchByUidEventuallyNull(lista, entita);
			// TODO: cercare un buon errore
			checkNotNullNorInvalidUid(foundEntity, entityName, throwError);
		}
	}
	
	/**
	 * Imposta in sessione gli errori attualmente presenti nel model per garantirne l'accesso ad una action successiva.
	 */
	protected void setErroriInSessionePerActionSuccessiva() {
		// Se ho errori
		if(!model.getErrori().isEmpty()) {
			// Imposto gli errori in sessione
			sessionHandler.setParametro(BilSessionParameter.ERRORI_AZIONE_PRECEDENTE, new ArrayList<Errore>(model.getErrori()));
		}
	}
	
	/**
	 * Imposta in sessione i messaggi attualmente presenti nel model per garantirne l'accesso ad una action successiva.
	 */
	protected void setMessaggiInSessionePerActionSuccessiva() {
		// Se ho messaggi
		if(!model.getMessaggi().isEmpty()) {
			// Imposto i messaggi in sessione
			sessionHandler.setParametro(BilSessionParameter.MESSAGGI_AZIONE_PRECEDENTE, new ArrayList<Messaggio>(model.getMessaggi()));
		}
	}
	
	/**
	 * Imposta in sessione le informazioni attualmente presenti nel model per garantirne l'accesso ad una action successiva.
	 */
	protected void setInformazioniInSessionePerActionSuccessiva() {
		// Se ho informazioni
		if(!model.getInformazioni().isEmpty()) {
			// Imposto le informazioni in sessione
			sessionHandler.setParametro(BilSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE, new ArrayList<Informazione>(model.getInformazioni()));
		}
	}
	
	/**
	 * Imposta in sessione le informazioni, i messaggi e gli errori attualmente presenti nel model per garantirne l'accesso ad una action successiva.
	 */
	protected void setInformazioniMessaggiErroriInSessionePerActionSuccessiva() {
		// Wrapper per impostare errori, messaggi ed informazioni
		setErroriInSessionePerActionSuccessiva();
		setMessaggiInSessionePerActionSuccessiva();
		setInformazioniInSessionePerActionSuccessiva();
	}
	
	/**
	 * Pulisce gli errori, i messaggi e le informazioni.
	 */
	protected void cleanErroriMessaggiInformazioni() {
		// Wrapper per pulire errori, messaggi e informazioni
		cleanErrori();
		cleanMessaggi();
		cleanInformazioni();
	}
	
	/**
	 * Carica l'informazione di successo per l'operazione all'interno del model.
	 */
	protected void impostaInformazioneSuccesso() {
		// Non imposto informazioni aggiuntive
		impostaInformazioneSuccesso("");
	}
	
	/**
	 * Carica l'informazione di successo per l'operazione all'interno del model.
	 * @param optional informazioni aggiuntive da apporre
	 */
	protected void impostaInformazioneSuccesso(String optional) {
		// Aggiungo l'informazioni di successo
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo" + optional));
	}
	
	/**
	 * Distrugge la famiglia di ancore, mantenendo solo la prima.
	 * @param familyUseCase le fmaiglia da distruggere
	 */
	protected void destroyAnchorFamily(String familyUseCase) {
		// Se non ho la famiglia, esco
		if(familyUseCase == null) {
			return;
		}
		
		final String methodName = "destroyAnchorFamily";
		// Ottengo lo stack dalla sessione
		AnchorStack anchorStack = sessionHandler.getParametro(CommonSessionParameter.ANCHOR_STACK, AnchorStack.class);
		// Ottengo le ancore
		Collection<Anchor> stack = anchorStack.getAnchorStack();
		// Per ogni ancora
		for(Iterator<Anchor> iterator = stack.iterator(); iterator.hasNext();) {
			// Ottengo l'ancora
			Anchor anchor = iterator.next();
			// Se lo use case e' lo stesso
			if(familyUseCase.equalsIgnoreCase(anchor.getUseCase())) {
				// Rimouvo l'ancora
				iterator.remove();
				log.debug(methodName, "destroyed anchor with name: " + anchor.getName());
			}
		}
	}
	
	/**
	 * Pulisce i parametri settati in sessione.
	 */
	protected void pulisciSessione() {
		final String methodName = "pulisciSessione";
		// Prendo l'elenco dei dati da pulire
		Set<BilSessionParameter> setDaPulire = sessionHandler.getParametro(BilSessionParameter.DA_PULIRE);
		
		// Se non ho dati da pulire esco
		if(setDaPulire == null || setDaPulire.isEmpty()) {
			return;
		}
		
		// Per ogni dato da pulire
		for(BilSessionParameter parametro : setDaPulire) {
			// Pulisco il dato
			sessionHandler.setParametro(parametro, null);
			log.debug(methodName, "Pulito parametro " + parametro.name());
		}
		// Pulisco i dati dalla sessione
		setDaPulire.clear();
		sessionHandler.setParametro(BilSessionParameter.DA_PULIRE, setDaPulire);
	}
	
	/**
	 * Controlla se il metodo si sia concluso senza alcun errore. In caso contrario, rilancia un'eccezione per uscire dalla pagina.
	 * @throws GenericFrontEndMessagesException in caso di errori nei servizii
	 */
	protected void checkMetodoConclusoSenzaErrori() {
		// Se ho errori
		if(hasErrori()) {
			StringBuilder erroriRiscontrati = new StringBuilder();
			// Per ogni errore, aggiungo il testo
			for(Errore errore : model.getErrori()) {
				erroriRiscontrati.append(errore.getTesto() + "\n");
			}
			// Lancio l'eccezione
			throw new GenericFrontEndMessagesException(ErroreCore.ERRORE_DI_SISTEMA.getErrore(erroriRiscontrati.toString()).getTesto(),
					GenericFrontEndMessagesException.Level.ERROR);
		}
	}
	
	/**
	 * Aggiunge la chiave in sessione affinch&eacute; l'oggetto sia pulito.
	 * @param parameter il parametro da impostare
	 */
	protected void aggiungiInSessionePerPulizia(BilSessionParameter parameter) {
		final String methodName = "aggiungiInSessionePerPulizia";
		// Prendo i parametri da pulire dalla sessione
		Set<BilSessionParameter> setDaPulire = sessionHandler.getParametro(BilSessionParameter.DA_PULIRE);
		
		if(setDaPulire == null) {
			// Se non ho i dati in sessione, inizializzo
			setDaPulire = EnumSet.noneOf(BilSessionParameter.class);
		}
		
		// Aggiungo il parametro
		setDaPulire.add(parameter);
		log.debug(methodName, "Aggiunto parametro " + parameter.name());
		// Imposto i dati in sessione
		sessionHandler.setParametro(BilSessionParameter.DA_PULIRE, setDaPulire);
	}
	
	/**
	 * Rilancia un'eccezione a partire dalla lista degli errori, compilando il messaggio con tutti i messaggi di errore.
	 * @param errori la lista dei messaggi di errore
	 * @throws GenericFrontEndMessagesException l'eccezione rilanciata
	 */
	protected void throwExceptionFromErrori(Collection<Errore> errori) {
	    throwExceptionFromErrori(ErroreCore.ERRORE_DI_SISTEMA, errori);
	}
	
	/**
	 * Rilancia un'eccezione a partire dalla lista degli errori, compilando il messaggio con tutti i messaggi di errore.
	 * @param template il tipo dell'errore
	 * @param errori la lista dei messaggi di errore
	 * @throws GenericFrontEndMessagesException l'eccezione rilanciata
	 */
	protected void throwExceptionFromErrori(TipoErrore template, Collection<Errore> errori) {
		StringBuilder sb = new StringBuilder();
		// Per ogni errore recupero il testo
		for(Errore errore : errori) {
			sb.append(errore.getTesto())
				.append("\n");
		}
		// Rilancio l'eccezione
		throw new GenericFrontEndMessagesException(template
					.getErrore(sb.toString()).getTesto(),
						GenericFrontEndMessagesException.Level.ERROR);
	}
	
	/**
	 * Rilancia un'eccezione a partire dall'errore, compilando il messaggio.
	 * @param errore messaggio d'errore
	 * @throws GenericFrontEndMessagesException l'eccezione rilanciata
	 */
	protected void throwExceptionFromErrore(Errore errore) {
		// Rilancio l'eccezione
		throw new GenericFrontEndMessagesException(errore.getTesto(),
						GenericFrontEndMessagesException.Level.ERROR);
	}
	
	/**
	 * Ottiene la posizione di start dalla sessione.
	 * @return la posizione di start, se presente; 0 in caso contrario
	 */
	protected Integer ottieniPosizioneStartDaSessione() {
		return ottieniPosizioneStartDaSessione(BilSessionParameter.RIENTRO_POSIZIONE_START);
	}
	
	/**
	 * Ottiene la posizione di start dalla sessione.
	 * @param bilSessionParameter il parametro di sessione
	 * @return la posizione di start, se presente; 0 in caso contrario
	 */
	protected Integer ottieniPosizioneStartDaSessione(BilSessionParameter bilSessionParameter) {
		// Ottengo il displayStart dalla sessione, se presente
		Integer posizioneStart = sessionHandler.getParametro(bilSessionParameter);
		// Se iol dato non e' impostato, inizializzo a zero
		if(posizioneStart == null) {
			posizioneStart = Integer.valueOf(0);
		} else {
			sessionHandler.setParametro(bilSessionParameter, null);
		}
		return posizioneStart;
	}
	
	/**
	 * Rilancia l'eccezione di operazione incompatibile con la fase di bilancio.
	 * @param faseBilancio la fase del bilancio
	 * @throws GenericFrontEndMessagesException sempre
	 */
	protected void throwOperazioneIncompatibileFaseBilancio(FaseBilancio faseBilancio) {
		// Lancia l'errore di operazione incompatibile
		throw new GenericFrontEndMessagesException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Bilancio", faseBilancio.name()).getTesto(),
			GenericFrontEndMessagesException.Level.ERROR);
	}
	
	/**
	 * Effettua il controllo sulle azioni decentrate.
	 * <br/>
	 * L'algoritmo di controllo delle azioni decentrate &eacute; il seguente:
	 * <ol>
	 *     <li>ottengo le azioni consentite all'utente dalla sessione</li>
	 *     <li>controllo se l'azione decentrata appartenga alle azioni consentite</li>
	 *     <li>
	 *         <ul>
	 *             <li>nel caso l'azione non appartenga alle azioni consentite, esco</li>
	 *             <li>nel caso l'azione appartenga alle azioni consentite, proseguo</li>
	 *         </ul>
	 *     </li>
	 *     <li>ottengo la lista delle SAC corrispondenti all'azione consentita</li>
	 *     <li>controllo che la SAC fornita in input corrisponda a una di quelle della lista di cui il punto 4</li>
	 * </ol>
	 * 
	 * <strong>NOTA</strong>: il metodo suppone che un utente che possa effettuare un'azione come decentrato NON possa effettuare la stessa azione come non-decentrato.
	 * 
	 * @param strutturaAmministrativoContabile la SAC su cui ho intenzione di operare
	 * @param azioneDecentrata                 l'azione da verificare
	 */
	protected void controlloDecentrato(StrutturaAmministrativoContabile strutturaAmministrativoContabile, String azioneDecentrata) {
		/*
		 * L'algoritmo di controllo delle azioni decentrate e' il seguente:
		 * 
		 * 1. ottengo le azioni consentite all'utente dalla sessione
		 * 2. controllo se l'azione decentrata appartenga alle azioni consentite
		 * 3a. nel caso l'azione non appartenga alle azioni consentite, esco
		 * 3b. nel caso l'azione appartenga alle azioni consentite, proseguo
		 *   4. ottengo la lista delle SAC corrispondenti all'azione consentita
		 *   5. controllo che la SAC fornita in input corrisponda a una di quelle della lista di cui il punto 4
		 * 
		 * NOTA: il metodo suppone che un utente che possa effettuare un'azione come decentrato NON possa
		 *       effettuare la stessa azione come non-decentrato.
		 */
		AzioneConsentita azioneConsentita = ComparatorUtils.findAzioneConsentitaByNomeAzione(sessionHandler.getAzioniConsentite(), azioneDecentrata);
		// Se ho un'azione consentita
		if(azioneConsentita != null) {
			// Recupero le SAC
			List<StrutturaAmministrativoContabile> listaSAC = ottieniListaStruttureAmministrativoContabiliDaSessione();
			// Se ho le SAC
			if(strutturaAmministrativoContabile != null && listaSAC != null && !listaSAC.isEmpty()) {
				// Decentrato: controllo la coerenza delle SAC
				StrutturaAmministrativoContabile sacNellaLista = ComparatorUtils.searchWithChildrenPossiblyNull(listaSAC, strutturaAmministrativoContabile);
				// Se la sac non e' presente, imposto l'errore
				checkCondition(sacNellaLista != null, ErroreFin.STRUTTURA_CONTABILE_NON_COMPATIBILE.getErrore("Utente"));
			}
		}
	}
	
	/**
	 * Effettua il controllo di uguaglianza tra le classi di soggetto del movimento di gestione e del soggetto.
	 * @param movimentoGestione il movimento da cui ottenere la classe
	 * @param soggetto          il soggetto di cui controllare le classi
	 * @param campo1            il primo campo
	 * @param campo2            il secondo campo
	 * @param campo3            il terzo campo
	 * @param campo4            il quarto campo
	 */
	protected void checkClasseSoggetto(MovimentoGestione movimentoGestione, Soggetto soggetto, String campo1, String campo2, String campo3, String campo4) {
		// Controlli sui campo soggetto
		checkClasseSoggetto(movimentoGestione, soggetto, campo1, campo2, campo3, campo4, true);
	}
	
	/**
	 * Effettua il controllo di uguaglianza tra le classi di soggetto del movimento di gestione e del soggetto.
	 * 
	 * @param movimentoGestione il movimento da cui ottenere la classe
	 * @param soggetto          il soggetto di cui controllare le classi
	 * @param campo1            il primo campo
	 * @param campo2            il secondo campo
	 * @param campo3            il terzo campo
	 * @param campo4            il quarto campo
	 * @param isErrore          se sia necessario aggiungere un errore o un messaggio
	 */
	protected void checkClasseSoggetto(MovimentoGestione movimentoGestione, Soggetto soggetto, String campo1, String campo2, String campo3, String campo4, boolean isErrore) {
		final String methodName = "checkClasseSoggetto";
		// Ottiene la classe soggetto
		ClasseSoggetto classeSoggettoMovimentoGestione = movimentoGestione.getClasseSoggetto();
		if(classeSoggettoMovimentoGestione == null) {
			// Movimento senza classe, esco dal controllo
			log.debug(methodName, "Classe soggetto non presente per il movimento di gestione");
			return;
		}
		// Ottengo le classi del soggetto
		List<ClassificazioneSoggetto> listaClasseSoggettoSoggetto = soggetto.getElencoClass();
		if(listaClasseSoggettoSoggetto == null) {
			// Se il soggetto non ha classi non posso continuare
			log.debug(methodName, "Classi soggetto non presenti sul soggetto");
			addErroreOrMessaggio(isErrore, ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA, campo1, campo2, campo3, campo4);
			// Esco
			return;
		}
		// Per ogni classificazione
		for(ClassificazioneSoggetto cs : listaClasseSoggettoSoggetto) {
			// Controllo che le classificazioni siano coerenti
			if(cs.getSoggettoClasseCode().equalsIgnoreCase(classeSoggettoMovimentoGestione.getCodice())) {
				// Il codice e' coerente, esco
				log.debug(methodName, "ho trovato un codice classe corrispondente: " + cs.getSoggettoClasseCode());
				return;
			}
		}
		// Aggiunge l'errore o il messaggio
		addErroreOrMessaggio(isErrore, ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA, campo1, campo2, campo3, campo4);
	}
	/**
	 * Determina se &egrave; necessario aggiungere nel model e nella action un errore o un messaggio, in base ai parametri di input.
	 * @param isErrore 		<code>true</code> se &egrave; necessario impostare il tipo errore come errore, <code>false</code> altrimenti 
	 * @param tipoErrore 	il tipoErrore da aggiungere
	 * @param params 		i parametri coi quali comporre la stringa da mostrare all'utente
	 */
	protected void addErroreOrMessaggio(boolean isErrore, TipoErrore tipoErrore, Object... params) {
		if(isErrore) {
			// Richiesto di aggiungere l'errore
			addErrore(tipoErrore.getErrore(params));
		} else {
			// Richiesto di aggiungere il messaggio
			addMessaggio(tipoErrore.getErrore(params));
		}
	}
	
	/**
	 * Fornisce un valore di default alla lista fornita in input nel caso in cui essa sia <code>null</code>.
	 * @param <T> la tipizzazione della lista
	 * 
	 * @param list la lista da defaultare
	 * @return la lista originale, se non <code>null</code>; una lista vuota in caso contrario
	 */
	protected <T> List<T> defaultingList(List<T> list) {
		// Se la lista non e' valorizzata la inizializza
		return list == null ? new ArrayList<T>() : list;
	}
	
	/**
	 * Costrusice una stringa per segnalare l'errore nell'invocazione del servizio.
	 * @param req il request del servizio che ha fornito l'errore
	 * @param res la response tramite cui loggare gli errori
	 * @return la stringa di errore
	 */
	public String createErrorInServiceInvocationString(ServiceRequest req, ServiceResponse res) {
		if(req == null) {
			// La request non e' valorizzata
			return "NULL request";
		}
		// Creo la stringa di errore
		StringBuilder sb = new StringBuilder()
			.append("Errore nell'invocazione del servizio ")
			.append(req.getClass().getSimpleName());
		if(res != null && res.getErrori() != null) {
			// Se ho errori
			for(Errore errore : res.getErrori()) {
				// Aggiungo errore per errore
				sb.append(" - ")
					.append(errore.getTesto());
			}
		}
		// Restituisco la stringa
		return sb.toString();
	}
	
	/**
	 * Wrappa la request per l'invocazione asincrona
	 * @param <REQ> la tipizzazione della request
	 * @param req la request da wrappare
	 * @return il wrapper della request per l'invocazione asincrona
	 */
	protected <REQ extends ServiceRequest> AsyncServiceRequestWrapper<REQ> wrapRequestToAsync(REQ req) {
		// Wrappa la request con l'azione richiesta fornita dal cruscotto 
		return wrapRequestToAsync(req, model.getAzioneRichiesta());
	}
	
	/**
	 * Wrappa la request per l'invocazione asincrona
	 * @param <REQ> la tipizzazione della request
	 * @param req             la request da wrappare
	 * @param azioneRichiesta l'azione richiesta per cui creare la request asincrona
	 * @return il wrapper della request per l'invocazione asincrona
	 */
	protected <REQ extends ServiceRequest> AsyncServiceRequestWrapper<REQ> wrapRequestToAsync(REQ req, AzioneRichiesta azioneRichiesta) {
		// Creo il wrapper
		AsyncServiceRequestWrapper<REQ> result = new AsyncServiceRequestWrapper<REQ>();
		
		// Mappatura dei campi
		result.setAzioneRichiesta(azioneRichiesta);
		result.setDataOra(req.getDataOra());
		result.setEnte(model.getEnte());
		result.setRequest(req);
		result.setRichiedente(model.getRichiedente());
		result.setAccount(model.getAccount());
		
		// Resituisco il wrapper
		return result;
	}
	
	/**
	 * Impostazione della lista delle modalita di pagamento.
	 * @param lista la lista delle modalita
	 * @return la lista di tutte le modalita, comprese le cessioni
	 */
	protected List<ModalitaPagamentoSoggetto> impostaListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> lista) {
		// Possibile che sia modificato
		return lista;
	}
	
	/**
	 * Ottiene la lista delle Strutture Amministrativo Contabili presenti in sessione.
	 * @return la lista delle strutture
	 */
	protected List<StrutturaAmministrativoContabile> ottieniListaStruttureAmministrativoContabiliDaSessione() {
		List<StrutturaAmministrativoContabile> res = sessionHandler.getAccount().getStruttureAmministrativeContabili();
		return res != null ? res : new ArrayList<StrutturaAmministrativoContabile>();
	}

	/**
	 * Controlla l'univocit&agrave; dell'atto amministrativo.
	 * @param listaAttoAmministrativo                 la lista degli atti
	 * @param strutturaAmministrativoContabileRicerca la struttura impostata nella ricerca
	 * @param toThrow                                 se lanciare l'eccezione
	 */
	protected void checkUnicoAttoAmministrativo(List<AttoAmministrativo> listaAttoAmministrativo, StrutturaAmministrativoContabile strutturaAmministrativoContabileRicerca, boolean toThrow) {
		if(listaAttoAmministrativo.size() == 1) {
			// Univoco: sono a posto
			return;
		}
		
		// Controllo di avere dei dati
		checkCondition(!listaAttoAmministrativo.isEmpty(), ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore(), toThrow);
		// Controllo di avere esattamente un dato
		checkCondition(listaAttoAmministrativo.size() == 1, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Provvedimento"), toThrow);
	}
	
	@Override
	protected void logServiceRequest(ServiceRequest req) {
		// IGNORO IL LOG
	}
	
	@Override
	protected void logServiceResponse(ServiceResponse res) {
		// IGNORO IL LOG
	}
	
	/**
	 * Controlla che la condizione di business sia verificata: in caso contrario lancia un'eccezione
	 * @param condition la condizione da verificare
	 * @param errore l'errore da lanciare nel caso in cui la condizione non sia verificata
	 * @throws GenericFrontEndMessagesException se la condizione non &eacute; verificata
	 */
	protected void checkBusinessCondition(boolean condition, Errore errore) {
		// Controllo la condizione
		if(!condition) {
			// Lancio l'eccezione
			throw new GenericFrontEndMessagesException(errore.getTesto());
		}
	}
	
	/**
	 * Controlla la risposta del servizio. Se vi sono errori, lancia l'eccezione
	 * @param methodName il metodo chiamante
	 * @param req la request
	 * @param res la response
	 * @throws WebServiceInvocationFailureException in caso di errori
	 */
	protected void checkServiceResponseException(String methodName, ServiceRequest req, ServiceResponse res) throws WebServiceInvocationFailureException {
		// Controllo gli errori
		if(res.hasErrori()) {
			// Si sono verificati degli errori: esco.
			// Creo il messaggio di errore
			String msg = createErrorInServiceInvocationString(req, res);
			// Loggo
			log.info(methodName, msg);
			// Aggiungo gli errori
			addErrori(res);
			// Lancio l'eccezione
			throw new WebServiceInvocationFailureException(msg);
		}
	}

	/**
	 * Ricerca l'azione
	 * @param nome il nome dell'azione
	 * @return l'azione
	 */
	protected Azione findAzione(String nome) {
		FindAzione req = model.creaRequestFindAzione(nome);
		FindAzioneResponse response = coreService.findAzione(req);
		checkErroriResponse(response, "findAzione");
	
		return response.getAzione() != null ? response.getAzione() : null;
	}
	
	/**
	 * Ottiene la lista conto tesoreria dalla lista codifica fin
	 * @param listaCodificaFin la lista codifica fin
	 * @return la lista del conto tesoreria
	 */
	protected List<ContoTesoreria> getListaContoTesoreriaByCodificaFin(List<? extends CodificaFin> listaCodificaFin){
		List<ContoTesoreria> lista = new ArrayList<ContoTesoreria>();
		if(listaCodificaFin == null) {
			return null;
		}
		
		for (CodificaFin codificaFin : listaCodificaFin) {
			ContoTesoreria codifica = new ContoTesoreria();
			codifica.setCodice(codificaFin.getCodice());
			codifica.setDescrizione(codificaFin.getDescrizione());
			codifica.setUid(codifica.getUid());
			lista.add(codifica);
		}
		return lista;
	}
	
}
