/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.conciliazione;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
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
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.ConciliazioneService;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ConciliazionePerBeneficiario;
import it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione.GestioneConciliazionePerBeneficiarioModel;
import it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.conciliazione.ElementoConciliazionePerBeneficiario;

/**
 * Classe di action per la gestione della conciliazione per beneficiario.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/11/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneConciliazionePerBeneficiarioAction extends GenericBilancioAction<GestioneConciliazionePerBeneficiarioModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2240664112770360710L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	@Autowired private transient ConciliazioneService conciliazioneService;
	@Autowired private transient SoggettoService soggettoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento delle liste, rigorosamente dalla sessione
		caricaListaClasseDiConciliazione();
		// TODO
		caricaListaClasseSoggettoDaSessione();
	}

	/**
	 * Caricamento della lista della classe soggetto a partire dalla sessione
	 */
	private void caricaListaClasseSoggettoDaSessione() {
		// TODO Auto-generated method stub
	}

	/**
	 * Caricamento della lista delle classi di conciliazione.
	 */
	private void caricaListaClasseDiConciliazione() {
		model.setListaClasseDiConciliazione(Arrays.asList(ClasseDiConciliazione.values()));
	}

	@SuppressWarnings("rawtypes")
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		model.setCapitoloRicerca(new Capitolo());
		model.getCapitoloRicerca().setAnnoCapitolo(model.getAnnoEsercizioInt());
		// Pulisco la ricerca per soggetto
		sessionHandler.setParametro(BilSessionParameter.SOGGETTO, null);
		// TODO
		caricaListaClasseSoggetto();
		return SUCCESS;
	}

	/**
	 * Caricamento della lista della classe soggetto.
	 */
	private void caricaListaClasseSoggetto() {
		// TODO Auto-generated method stub
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
	 * Ottiene i dati del soggetto fornito a partire dalla sessione se correttamente popolata o dal servizio.
	 * 
	 * @param soggetto il soggetto da cercare
	 * @return il soggetto ottenuto
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del serviziob
	 */
	private Soggetto ottieniSoggettoDaSessioneOServizio(Soggetto soggetto) throws WebServiceInvocationFailureException {
		final String methodName = "ottieniSoggettoDaSessioneOServizio";
		Soggetto soggettoSession = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		if(soggettoSession != null && soggetto.getCodiceSoggetto().equals(soggettoSession.getCodiceSoggetto())) {
			return soggettoSession;
		}
	
		log.debug(methodName, "Ricerca puntuale del soggetto");
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(soggetto);
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
			Errore errore = ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Soggetto " + soggetto.getCodiceSoggetto() + " non trovato");
			addErrore(errore);
			throw new WebServiceInvocationFailureException(errore.getTesto());
		}
		soggettoSession = response.getSoggetto();
		sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggettoSession);
		return soggettoSession;
	}
	
	/**
	 * Ricerca sintetica della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaSintetica() {
		final String methodName = "ricercaSintetica";
		RicercaSinteticaConciliazionePerBeneficiario request = model.creaRequestRicercaSinteticaConciliazionePerBeneficiario();
		logServiceRequest(request);
		RicercaSinteticaConciliazionePerBeneficiarioResponse response = conciliazioneService.ricercaSinteticaConciliazionePerBeneficiario(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaSinteticaConciliazionePerBeneficiario.class, response));
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		log.debug(methodName, "Trovati " + response.getTotaleElementi() + " risultati");
		
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CONCILIAZIONI_PER_BENEFICIARIO, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CONCILIAZIONI_PER_BENEFICIARIO, response.getConciliazioni());
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link #ricercaSintetica()}.
	 */
	public void validateRicercaSintetica() {
		checkSoggettoRicerca();
		checkCondition(model.getCapitoloRicerca().getAnnoCapitolo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno"));
	}
	
	/**
	 * Controlli di validit&agrave; del soggetto per la ricerca.
	 */
	private void checkSoggettoRicerca() {
		Soggetto soggetto = checkSoggetto(model.getSoggettoRicerca());
		model.setSoggettoRicerca(soggetto);
	}

	/**
	 * Eliminazione della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String elimina() {
		final String methodName = "elimina";
		EliminaConciliazionePerBeneficiario request = model.creaRequestEliminaConciliazionePerBeneficiario();
		logServiceRequest(request);
		EliminaConciliazionePerBeneficiarioResponse response = conciliazioneService.eliminaConciliazionePerBeneficiario(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(EliminaConciliazionePerBeneficiario.class, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Eliminata conciliazione con uid " + model.getConciliazionePerBeneficiario1().getUid());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #elimina()}.
	 */
	public void validateElimina() {
		checkNotNullNorInvalidUid(model.getConciliazionePerBeneficiario1(), "Conciliazione");
	}

	/**
	 * Ricerca di dettaglio della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaDettaglio() {
		final String methodName = "ricercaDettaglio";
		RicercaDettaglioConciliazionePerBeneficiario request = model.creaRequestRicercaDettaglioConciliazionePerBeneficiario();
		logServiceRequest(request);
		RicercaDettaglioConciliazionePerBeneficiarioResponse response = conciliazioneService.ricercaDettaglioConciliazionePerBeneficiario(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioConciliazionePerBeneficiario.class, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Ricercata conciliazione con uid " + model.getConciliazionePerBeneficiario1().getUid());
		model.setElementoConciliazionePerBeneficiario(new ElementoConciliazionePerBeneficiario(response.getConciliazionePerBeneficiario(), model.isGestioneUEB()));
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ricercaDettaglio()}.
	 */
	public void validateRicercaDettaglio() {
		checkNotNullNorInvalidUid(model.getConciliazionePerBeneficiario1(), "Conciliazione");
	}

	/**
	 * Inserimento della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisci() {
		final String methodName = "inserisci";
		
		InserisceConciliazioniPerBeneficiario request = model.creaRequestInserisceConciliazioniPerBeneficiario();
		logServiceRequest(request);
		InserisceConciliazioniPerBeneficiarioResponse response = conciliazioneService.inserisceConciliazioniPerBeneficiario(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(InserisceConciliazioniPerBeneficiario.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Inserite " + response.getConciliazioniPerBeneficiario().size() + " conciliazioni per capitolo");
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link #inserisci()}.
	 */
	public void validateInserisci() {
		checkNotNullNorInvalidUid(model.getCapitolo(), "Capitolo");
		// Almeno uno dei conti e' obbligatorio
		checkCondition(hasConto(model.getConciliazionePerBeneficiario1()) || hasConto(model.getConciliazionePerBeneficiario2()),
				ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("almeno un conto e' obbligatorio"));
		
		checkSoggetto();
	}
	
	/**
	 * Controlli di validit&agrave; del soggetto.
	 */
	private void checkSoggetto() {
		Soggetto soggetto = checkSoggetto(model.getSoggetto());
		model.setSoggetto(soggetto);
	}
	
	/**
	 * Controlli di validit&agrave; del soggetto.
	 * @param soggetto il soggetto da controllare
	 * @return il soggetot valido
	 */
	private Soggetto checkSoggetto(Soggetto soggetto) {
		final String methodName = "checkSoggetto";
		checkNotNull(soggetto, "Soggetto", true);
		checkCondition(StringUtils.isNotBlank(soggetto.getCodiceSoggetto()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"), true);
		// Ho il soggetto: ne carico i dati
		try {
			return ottieniSoggettoDaSessioneOServizio(soggetto);
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errori nel caricamento del soggetto: " + wsife.getMessage());
			return null;
		}
	}

	/**
	 * Controlla se la conciliazione ha il conto valorizzato.
	 * 
	 * @param conciliazionePerBeneficiario la conciliazione da controllare
	 * @return <code>true</code> se la conciliazione ha il conto; <code>false</code> altrimenti
	 */
	private boolean hasConto(ConciliazionePerBeneficiario conciliazionePerBeneficiario) {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getConto() != null && conciliazionePerBeneficiario.getConto().getUid() != 0;
	}

	/**
	 * Aggiornamento della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		AggiornaConciliazionePerBeneficiario request = model.creaRequestAggiornaConciliazionePerBeneficiario();
		logServiceRequest(request);
		AggiornaConciliazionePerBeneficiarioResponse response = conciliazioneService.aggiornaConciliazionePerBeneficiario(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaConciliazionePerBeneficiario.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Aggiornata conciliazione con uid " + response.getConciliazionePerBeneficiario().getUid());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornamento()}.
	 */
	public void validateAggiornamento() {
		checkNotNullNorInvalidUid(model.getConciliazionePerBeneficiario1(), "Conciliazione", true);
		checkNotNullNorInvalidUid(model.getSoggetto(), "Soggetto");
		checkNotNullNorInvalidUid(model.getCapitolo(), "Capitolo");
		
		// Almeno uno dei conti e' obbligatorio
		checkCondition(hasConto(model.getConciliazionePerBeneficiario1()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto"));
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
}
