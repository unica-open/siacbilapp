/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscprev;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import xyz.timedrain.arianna.plugin.BreadCrumbTrail;
import xyz.timedrain.arianna.plugin.Crumb;
/*
import org.softwareforge.struts2.breadcrumb.BreadCrumb;_
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;
import org.softwareforge.struts2.breadcrumb.Crumb;
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.ComponenteTotalePrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.InserisciCapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiPropostaNumeroCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiPropostaNumeroCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TotaliAnnoDiEsercizio;
import it.csi.siac.siaccommonapp.handler.session.CommonSessionParameter;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di Action per la gestione dell'inserimento del Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.1.0 30/07/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciCapitoloUscitaPrevisioneAction extends CapitoloUscitaAction<InserisciCapitoloUscitaPrevisioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2871930616441753266L;
	
	@Autowired private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE);
		
		// Carico eventualmente il modello dalla sessione
		caricaModelDaSessioneSePresente();
		inizializzaDatiVariazione();
		
		log.debugEnd(methodName, "");
	}
	
	private void inizializzaDatiVariazione() {
		Boolean daVariazione = sessionHandler.getParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE, null);
		model.setDaVariazione(daVariazione);
	}

	/**
	 * Carica il model da quello presente in sessione.
	 */
	private void caricaModelDaSessioneSePresente() {
		final String methodName = "caricaModelDaSessioneSePresente";
		
		InserisciCapitoloUscitaPrevisioneModel modelInSessione = sessionHandler.getParametro(BilSessionParameter.MODEL_INSERIMENTO_USCITA_PREVISIONE);
		if(modelInSessione != null) {
			log.debug(methodName, "Model presente in sessione. Copia dei dati");
			
			// Sostituire il model attuale con quello ottenuto dalla sessione
			model = modelInSessione;
			// Pulire la sessione
			log.debug(methodName, "Pulisco la sessione");
			sessionHandler.setParametro(BilSessionParameter.MODEL_INSERIMENTO_USCITA_PREVISIONE, null);
			// Caricare le liste derivate
			log.debug(methodName, "Caricamento eventuali liste derivate");
			caricaListaCodificheAggiornamento();
		}
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		impostaValoriDefault();
		return SUCCESS;
	}
	
	/**
	 * Impostazione dei valori di default.
	 */
	private void impostaValoriDefault() {
		final String methodName = "impostaValoriDefault";
		log.debug(methodName, "Impostazione dei valori di default per capitolo da copiare e per capitolo da inserire");
		
		// Default capitolo da copiare
		CapitoloUscitaPrevisione cupCopiare = model.getCapitoloDaCopiare();
		if(cupCopiare == null) {
			cupCopiare = new CapitoloUscitaPrevisione();
			model.setCapitoloDaCopiare(cupCopiare);
		}
		cupCopiare.setNumeroArticolo(0);
		cupCopiare.setNumeroUEB(1);
		log.debug(methodName, "Default per il capitolo da copiare - Articolo " + cupCopiare.getNumeroArticolo() + " UEB " + cupCopiare.getNumeroUEB());
		
		// Default capitolo da inserire
		boolean daCduChiamante = daCduChiamante("OP-GESC001-insVar", "OP-GESC002-aggVar");
		CapitoloUscitaPrevisione cup = model.getCapitoloUscitaPrevisione();
		if(cup == null) {
			cup = new CapitoloUscitaPrevisione();
			model.setCapitoloUscitaPrevisione(cup);
		}
		CategoriaCapitolo categoriaCapitoloStandard = ComparatorUtils.findByCodice(model.getListaCategoriaCapitolo(),
				BilConstants.CODICE_CATEGORIA_CAPITOLO_STANDARD.getConstant());
		
		cup.setCategoriaCapitolo(categoriaCapitoloStandard);
		cup.setFlagImpegnabile(Boolean.TRUE);
		//task-55
		cup.setFlagNonInserireAllegatoA1(Boolean.FALSE);
		if(!daCduChiamante || !model.isGestioneUEB()) {
			cup.setNumeroUEB(1);
			log.debug(methodName, "Default per il capitolo da inserire - UEB " + cup.getNumeroUEB());
		}
		
		//task-86
		if(abilitaNumerazioneAutomaticaCapitolo()) {
			LeggiPropostaNumeroCapitolo req = model.leggiPropostaNumeroCapitolo();
			logServiceRequest(req);
			LeggiPropostaNumeroCapitoloResponse response = capitoloService.leggiPropostaNumeroCapitoloService(req);
			logServiceResponse(response);
			if(!response.hasErrori()) {
				model.getCapitoloUscitaPrevisione().setNumeroCapitolo(response.getNumeroPropostoCapitolo());
			}else {
				log.debug(methodName, "Errore nella risposta del servizio");
				addErrori(methodName, response);
				log.debug(methodName, "Model: " + model);
			}
		}
				
		// CR-2559
		impostaSiopeSeUnico();
		// SIAC-4724
		model.setTipoCapitoloCopia(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE);
	}
	
	/**
	 * CR-2559
	 * Se il SIOPE &eacute; univoco per l'ente, preselezionarlo.
	 */
	private void impostaSiopeSeUnico() {
		SiopeSpesa siope = ottieniSiopeSeUnico(TipologiaClassificatore.SIOPE_SPESA_I);
		model.setSiopeSpesa(siope);
	}

	/**
	 * Inserisce il Capitolo di Uscita Previsione
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String inserisceCDU() {
		final String methodName = "inserisceCDU";
		/* Valorizzo i classificatori a partire dalla sessione */
		log.debug(methodName, "Valorizzo i classificatori");
		model.caricaClassificatoriDaSessione(sessionHandler);
		
		log.debug(methodName, "Creazione della request");

		Boolean daVariazione = model.getDaVariazione();
		
		//SIAC-6884
		boolean decentrato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.INSERISCI_VARIAZIONE_DECENTRATA, sessionHandler.getAzioniConsentite());
		
		StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio = Boolean.TRUE.equals(model.getDaVariazione()) ? StatoOperativoElementoDiBilancio.PROVVISORIO : StatoOperativoElementoDiBilancio.VALIDO;
		
		if(decentrato){			
			log.debug(methodName, "Operatore decentrato: operazione non consentita");
			List<Errore> listaErrori = new ArrayList<Errore>();
			Errore err = new Errore();
			err.setCodice("COR_ERR_0044");
			err.setDescrizione("Operazione non consentita: Operatore decentrato");
			listaErrori.add(err);
			addErrori(listaErrori);
			
			return INPUT;
		}
		//SIAC-6884
		if(decentrato){			
			log.debug(methodName, "Operatore decentrato: operazione non consentita");
			checkCondition(false, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Operatore decentrato"), true);
		
			return INPUT;
		}
		InserisceCapitoloDiUscitaPrevisione request = null;
		try {
			request = model.creaRequestInserisceCapitoloDiUscitaPrevisione(statoOperativoElementoDiBilancio);
		} catch (Exception e) {
			log.error(methodName, "Errore nella creazione della request", e);
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore(e.getMessage()));
			return INPUT;
		}
			
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di inserimento");
		InserisceCapitoloDiUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.inserisceCapitoloDiUscitaPrevisione(request);
		log.debug(methodName, "Richiamato il WebService di inserimento");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);				
			log.debug(methodName, "Model: " + model);
					
			return INPUT;
		}
		
		// Controllo gli errori
		
		
		//SIAC-6884 mi serve per la visualizzazione delle componenti
		if(response.getCapitoloUPrevInserito()!= null && 
				response.getCapitoloUPrevInserito().getClassificatoriGenerici() != null && 
				!response.getCapitoloUPrevInserito().getClassificatoriGenerici().isEmpty()){
			
			for(ClassificatoreGenerico cg : response.getCapitoloUPrevInserito().getClassificatoriGenerici()){
				if(cg.getTipoClassificatore().getCodice().equals(TipologiaClassificatore.CLASSIFICATORE_3.name())){
					model.setCapitoloFondino(cg.getCodice().equals("01") ? true : false);
				}
						
			}
		}else{
			model.setCapitoloFondino(false);
		}
		
		
		log.debug(methodName, "Capitolo di Uscita di Previsione inserito correttamente");
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		/* Injetto il capitolo di uscita previsione ottenuto dalla response */
		model.injettaDatiNelCapitolo(response.getCapitoloUPrevInserito());
		
		/* Pulisco le liste caricate in sessione */
		sessionHandler.cleanAllSafely();
		
		// Se arrivo dal cdu delle Variazioni, imposto i dati nella lista
		if(Boolean.TRUE.equals(daVariazione)) {
			impostaDatiPerVariazioni(model.getCapitoloUscitaPrevisione());
			String suffixInserimento = model.isGestioneUEB()? "ConUEB" : "SenzaUEB";
			String suffixAggiornamento = model.isGestioneUEB()? "ConUEB" : "";
			String suffix = sessionHandler.getParametro(BilSessionParameter.INSERISCI_NUOVO_DA_AGGIORNAMENTO) != null ? "aggiornamento" + suffixAggiornamento : "inserimento" + suffixInserimento;
			return SUCCESS + "_" + suffix + "_variazione";
		}
		
		return SUCCESS;
	}
	
	/**
	 * Cerca il Capitolo di Uscita selezionato.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String cerca() {		
		final String methodName = "cerca";
		
		/* Validazione dei campi */
		log.debug(methodName, "Capitolo da cercare: " + model.getCapitoloUscitaPrevisione());
		
		CapitoloUscitaPrevisione capitolo = model.getCapitoloUscitaPrevisione();
		
		checkNotNull(capitolo.getNumeroCapitolo(), "Capitolo");
		checkNotNull(capitolo.getNumeroArticolo(), "Articolo");
		if(model.isGestioneUEB()) {
			checkNotNull(capitolo.getNumeroUEB(), "UEB");
		}
		if(hasErrori()) {
			return INPUT;
		}
		
		log.debug(methodName, "Creazione della request");
		RicercaPuntualeCapitoloUscitaPrevisione request = model.creaRequestRicercaPuntualeCapitoloUscitaPrevisione(false);
		logServiceRequest(request);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di ricerca puntuale");
		RicercaPuntualeCapitoloUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.ricercaPuntualeCapitoloUscitaPrevisione(request);
		log.debug(methodName, "Richiamato il WebService di ricerca puntuale");
		logServiceResponse(response);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Nessun capitolo corrispondente trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
		} else {
			log.debug(methodName, "Esiste un capitolo corrispondente trovato");
			
			String stringaErrore = "(" + 
					request.getRicercaPuntualeCapitoloUPrev().getAnnoEsercizio() + "/" +
					request.getRicercaPuntualeCapitoloUPrev().getAnnoCapitolo() + "/" +
					request.getRicercaPuntualeCapitoloUPrev().getNumeroCapitolo() + "/" +
					request.getRicercaPuntualeCapitoloUPrev().getNumeroArticolo() + "/" +
					// Sostituzione per le UEB
					"%%UEB%%" + 
					request.getRicercaPuntualeCapitoloUPrev().getStatoOperativoElementoDiBilancio() +
					")";
			
			if(model.isGestioneUEB()) {
				stringaErrore = stringaErrore.replace("%%UEB%%", request.getRicercaPuntualeCapitoloUPrev().getNumeroUEB() + "/");
				// TODO: Calcolo ultimo valore UEB libero
			} else {
				stringaErrore = stringaErrore.replace("%%UEB%%", "");
				/* Pulizia dei campi */
				log.debug(methodName, "Pulitura dei campi del form");
				model.getCapitoloUscitaPrevisione().setNumeroCapitolo(null);
				model.getCapitoloUscitaPrevisione().setNumeroArticolo(null);
			}
			
			Errore errore = ErroreCore.ENTITA_PRESENTE.getErrore("CAPITOLO USCITA PREVISIONE", stringaErrore);
			addErrore(methodName, errore);
			
		}
		return SUCCESS;
	}
	
	/**
	 * Copia il Capitolo di Uscita selezionato.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String copia() {
		final String methodName = "copia";
		validateCopia();
		if(hasErrori()) {
			log.debug(methodName, "Errori di validazione");
			return INPUT;
		}
		
		if(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE.equals(model.getTipoCapitoloCopia())) {
			log.debug(methodName, "Copia da previsione");
			return copiaCapitoloUscitaPrevisione();
		}
		log.debug(methodName, "Copia da gestione");
		return copiaCapitoloUscitaGestione();
	}
	
	/**
	 * Copia dei dati del capitolo di uscita previsione
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	private String copiaCapitoloUscitaPrevisione() {
		final String methodName = "copiaCapitoloUscitaPrevisione";
		log.debug(methodName, "Creazione della request");
		RicercaPuntualeCapitoloUscitaPrevisione requestPuntuale = model.creaRequestRicercaPuntualeCapitoloUscitaPrevisione(true);
		logServiceRequest(requestPuntuale);
		
		log.debug(methodName, "Richiamo il WebService di ricerca puntuale");
		RicercaPuntualeCapitoloUscitaPrevisioneResponse responsePuntuale = capitoloUscitaPrevisioneService.ricercaPuntualeCapitoloUscitaPrevisione(requestPuntuale);
		log.debug(methodName, "Richiamato il WebService di ricerca puntuale");
		logServiceResponse(responsePuntuale);
		
		if(responsePuntuale.isFallimento()) {
			log.debug(methodName, "Nessun capitolo corrispondente trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return INPUT;
		}
		if(responsePuntuale.hasErrori()) {
			log.info(methodName, "Vi sono errori nell'invocazione del servizio di ricerca puntuale");
			addErrori(responsePuntuale);
			return INPUT;
		}
		
		log.debug(methodName, "Trovato un capitolo corrispondente: creazione della request per la ricerca di dettaglio");
		RicercaDettaglioCapitoloUscitaPrevisione requestDettaglio = model.creaRequestRicercaDettaglioCapitoloUscitaPrevisione(responsePuntuale.getCapitoloUscitaPrevisione().getUid());
		logServiceRequest(requestDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaPrevisioneResponse responseDettaglio = capitoloUscitaPrevisioneService.ricercaDettaglioCapitoloUscitaPrevisione(requestDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseDettaglio);
		
		if(responseDettaglio.hasErrori()) {
			log.info(methodName, "Vi sono errori nell'invocazione del servizio di ricerca dettaglio");
			addErrori(responseDettaglio);
			return INPUT;
		}
		
		log.debug(methodName, "Impostazione dei dati dal capitolo presente in archivio");
		model.copiaDatiCapitolo(responseDettaglio.getCapitoloUscitaPrevisione());
		
		/* Carico le liste dei classificatori */
		log.debug(methodName, "Carico le liste dei classificatori");
		caricaListaCodificheAggiornamento();
		return SUCCESS;
	}

	/**
	 * Copia dei dati del capitolo di uscita gestione
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	private String copiaCapitoloUscitaGestione() {
		final String methodName = "copiaCapitoloUscitaGestione";
		log.debug(methodName, "Creazione della request");
		RicercaPuntualeCapitoloUscitaGestione requestPuntuale = model.creaRequestRicercaPuntualeCapitoloUscitaGestione();
		logServiceRequest(requestPuntuale);
		
		log.debug(methodName, "Richiamo il WebService di ricerca puntuale");
		RicercaPuntualeCapitoloUscitaGestioneResponse responsePuntuale = capitoloUscitaGestioneService.ricercaPuntualeCapitoloUscitaGestione(requestPuntuale);
		log.debug(methodName, "Richiamato il WebService di ricerca puntuale");
		logServiceResponse(responsePuntuale);
		
		if(responsePuntuale.isFallimento()) {
			log.debug(methodName, "Nessun capitolo corrispondente trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return INPUT;
		}
		if(responsePuntuale.hasErrori()) {
			log.info(methodName, "Vi sono errori nell'invocazione del servizio di ricerca puntuale");
			addErrori(responsePuntuale);
			return INPUT;
		}
		
		log.debug(methodName, "Trovato un capitolo corrispondente: creazione della request per la ricerca di dettaglio");
		RicercaDettaglioCapitoloUscitaGestione requestDettaglio = model.creaRequestRicercaDettaglioCapitoloUscitaGestione(responsePuntuale.getCapitoloUscitaGestione().getUid());
		logServiceRequest(requestDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaGestioneResponse responseDettaglio = capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(requestDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseDettaglio);
		
		if(responseDettaglio.hasErrori()) {
			log.info(methodName, "Vi sono errori nell'invocazione del servizio di ricerca dettaglio");
			addErrori(responseDettaglio);
			return INPUT;
		}
		
		log.debug(methodName, "Impostazione dei dati dal capitolo presente in archivio");
		model.copiaDatiCapitolo(responseDettaglio.getCapitoloUscita());
		
		/* Carico le liste dei classificatori */
		log.debug(methodName, "Carico le liste dei classificatori");
		caricaListaCodificheAggiornamento();
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link #copia()}
	 */
	private void validateCopia() {
		/* Validazione dei campi */
		checkCondition(model.getBilancioDaCopiare() != null && model.getBilancioDaCopiare().getAnno() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno di bilancio"));
		checkNotNull(model.getTipoCapitoloCopia(), "Tipo da copiare");
		checkCondition(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE.equals(model.getTipoCapitoloCopia()) || TipoCapitolo.CAPITOLO_USCITA_GESTIONE.equals(model.getTipoCapitoloCopia()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Tipo da copiare", ": deve essere uscita previsione o gestione"));
		checkNotNull(model.getCapitoloDaCopiare(), "Capitolo da copiare", true);
		checkNotNull(model.getCapitoloDaCopiare().getNumeroCapitolo(), "Capitolo da copiare");
		checkNotNull(model.getCapitoloDaCopiare().getNumeroArticolo(), "Articolo da copiare");
		checkCondition(!model.isGestioneUEB() || model.getCapitoloDaCopiare().getNumeroUEB() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("UEB da copiare"));
	}
	
	/**
	 * Calcola i totali degli Stanziamenti di Previsione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String visualizza() {
		final String methodName = "visualizza";
		log.debug(methodName, "Creazione della request");
		CalcolaTotaliStanziamentiDiPrevisione request = model.creaRequestCalcolaTotaliStanziamentiDiPrevisione();
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di calcolo totali");
		request.setCalcolaComponenti(true);
		CalcolaTotaliStanziamentiDiPrevisioneResponse response = capitoloService.calcolaTotaliStanziamentiDiPrevisione(request);
		log.debug(methodName, "Richiamato il WebService di calcolo totali");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Successo. Carico i dati dalla Response");
		model.setTotaliEsercizio0(response.getTotaliAnnoEsercizioPassato());
		model.setTotaliEsercizio1(response.getTotaliAnnoEsercizioAttuale());
		model.setTotaliEsercizio2(response.getTotaliAnnoEsercizioSuccessivo());
		
		
		model.setComponenteImportiAnno0(createComponentiImportiModel(model.getTotaliEsercizio0()));
		model.setComponenteImportiAnno1(createComponentiImportiModel(model.getTotaliEsercizio1()));
		model.setComponenteImportiAnno2(createComponentiImportiModel(model.getTotaliEsercizio2()));
		
		
		
		
		
		
		// Salvataggio del model in sessione, sì che vengano mantenuti i dati salvati
		log.debug(methodName, "Salvataggio del model in sessione");
		sessionHandler.setParametro(BilSessionParameter.MODEL_INSERIMENTO_USCITA_PREVISIONE, model);
		
		return SUCCESS;
	}
	
	/**
	 * Redirige la action sul form di aggiornamento.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Redirigo verso l'aggiornamento. Capitolo da aggiornare - uid: " + model.getUidDaAggiornare());
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		CapitoloUscitaPrevisione cup = model.getCapitoloUscitaPrevisione();
		if(cup != null && !model.isGestioneUEB()){
			cup.setNumeroUEB(Integer.valueOf(1));
		}
		// Controllo che il capitolo ci sia
		checkNotNull(cup, "Capitolo", true);
		
		checkNotNull(cup.getNumeroCapitolo(), "Capitolo");
		checkNotNull(cup.getNumeroArticolo(), "Articolo");
		checkNotNull(cup.getNumeroUEB(), "UEB");
		checkNotNullNorEmpty(cup.getDescrizione(), "Descrizione");
		checkNotNullNorInvalidUid(cup.getCategoriaCapitolo(), "Tipo capitolo");
		
		// CR-2204: controllo la classificazione di bilancio solo per i tipi standard		
		// CR-5007: controllo la classificazione di bilancio solo per i tipi FondoPluriennaleVincolato
		if(isCategoriaCapitoloStandard(cup.getCategoriaCapitolo()) || isCategoriaCapitoloFondoPluriennaleVincolato(cup.getCategoriaCapitolo())) {
			checkNotNullNorInvalidUid(model.getMissione(), "Missione");
			checkNotNullNorInvalidUid(model.getProgramma(), "Programma");
			checkNotNullNorInvalidUid(model.getTitoloSpesa(), "Titolo");
			checkNotNullNorInvalidUid(model.getMacroaggregato(), "Macroaggregato");
			checkNotNullNorInvalidUid(model.getElementoPianoDeiConti(), "Elemento del Piano dei Conti");
			//task-9
			checkNotNullNorInvalidUid(model.getClassificazioneCofog(), "Cofog");
		}
		
		checkNotNullNorInvalidUid(model.getStrutturaAmministrativoContabile(), "Struttura Amministrativa Responsabile");
		
		//task-55
		Missione missioneConDati = ComparatorUtils.searchByUid(model.getListaMissione(), model.getMissione());
		if("20".equals(missioneConDati.getCodice())) {
			checkNotNull(cup.getFlagNonInserireAllegatoA1(), "Capitolo da non inserire nell'allegato A1");
		}else {
			model.getCapitoloUscitaPrevisione().setFlagNonInserireAllegatoA1(null);
		}
				
		
		checkCondition(!isMissioneWithCodice(CODICE_MISSIONE_20) || model.idEntitaPresente(model.getRisorsaAccantonata()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("risorsa accantonata"));
		
		//validaImportoCapitoloUscitaPrevisione();
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = 
				FaseBilancio.PREVISIONE.equals(faseBilancio) ||
				FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio);
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	@Override
	public Crumb getCurrentCrumb() {
		final String methodName = "getCurrentCrumb";
		// Il valore di default quando non vi sono precedenti actions è null
		// Qualora sulla pagina si trovi un null, l'indietro deve redirigere verso il cruscotto
		Crumb currentCrumb = null;
		BreadCrumbTrail trail = sessionHandler.getParametro(CommonSessionParameter.BREADCRUMB_TRAIL, BreadCrumbTrail.class);
		try {
			int numeroCrumbs = trail.getCrumbs().size();
			// Ottengo il crumb attuale
			currentCrumb = trail.getCrumbs().get(numeroCrumbs - 1);
		} catch(Exception e) {
			log.debug(methodName, "Il trail delle azioni precedenti non contiene sufficienti crumbs");
		}
		return currentCrumb;
	}
	
	/**
	 * Annullamento dei campi dell'azione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String annulla() {
		impostaValoriDefault();
		return SUCCESS;
	}
	
	/**
	 * Metodo di utilit&agrave; per la validazione degli importi per il Capitolo di previsione.
	 * 
	 * @param importi                l'Importo da validare
	 * @param annoRispettoAEsercizio lo scostamento dell'anno dell'Importo rispetto all'anno di esercizio
	 * @param controlloGlobale       variabile che definisce se effettuare un controllo su tutti i parametri o solo su alcuni
	 * @param forceCassaCoherence    forza la coerenza dell'importo di cassa: <code>cassa &le; competenza + residuo</code>
	 */
	@Override
	protected void validaImportoCapitolo(ImportiCapitolo importi, int annoRispettoAEsercizio, boolean controlloGlobale, boolean forceCassaCoherence) {
		int anno = model.getAnnoEsercizioInt() + annoRispettoAEsercizio;
		if(importi == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Competenza per anno " + anno));
			if(controlloGlobale) {
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Residui per anno " + anno));
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Cassa per anno " + anno));
			}
			return;
		}
		
		// Gli importi sono valorizzati
		checkImporto(importi.getStanziamento(), "Competenza", anno);
		if(controlloGlobale) {
			checkImporto(importi.getStanziamentoResiduo(), "Residui", anno);
			checkImporto(importi.getStanziamentoCassa(), "Cassa", anno);
			if(forceCassaCoherence && importi.getStanziamento() != null && importi.getStanziamentoResiduo() != null && importi.getStanziamentoCassa() != null
					&& (importi.getStanziamentoResiduo().compareTo(BigDecimal.ZERO)>0 || importi.getStanziamento().compareTo(BigDecimal.ZERO)>0 )) {
				checkCondition(importi.getStanziamentoCassa().subtract(importi.getStanziamento()).subtract(importi.getStanziamentoResiduo()).signum() <= 0,
						ErroreCore.VALORE_NON_CONSENTITO.getErrore("Cassa per anno " + anno, ": deve essere inferiore o uguale alla somma di competenza e residuo ("
							+ FormatUtils.formatCurrency(importi.getStanziamento().add(importi.getStanziamentoResiduo())) + ")"));
			}
		}
	}
	
	
	/**
	 * 
	 * Ipotizziamo che i componenti della spesa sono sicuramente presenti
	 * in realta le stesse liste dovrebbero avere lo stesso numero di componenti
	 */
	private List<ComponenteTotalePrevisioneModel> createComponentiImportiModel(TotaliAnnoDiEsercizio annoEsercizio){
		
		List<ComponenteTotalePrevisioneModel>  componentiImportiAnno = new ArrayList<ComponenteTotalePrevisioneModel>();
		if(annoEsercizio.getListaComponenteImportiCapitoloSpesa()!= null &&
				!annoEsercizio.getListaComponenteImportiCapitoloSpesa().isEmpty()){
			for(int i=0;i<annoEsercizio.getListaComponenteImportiCapitoloSpesa().size();i++){
				//SPESA
				ComponenteTotalePrevisioneModel ctp = new ComponenteTotalePrevisioneModel();
				ComponenteImportiCapitolo cic = annoEsercizio.getListaComponenteImportiCapitoloSpesa().get(i);
				ctp.setDescrizione(cic.getTipoComponenteImportiCapitolo().getDescrizione());
				ctp.setUid(cic.getTipoComponenteImportiCapitolo().getUid());
				ctp.setImportoSpesa((cic.getListaDettaglioComponenteImportiCapitolo()!= null && !cic.getListaDettaglioComponenteImportiCapitolo().isEmpty()) 
						? cic.getListaDettaglioComponenteImportiCapitolo().get(0).getImporto() : BigDecimal.ZERO ) ;
				//ENTRATA
				if(annoEsercizio.getListaComponenteImportiCapitoloEntrata()!= null &&
						!annoEsercizio.getListaComponenteImportiCapitoloEntrata().isEmpty()
						&& annoEsercizio.getListaComponenteImportiCapitoloEntrata().size()>i
						){
					ComponenteImportiCapitolo cicEntrata = annoEsercizio.getListaComponenteImportiCapitoloEntrata().get(i);
					if(cicEntrata.getListaDettaglioComponenteImportiCapitolo()!= null && !cicEntrata.getListaDettaglioComponenteImportiCapitolo().isEmpty()){
						ctp.setImportoEntrata(cicEntrata.getListaDettaglioComponenteImportiCapitolo().get(0).getImporto());
					}
				}else{
					ctp.setImportoEntrata(BigDecimal.ZERO);
				}
				//DIFFERENZA
				BigDecimal diff = ctp.getImportoEntrata().subtract(ctp.getImportoSpesa());
				ctp.setImportoDifferenza(diff);
				componentiImportiAnno.add(ctp);
			}
			
		}
		
		return componentiImportiAnno;
	}
	
	
	
	
}
