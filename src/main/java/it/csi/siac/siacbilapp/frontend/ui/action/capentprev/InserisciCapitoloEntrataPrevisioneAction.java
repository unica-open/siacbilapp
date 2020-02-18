/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentprev;

import java.math.BigDecimal;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;
import org.softwareforge.struts2.breadcrumb.Crumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloEntrataAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentprev.InserisciCapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonapp.handler.session.CommonSessionParameter;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione dell'inserimento del Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 31/07/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciCapitoloEntrataPrevisioneAction extends CapitoloEntrataAction<InserisciCapitoloEntrataPrevisioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3294297196623922027L;
	
	@Autowired private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_ENTRATA_PREVISIONE);
		
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
		CapitoloEntrataPrevisione cepCopiare = model.getCapitoloDaCopiare();
		if(cepCopiare == null) {
			cepCopiare = new CapitoloEntrataPrevisione();
			model.setCapitoloDaCopiare(cepCopiare);
		}
		cepCopiare.setNumeroArticolo(0);
		cepCopiare.setNumeroUEB(1);
		log.debug(methodName, "Default per il capitolo da copiare - Articolo " + cepCopiare.getNumeroArticolo() + " UEB " + cepCopiare.getNumeroUEB());
		
		// Default capitolo da inserire
		boolean daCduChiamante = daCduChiamante("OP-GESC001-insVar", "OP-GESC002-aggVar");
		CapitoloEntrataPrevisione cep = model.getCapitoloEntrataPrevisione();
		if(cep == null) {
			cep = new CapitoloEntrataPrevisione();
			model.setCapitoloEntrataPrevisione(cep);
		}
		CategoriaCapitolo categoriaCapitoloStandard = ComparatorUtils.findByCodice(model.getListaCategoriaCapitolo(),
				BilConstants.CODICE_CATEGORIA_CAPITOLO_STANDARD.getConstant());
		
		cep.setCategoriaCapitolo(categoriaCapitoloStandard);
		cep.setFlagImpegnabile(Boolean.TRUE);
		if(!daCduChiamante || !model.isGestioneUEB()) {
			cep.setNumeroUEB(1);
			log.debug(methodName, "Default per il capitolo da inserire - UEB " + cep.getNumeroUEB());
		}
		
		// CR-2559
		impostaSiopeSeUnico();
		// SIAC-4724
		model.setTipoCapitoloCopia(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE);
	}
	
	/**
	 * CR-2559
	 * Se il SIOPE &eacute; univoco per l'ente, preselezionarlo.
	 */
	private void impostaSiopeSeUnico() {
		SiopeEntrata siope = ottieniSiopeSeUnico(TipologiaClassificatore.SIOPE_ENTRATA_I);
		model.setSiopeEntrata(siope);
	}
	
	/**
	 * Inserisce il Capitolo di Entrata Previsione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String inserisceCDU() {
		final String methodName = "inserisceCDU";
		log.debug(methodName, "Creazione della request");
		
		Boolean daVariazione = model.getDaVariazione();		
		
		StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio = Boolean.TRUE.equals(model.getDaVariazione()) ? StatoOperativoElementoDiBilancio.PROVVISORIO : StatoOperativoElementoDiBilancio.VALIDO;
		
		// SIAC-5582: Forzo il flag accertato per cassa a false nel caso in cui NON sia gestibile
		if(!model.isFlagAccertatoPerCassaVisibile()) {
			model.getCapitoloEntrataPrevisione().setFlagAccertatoPerCassa(Boolean.FALSE);
		}
		
		InserisceCapitoloDiEntrataPrevisione request = model.creaRequestInserisceCapitoloDiEntrataPrevisione(statoOperativoElementoDiBilancio);
		
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di inserimento");
		InserisceCapitoloDiEntrataPrevisioneResponse response = capitoloEntrataPrevisioneService.inserisceCapitoloDiEntrataPrevisione(request);
		log.debug(methodName, "Richiamato il WebService di inserimento");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		log.debug(methodName, "Capitolo di Entrata inserito correttamente");
		
		/* Injetto il capitolo di uscita previsione ottenuto dalla response */
		model.injettaDatiNelCapitolo(response.getCapitoloEntrataPrevisione());
		
		/* Pulisco le liste caricate in sessione */
		sessionHandler.cleanAllSafely();
		
		// Se arrivo dal cdu delle Variazioni, imposto i dati nella lista
		if(Boolean.TRUE.equals(daVariazione)) {
			impostaDatiPerVariazioni(model.getCapitoloEntrataPrevisione());
			String suffixInserimento = model.isGestioneUEB()? "ConUEB" : "SenzaUEB";
			String suffixAggiornamento = model.isGestioneUEB()? "ConUEB" : "";
			String suffix = sessionHandler.getParametro(BilSessionParameter.INSERISCI_NUOVO_DA_AGGIORNAMENTO) != null ? "aggiornamento" + suffixAggiornamento : "inserimento" + suffixInserimento;
			
			return SUCCESS + "_" + suffix + "_variazione";
		}
		
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;
	}
	
	/**
	 * Cerca il Capitolo di Entrata selezionato.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String cerca() {		
		final String methodName = "cerca";
		
		/* Validazione dei campi */
		log.debug(methodName, "Capitolo da cercare: " + model.getCapitoloEntrataPrevisione());
		
		CapitoloEntrataPrevisione capitolo = model.getCapitoloEntrataPrevisione();
		
		checkNotNull(capitolo.getNumeroCapitolo(), "Capitolo");
		checkNotNull(capitolo.getNumeroArticolo(), "Articolo");
		if(model.isGestioneUEB()) {
			checkNotNull(capitolo.getNumeroUEB(), "UEB");
		}
		
		if(hasErrori()) {
			return INPUT;
		}
		
		log.debug(methodName, "Creazione della request");
		RicercaPuntualeCapitoloEntrataPrevisione request = model.creaRequestRicercaPuntualeCapitoloEntrataPrevisione(false);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di ricerca puntuale");
		RicercaPuntualeCapitoloEntrataPrevisioneResponse response = capitoloEntrataPrevisioneService.ricercaPuntualeCapitoloEntrataPrevisione(request);
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
					request.getRicercaPuntualeCapitoloEPrev().getAnnoEsercizio() + "/" +
					request.getRicercaPuntualeCapitoloEPrev().getAnnoCapitolo() + "/" +
					request.getRicercaPuntualeCapitoloEPrev().getNumeroCapitolo() + "/" +
					request.getRicercaPuntualeCapitoloEPrev().getNumeroArticolo() + "/" +
					// Sostituzione per le UEB
					"%%UEB%%" + 
					request.getRicercaPuntualeCapitoloEPrev().getStatoOperativoElementoDiBilancio() +
					")";
			
			if(model.isGestioneUEB()) {
				stringaErrore = stringaErrore.replace("%%UEB%%", request.getRicercaPuntualeCapitoloEPrev().getNumeroUEB() + "/");
				// TODO: Calcolo ultimo valore UEB libero
			} else {
				stringaErrore = stringaErrore.replace("%%UEB%%", "");
				/* Pulizia dei campi */
				log.debug(methodName, "Pulitura dei campi del form");
				model.getCapitoloEntrataPrevisione().setNumeroCapitolo(null);
				model.getCapitoloEntrataPrevisione().setNumeroArticolo(null);
			}
			
			Errore errore = ErroreCore.ENTITA_PRESENTE.getErrore("CAPITOLO ENTRATA PREVISIONE", stringaErrore);
			addErrore(methodName, errore);
		}
		return SUCCESS;
	}
	
	/**
	 * Copia il Capitolo di Entrata selezionato.
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
		
		if(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE.equals(model.getTipoCapitoloCopia())) {
			log.debug(methodName, "Copia da previsione");
			return copiaCapitoloEntrataPrevisione();
		}
		log.debug(methodName, "Copia da gestione");
		return copiaCapitoloEntrataGestione();
	}
	
	/**
	 * Copia dei dati del capitolo di uscita previsione
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	private String copiaCapitoloEntrataPrevisione() {
		final String methodName = "copiaCapitoloEntrataPrevisione";
		log.debug(methodName, "Creazione della request");
		RicercaPuntualeCapitoloEntrataPrevisione requestPuntuale = model.creaRequestRicercaPuntualeCapitoloEntrataPrevisione(true);
		logServiceRequest(requestPuntuale);
		
		log.debug(methodName, "Richiamo il WebService di ricerca puntuale");
		RicercaPuntualeCapitoloEntrataPrevisioneResponse responsePuntuale = capitoloEntrataPrevisioneService.ricercaPuntualeCapitoloEntrataPrevisione(requestPuntuale);
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
		RicercaDettaglioCapitoloEntrataPrevisione requestDettaglio = model.creaRequestRicercaDettaglioCapitoloEntrataPrevisione(responsePuntuale.getCapitoloEntrataPrevisione().getUid());
		logServiceRequest(requestDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloEntrataPrevisioneResponse responseDettaglio = capitoloEntrataPrevisioneService.ricercaDettaglioCapitoloEntrataPrevisione(requestDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseDettaglio);
		
		if(responseDettaglio.hasErrori()) {
			log.info(methodName, "Vi sono errori nell'invocazione del servizio di ricerca dettaglio");
			addErrori(responseDettaglio);
			return INPUT;
		}
		
		log.debug(methodName, "Impostazione dei dati dal capitolo presente in archivio");
		model.copiaDatiCapitolo(responseDettaglio.getCapitoloEntrataPrevisione());
		
		/* Carico le liste dei classificatori */
		log.debug(methodName, "Carico le liste dei classificatori");
		caricaListaCodificheAggiornamento();
		return SUCCESS;
	}

	/**
	 * Copia dei dati del capitolo di uscita gestione
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	private String copiaCapitoloEntrataGestione() {
		final String methodName = "copiaCapitoloEntrataGestione";
		log.debug(methodName, "Creazione della request");
		RicercaPuntualeCapitoloEntrataGestione requestPuntuale = model.creaRequestRicercaPuntualeCapitoloEntrataGestione();
		logServiceRequest(requestPuntuale);
		
		log.debug(methodName, "Richiamo il WebService di ricerca puntuale");
		RicercaPuntualeCapitoloEntrataGestioneResponse responsePuntuale = capitoloEntrataGestioneService.ricercaPuntualeCapitoloEntrataGestione(requestPuntuale);
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
		RicercaDettaglioCapitoloEntrataGestione requestDettaglio = model.creaRequestRicercaDettaglioCapitoloEntrataGestione(responsePuntuale.getCapitoloEntrataGestione().getUid());
		logServiceRequest(requestDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloEntrataGestioneResponse responseDettaglio = capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(requestDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseDettaglio);
		
		if(responseDettaglio.hasErrori()) {
			log.info(methodName, "Vi sono errori nell'invocazione del servizio di ricerca dettaglio");
			addErrori(responseDettaglio);
			return INPUT;
		}
		
		log.debug(methodName, "Impostazione dei dati dal capitolo presente in archivio");
		model.copiaDatiCapitolo(responseDettaglio.getCapitoloEntrataGestione());
		
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
		checkCondition(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE.equals(model.getTipoCapitoloCopia()) || TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE.equals(model.getTipoCapitoloCopia()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Tipo da copiare", ": deve essere entrata previsione o gestione"));
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
		
		// Salvataggio del model in sessione, sì che vengano mantenuti i dati salvati
		log.debug(methodName, "Salvataggio del model in sessione");
		sessionHandler.setParametro(BilSessionParameter.MODEL_INSERIMENTO_ENTRATA_PREVISIONE, model);
		
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
		CapitoloEntrataPrevisione cep = model.getCapitoloEntrataPrevisione();
		if(cep != null && !model.isGestioneUEB()){
			cep.setNumeroUEB(Integer.valueOf(1));
		}
		// Carico i classificatori
		model.caricaClassificatoriDaSessione(sessionHandler);
		// Controllo che il capitolo ci sia
		checkNotNull(cep, "Capitolo", true);
		
		checkNotNull(cep.getNumeroCapitolo(), "Capitolo");
		checkNotNull(cep.getNumeroArticolo(), "Articolo");
		checkNotNull(cep.getNumeroUEB(), "UEB");
		checkNotNullNorEmpty(cep.getDescrizione(), "Descrizione");
		checkNotNullNorInvalidUid(cep.getCategoriaCapitolo(), "Tipo capitolo");
		
		// CR-2204: controllo la classificazione di bilancio solo per i tipi standard
		if(isCategoriaCapitoloStandard(cep.getCategoriaCapitolo())) {
			checkNotNullNorInvalidUid(model.getTitoloEntrata(), "Titolo");
			checkNotNullNorInvalidUid(model.getTipologiaTitolo(), "Tipologia");
			checkNotNullNorInvalidUid(model.getCategoriaTipologiaTitolo(), "Categoria");
			checkNotNullNorInvalidUid(model.getElementoPianoDeiConti(), "Elemento del Piano dei Conti");
		}
		
		checkNotNullNorInvalidUid(model.getStrutturaAmministrativoContabile(), "Struttura Amministrativa Responsabile");
		
		validaImportoCapitoloEntrataPrevisione();
	}
	
	/**
	 * Unifica la validazione per gli Importi del Capitolo di Entrata Previsione.
	 */
	private void validaImportoCapitoloEntrataPrevisione() {
		validaImportoCapitolo(model.getImportiCapitoloEntrataPrevisione0(), 0, true, true);
		validaImportoCapitolo(model.getImportiCapitoloEntrataPrevisione1(), 1, false, true);
		validaImportoCapitolo(model.getImportiCapitoloEntrataPrevisione2(), 2, false, true);
	}
	
	/**
	 * Carica il model da quello presente in sessione.
	 */
	private void caricaModelDaSessioneSePresente() {
		final String methodName = "caricaModelDaSessioneSePresente";
		
		InserisciCapitoloEntrataPrevisioneModel modelInSessione = sessionHandler.getParametro(BilSessionParameter.MODEL_INSERIMENTO_ENTRATA_PREVISIONE);
		if(modelInSessione != null) {
			log.debug(methodName, "Model presente in sessione. Copia dei dati");
			
			// Sostituire il model attuale con quello ottenuto dalla sessione
			model = modelInSessione;
			// Pulire la sessione
			log.debug(methodName, "Pulisco la sessione");
			sessionHandler.setParametro(BilSessionParameter.MODEL_INSERIMENTO_ENTRATA_PREVISIONE, null);
			// Caricare le liste derivate
			log.debug(methodName, "Caricamento eventuali liste derivate");
			caricaListaCodificheAggiornamento();
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
	 * Metodo di utilit&agrave; per la validazione degli importi per il Capitolo.
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
						ErroreCore.VALORE_NON_VALIDO.getErrore("Cassa per anno " + anno, ": deve essere inferiore o uguale alla somma di competenza e residuo ("
							+ FormatUtils.formatCurrency(importi.getStanziamento().add(importi.getStanziamentoResiduo())) + ")"));
			}
		}
	}
}
