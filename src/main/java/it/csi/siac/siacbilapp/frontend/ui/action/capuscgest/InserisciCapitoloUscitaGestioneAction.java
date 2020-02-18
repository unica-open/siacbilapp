/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.InserisciCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaGestioneResponse;
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
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

/**
 * Classe di Action per la gestione dell'inserimento del Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 30/07/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciCapitoloUscitaGestioneAction extends CapitoloUscitaAction<InserisciCapitoloUscitaGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5432781486431971236L;
	
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	@Autowired private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE);
		inizializzaDatiVariazione();
		
		log.debugEnd(methodName, "");
		
		
		
	}

	private void inizializzaDatiVariazione() {
		Boolean daVariazione = sessionHandler.getParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE, null);
		model.setDaVariazione(daVariazione);
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
		log.debug(methodName, "Impostazione dei valori di default per capitolo da copiare, capitolo da inserire e importi");
		
		// Default capitolo da copiare
		CapitoloUscitaGestione cugCopiare = model.getCapitoloDaCopiare();
		if(cugCopiare == null) {
			cugCopiare = new CapitoloUscitaGestione();
			model.setCapitoloDaCopiare(cugCopiare);
		}
		cugCopiare.setNumeroArticolo(0);
		cugCopiare.setNumeroUEB(1);
		log.debug(methodName, "Default per il capitolo da copiare - Articolo " + cugCopiare.getNumeroArticolo() + " UEB " + cugCopiare.getNumeroUEB());
		
		// Default capitolo da inserire
		boolean daCduChiamante = daCduChiamante("OP-GESC001-insVar", "OP-GESC002-aggVar");
		CapitoloUscitaGestione cug = model.getCapitoloUscitaGestione();
		if(cug == null) {
			cug = new CapitoloUscitaGestione();
			model.setCapitoloUscitaGestione(cug);
		}
		CategoriaCapitolo categoriaCapitoloStandard = ComparatorUtils.findByCodice(model.getListaCategoriaCapitolo(),
				BilConstants.CODICE_CATEGORIA_CAPITOLO_STANDARD.getConstant());
		
		cug.setCategoriaCapitolo(categoriaCapitoloStandard);
		cug.setFlagImpegnabile(Boolean.TRUE);
		if(!daCduChiamante || !model.isGestioneUEB()) {
			cug.setNumeroUEB(1);
			log.debug(methodName, "Default per il capitolo da inserire - UEB " + cug.getNumeroUEB());
		}
		
		ImportiCapitoloUG importi0 = new ImportiCapitoloUG();
		ImportiCapitoloUG importi1 = new ImportiCapitoloUG();
		ImportiCapitoloUG importi2 = new ImportiCapitoloUG();
		
		importi0.setStanziamento(BigDecimal.ZERO);
		importi0.setStanziamentoResiduo(BigDecimal.ZERO);
		importi0.setStanziamentoCassa(BigDecimal.ZERO);
		importi0.setFondoPluriennaleVinc(BigDecimal.ZERO);
		log.debug(methodName, "Default per gli importi capitolo anno " + model.getAnnoEsercizio() + " -" + " Competenza " + importi0.getStanziamento()
				+ " Residui " + importi0.getStanziamentoResiduo() + " Cassa " + importi0.getStanziamentoCassa() + " FPV " + importi0.getFondoPluriennaleVinc());
		
		importi1.setStanziamento(BigDecimal.ZERO);
		importi2.setStanziamento(BigDecimal.ZERO);
		
		model.setImportiCapitoloUscitaGestione0(importi0);
		model.setImportiCapitoloUscitaGestione1(importi1);
		model.setImportiCapitoloUscitaGestione2(importi2);
		
		// CR-2559
		impostaSiopeSeUnico();
		// SIAC-4724
		model.setTipoCapitoloCopia(TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
		
		//Servizio per dettaglio componenti
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
	 * Inserisce il Capitolo di Uscita Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String inserisceCDU() {
		final String methodName = "inserisceCDU";
		/* Valorizzo i classificatori a partire dalla sessione */
		log.debug(methodName, "Valorizzo i classificatori");
		model.caricaClassificatoriDaSessione(sessionHandler);
		
		Account account = sessionHandler.getAccount();
		boolean decentrato = AzioniConsentiteFactory.isConsentito(AzioniConsentite.INSERISCI_VARIAZIONE_DECENTRATA, sessionHandler.getAzioniConsentite());
		
		log.debug(methodName, "Creazione della request");
		
		Boolean daVariazione = model.getDaVariazione();
		
		StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio = Boolean.TRUE.equals(model.getDaVariazione()) ? StatoOperativoElementoDiBilancio.PROVVISORIO : StatoOperativoElementoDiBilancio.VALIDO;
		
		//SIAC-6884
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
		
		
		
		InserisceCapitoloDiUscitaGestione request = model.creaRequestInserisceCapitoloDiUscitaGestione(statoOperativoElementoDiBilancio);
		
		logServiceRequest(request);
		
		
		
		log.debug(methodName, "Richiamo il WebService di inserimento");
		InserisceCapitoloDiUscitaGestioneResponse response = capitoloUscitaGestioneService.inserisceCapitoloDiUscitaGestione(request);
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
		
		//SIAC-6884 mi serve per la visualizzazione delle componenti
		if(response.getCapitoloUscitaGestione().getClassificatoriGenerici() != null || response.getCapitoloUscitaGestione().getClassificatoriGenerici().size()>0){
			
			for(ClassificatoreGenerico cg : response.getCapitoloUscitaGestione().getClassificatoriGenerici()){
				if(cg.getTipoClassificatore().getCodice().equals(TipologiaClassificatore.CLASSIFICATORE_3.name())){
					model.setCapitoloFondino(cg.getCodice().equals("01") ? true : false);
				}
						
			}
		}else{
			model.setCapitoloFondino(false);
		}
		
		log.debug(methodName, "Capitolo di Uscita di Gestione inserito correttamente");
		
		/* Injetto il capitolo di uscita previsione ottenuto dalla response */
		model.injettaDatiNelCapitolo(response.getCapitoloUscitaGestione());
		
		/* Pulisco le liste caricate in sessione */
		sessionHandler.cleanAllSafely();
		
		// Se arrivo dal cdu delle Variazioni, imposto i dati nella lista
		if(Boolean.TRUE.equals(daVariazione)) {
			impostaDatiPerVariazioni(model.getCapitoloUscitaGestione());
			String suffixInserimento = model.isGestioneUEB()? "ConUEB" : "SenzaUEB";
			String suffixAggiornamento =  model.isGestioneUEB()? "ConUEB" : "";
			String suffix = sessionHandler.getParametro(BilSessionParameter.INSERISCI_NUOVO_DA_AGGIORNAMENTO) != null ? "aggiornamento" + suffixAggiornamento : "inserimento" + suffixInserimento;
			return SUCCESS + "_" + suffix + "_variazione";
		}
		
		// Aggiungo l'informazione
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
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
		log.debug(methodName, "Capitolo da cercare: " + model.getCapitoloUscitaGestione());
		
		CapitoloUscitaGestione capitolo = model.getCapitoloUscitaGestione();
		
		checkNotNull(capitolo.getNumeroCapitolo(), "Capitolo");
		checkNotNull(capitolo.getNumeroArticolo(), "Articolo");
		if(model.isGestioneUEB()) {
			checkNotNull(capitolo.getNumeroUEB(), "UEB");
		}
		
		if(hasErrori()) {
			return INPUT;
		}
		
		log.debug(methodName, "Creazione della request");
		RicercaPuntualeCapitoloUscitaGestione request = model.creaRequestRicercaPuntualeCapitoloUscitaGestione(false);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di ricerca puntuale");
		RicercaPuntualeCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaPuntualeCapitoloUscitaGestione(request);
		log.debug(methodName, "Richiamato il WebService di ricerca puntuale");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Nessun capitolo corrispondente trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
		} else {
			log.debug(methodName, "Esiste un capitolo corrispondente trovato");
			
			String stringaErrore = "(" + 
					request.getRicercaPuntualeCapitoloUGest().getAnnoEsercizio() + "/" +
					request.getRicercaPuntualeCapitoloUGest().getAnnoCapitolo() + "/" +
					request.getRicercaPuntualeCapitoloUGest().getNumeroCapitolo() + "/" +
					request.getRicercaPuntualeCapitoloUGest().getNumeroArticolo() + "/" +
					// Sostituzione per le UEB
					"%%UEB%%" + 
					request.getRicercaPuntualeCapitoloUGest().getStatoOperativoElementoDiBilancio() +
					")";
			
			if(model.isGestioneUEB()) {
				stringaErrore = stringaErrore.replace("%%UEB%%", request.getRicercaPuntualeCapitoloUGest().getNumeroUEB() + "/");
				// TODO: Calcolo ultimo valore UEB libero
			} else {
				stringaErrore = stringaErrore.replace("%%UEB%%", "");
				/* Pulizia dei campi */
				log.debug(methodName, "Pulitura dei campi del form");
				model.getCapitoloUscitaGestione().setNumeroCapitolo(null);
				model.getCapitoloUscitaGestione().setNumeroArticolo(null);
			}
			
			Errore errore = ErroreCore.ENTITA_PRESENTE.getErrore("CAPITOLO USCITA GESTIONE", stringaErrore);
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
		
		if(TipoCapitolo.CAPITOLO_USCITA_GESTIONE.equals(model.getTipoCapitoloCopia())) {
			log.debug(methodName, "Copia da gestione");
			return copiaCapitoloUscitaGestione();
		}
		log.debug(methodName, "Copia da previsione");
		return copiaCapitoloUscitaPrevisione();
	}
	
	/**
	 * Copia dei dati del capitolo di uscita previsione
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	private String copiaCapitoloUscitaPrevisione() {
		final String methodName = "copiaCapitoloUscitaPrevisione";
		log.debug(methodName, "Creazione della request");
		RicercaPuntualeCapitoloUscitaPrevisione requestPuntuale = model.creaRequestRicercaPuntualeCapitoloUscitaPrevisione();
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
		RicercaPuntualeCapitoloUscitaGestione requestPuntuale = model.creaRequestRicercaPuntualeCapitoloUscitaGestione(true);
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
		CapitoloUscitaGestione cug = model.getCapitoloUscitaGestione();
		if(cug != null && !model.isGestioneUEB()){
			cug.setNumeroUEB(Integer.valueOf(1));
		}
		// Controllo che il capitolo ci sia
		checkNotNull(cug, "Capitolo", true);
		
		checkNotNull(cug.getNumeroCapitolo(), "Capitolo");
		checkNotNull(cug.getNumeroArticolo(), "Articolo");
		checkNotNull(cug.getNumeroUEB(), "UEB");
		checkNotNullNorEmpty(cug.getDescrizione(), "Descrizione");
		checkNotNullNorInvalidUid(cug.getCategoriaCapitolo(), "Tipo capitolo");
		
		// CR-2204: controllo la classificazione di bilancio solo per i tipi standard
		if(isCategoriaCapitoloStandard(cug.getCategoriaCapitolo()) || isCategoriaCapitoloFondoPluriennaleVincolato(cug.getCategoriaCapitolo())) {
			checkNotNullNorInvalidUid(model.getMissione(), "Missione");
			checkNotNullNorInvalidUid(model.getProgramma(), "Programma");
			checkNotNullNorInvalidUid(model.getTitoloSpesa(), "Titolo");
			checkNotNullNorInvalidUid(model.getMacroaggregato(), "Macroaggregato");
			checkNotNullNorInvalidUid(model.getElementoPianoDeiConti(), "Elemento del Piano dei Conti");
		}
		
		checkNotNullNorInvalidUid(model.getStrutturaAmministrativoContabile(), "Struttura Amministrativa Responsabile");
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = 
				FaseBilancio.GESTIONE.equals(faseBilancio) ||
				FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) ||
				FaseBilancio.ASSESTAMENTO.equals(faseBilancio);
		// SIAC-4637
		if(Boolean.TRUE.equals(model.getDaVariazione())) {
			faseDiBilancioCompatibile = faseDiBilancioCompatibile
					|| FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
		}
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
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
	
}
