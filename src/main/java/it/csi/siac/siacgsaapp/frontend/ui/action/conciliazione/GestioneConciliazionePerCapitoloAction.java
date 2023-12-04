/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.conciliazione;

import java.util.Arrays;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.ConciliazioneService;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ConciliazionePerCapitolo;
import it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione.GestioneConciliazionePerCapitoloModel;
import it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.conciliazione.ElementoConciliazionePerCapitolo;

/**
 * Classe di action per la gestione della conciliazione per capitolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneConciliazionePerCapitoloAction extends GenericBilancioAction<GestioneConciliazionePerCapitoloModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3097788312516191777L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	@Autowired private transient ConciliazioneService conciliazioneService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento delle liste, rigorosamente dalla sessione
		caricaListaClasseDiConciliazione();
	}
	
	/**
	 * Caricamento della lista delle classi di conciliazione.
	 */
	private void caricaListaClasseDiConciliazione() {
		model.setListaClasseDiConciliazione(Arrays.asList(ClasseDiConciliazione.values()));
	}

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile();
		return SUCCESS;
	}

	@Override
	protected void checkCasoDUsoApplicabile() {
		final String methodName = "checkCasoDUsoApplicabile";
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.warn(methodName, "Errori nel caricamento del bilancio");
			addErrori(response);
			throw new GenericFrontEndMessagesException(createErrorInServiceInvocationString(RicercaDettaglioBilancio.class, response));
		}
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
				FaseBilancio.PREVISIONE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
		model.setFaseBilancio(faseBilancio);
	}

	/**
	 * Ricerca del capitolo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaCapitolo() {
		final String methodName = "ricercaCapitolo";
		log.debug(methodName, "Ricerca sintetica del capitolo di gestione: tipo " + model.getTipoCapitoloRicerca());
		
		Capitolo<?, ?> capitolo = null;
		try {
			if(model.isSpesa(model.getTipoCapitoloRicerca())) {
				capitolo = ricercaCapitoloUscita(model.getCapitoloRicerca());
			} else if (model.isEntrata(model.getTipoCapitoloRicerca())) {
				capitolo =  ricercaCapitoloEntrata(model.getCapitoloRicerca());
			}
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nel caricamento dei dati: " + wsife.getMessage());
			return INPUT;
		}
		if(capitolo == null) {
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("tipo di capitolo " + model.getTipoCapitoloRicerca() + " non supportato"));
			return INPUT;
		}
		model.setCapitoloRicerca(capitolo);
		return SUCCESS;
	}
	
	/**
	 * Ricerca del capitolo di uscita gestione.
	 * @param capitolo il capitolo da cercare
	 * @return il capitolo di uscita trovato
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private CapitoloUscitaGestione ricercaCapitoloUscita(Capitolo<?, ?> capitolo) throws WebServiceInvocationFailureException {
		RicercaPuntualeCapitoloUscitaGestione request = model.creaRequestRicercaPuntualeCapitoloUscitaGestione(capitolo);
		logServiceRequest(request);
		RicercaPuntualeCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaPuntualeCapitoloUscitaGestione(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaPuntualeCapitoloUscitaGestione.class, response));
		}
		return response.getCapitoloUscitaGestione();
	}

	/**
	 * Ricerca sintetica della conciliazione per capitolo di entrata gestione.
	 * @param capitolo il capitolo da cercare
	 * @return una stringa corrispondente al risultato dell'invocazione
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private CapitoloEntrataGestione ricercaCapitoloEntrata(Capitolo<?, ?> capitolo) throws WebServiceInvocationFailureException {
		RicercaPuntualeCapitoloEntrataGestione request = model.creaRequestRicercaPuntualeCapitoloEntrataGestione(capitolo);
		logServiceRequest(request);
		RicercaPuntualeCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaPuntualeCapitoloEntrataGestione(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaPuntualeCapitoloEntrataGestione.class, response));
		}
		return response.getCapitoloEntrataGestione();
	}

	/**
	 * Validazione per il metodo {@link #ricercaCapitolo()}.
	 */
	public void validateRicercaCapitolo() {
		checkNotNullNorEmpty(model.getTipoCapitoloRicerca(), "Tipo");
		checkNotNull(model.getCapitoloRicerca(), "Capitolo", true);
		
		checkNotNull(model.getCapitoloRicerca().getNumeroCapitolo(), "Numero");
		checkNotNull(model.getCapitoloRicerca().getNumeroArticolo(), "Articolo");
		checkNotNull(model.getCapitoloRicerca().getNumeroUEB(), "UEB");
	}
	
	/**
	 * Ricerca sintetica della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaSintetica() {
		final String methodName = "ricercaSintetica";
		RicercaSinteticaConciliazionePerCapitolo request = model.creaRequestRicercaSinteticaConciliazionePerCapitolo();
		logServiceRequest(request);
		RicercaSinteticaConciliazionePerCapitoloResponse response = conciliazioneService.ricercaSinteticaConciliazionePerCapitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaSinteticaConciliazionePerCapitolo.class, response));
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		log.debug(methodName, "Trovati " + response.getTotaleElementi() + " risultati");
		
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CONCILIAZIONI_PER_CAPITOLO, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CONCILIAZIONI_PER_CAPITOLO, response.getConciliazioni());
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link #ricercaSintetica()}.
	 */
	public void validateRicercaSintetica() {
		checkNotNullNorInvalidUid(model.getCapitoloRicerca(), "Capitolo");
	}

	/**
	 * Eliminazione della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String elimina() {
		final String methodName = "elimina";
		EliminaConciliazionePerCapitolo request = model.creaRequestEliminaConciliazionePerCapitolo();
		logServiceRequest(request);
		EliminaConciliazionePerCapitoloResponse response = conciliazioneService.eliminaConciliazionePerCapitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(EliminaConciliazionePerCapitolo.class, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Eliminata conciliazione con uid " + model.getConciliazionePerCapitolo1().getUid());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #elimina()}.
	 */
	public void validateElimina() {
		checkNotNullNorInvalidUid(model.getConciliazionePerCapitolo1(), "Conciliazione");
	}

	/**
	 * Ricerca di dettaglio della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaDettaglio() {
		final String methodName = "ricercaDettaglio";
		RicercaDettaglioConciliazionePerCapitolo request = model.creaRequestRicercaDettaglioConciliazionePerCapitolo();
		logServiceRequest(request);
		RicercaDettaglioConciliazionePerCapitoloResponse response = conciliazioneService.ricercaDettaglioConciliazionePerCapitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioConciliazionePerCapitolo.class, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Ricercata conciliazione con uid " + model.getConciliazionePerCapitolo1().getUid());
		model.setElementoConciliazionePerCapitolo(new ElementoConciliazionePerCapitolo(response.getConciliazionePerCapitolo()));
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ricercaDettaglio()}.
	 */
	public void validateRicercaDettaglio() {
		checkNotNullNorInvalidUid(model.getConciliazionePerCapitolo1(), "Conciliazione");
	}

	/**
	 * Ricerca del capitolo per l'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaCapitoloInserimento() {
		final String methodName = "ricercaCapitoloInserimento";
		log.debug(methodName, "Ricerca sintetica del capitolo di gestione: tipo " + model.getTipoCapitolo());
		
		Capitolo<?, ?> capitolo = null;
		try {
			if(model.isSpesa(model.getTipoCapitolo())) {
				capitolo = ricercaCapitoloUscita(model.getCapitolo());
			} else if (model.isEntrata(model.getTipoCapitolo())) {
				capitolo =  ricercaCapitoloEntrata(model.getCapitolo());
			}
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nel caricamento dei dati: " + wsife.getMessage());
			return INPUT;
		}
		if(capitolo == null) {
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("tipo di capitolo " + model.getTipoCapitolo() + " non supportato"));
			return INPUT;
		}
		model.setCapitolo(capitolo);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ricercaCapitoloInserimento()}.
	 */
	public void validateRicercaCapitoloInserimento() {
		checkNotNullNorEmpty(model.getTipoCapitolo(), "Tipo");
		checkNotNull(model.getCapitolo(), "Capitolo", true);
		
		checkNotNull(model.getCapitolo().getNumeroCapitolo(), "Numero");
		checkNotNull(model.getCapitolo().getNumeroArticolo(), "Articolo");
		checkNotNull(model.getCapitolo().getNumeroUEB(), "UEB");
	}

	/**
	 * Inserimento della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisci() {
		final String methodName = "inserisci";
		
		InserisceConciliazioniPerCapitolo request = model.creaRequestInserisceConciliazioniPerCapitolo();
		logServiceRequest(request);
		InserisceConciliazioniPerCapitoloResponse response = conciliazioneService.inserisceConciliazioniPerCapitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(InserisceConciliazioniPerCapitolo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Inserite " + response.getConciliazioniPerCapitolo().size() + " conciliazioni per capitolo");
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link #inserisci()}.
	 */
	public void validateInserisci() {
		checkNotNullNorInvalidUid(model.getCapitolo(), "Capitolo");
		
		// Almeno uno dei conti e' obbligatorio
		checkCondition(hasConto(model.getConciliazionePerCapitolo1()) || hasConto(model.getConciliazionePerCapitolo2()),
				ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("almeno un conto e' obbligatorio"));
	}
	
	/**
	 * Controlla se la conciliazione ha il conto valorizzato.
	 * 
	 * @param conciliazionePerCapitolo la conciliazione da controllare
	 * @return <code>true</code> se la conciliazione ha il conto; <code>false</code> altrimenti
	 */
	private boolean hasConto(ConciliazionePerCapitolo conciliazionePerCapitolo) {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getConto() != null && conciliazionePerCapitolo.getConto().getUid() != 0;
	}

	/**
	 * Aggiornamento della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		AggiornaConciliazionePerCapitolo request = model.creaRequestAggiornaConciliazionePerCapitolo();
		logServiceRequest(request);
		AggiornaConciliazionePerCapitoloResponse response = conciliazioneService.aggiornaConciliazionePerCapitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaConciliazionePerCapitolo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Aggiornata conciliazione con uid " + response.getConciliazionePerCapitolo().getUid());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornamento()}.
	 */
	public void validateAggiornamento() {
		checkNotNullNorInvalidUid(model.getConciliazionePerCapitolo1(), "Conciliazione", true);
		checkNotNullNorInvalidUid(model.getCapitolo(), "Capitolo");
		
		// Almeno uno dei conti e' obbligatorio
		checkCondition(hasConto(model.getConciliazionePerCapitolo1()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto"));
	}
}
