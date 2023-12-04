/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentgest;

import java.math.BigDecimal;

import org.apache.struts2.interceptor.validation.SkipValidation;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloEntrataAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentgest.InserisciCapitoloEntrataGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiPropostaNumeroCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiPropostaNumeroCapitoloResponse;
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
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.CategoriaCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione dell'inserimento del Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 30/07/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciCapitoloEntrataGestioneAction extends CapitoloEntrataAction<InserisciCapitoloEntrataGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2829978802277313437L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	@Autowired private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE);
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
		CapitoloEntrataGestione cegCopiare = model.getCapitoloDaCopiare();
		if(cegCopiare == null) {
			cegCopiare = new CapitoloEntrataGestione();
			model.setCapitoloDaCopiare(cegCopiare);
		}
		cegCopiare.setNumeroArticolo(0);
		cegCopiare.setNumeroUEB(1);
		log.debug(methodName, "Default per il capitolo da copiare - Articolo " + cegCopiare.getNumeroArticolo() + " UEB " + cegCopiare.getNumeroUEB());
		
		// Default capitolo da inserire
		boolean daCduChiamante = daCduChiamante("OP-GESC001-insVar", "OP-GESC002-aggVar");
		CapitoloEntrataGestione ceg = model.getCapitoloEntrataGestione();
		if(ceg == null) {
			ceg = new CapitoloEntrataGestione();
			model.setCapitoloEntrataGestione(ceg);
		}
		
		CategoriaCapitolo categoriaCapitoloStandard = ComparatorUtils.findByCodice(model.getListaCategoriaCapitolo(),
				BilConstants.CODICE_CATEGORIA_CAPITOLO_STANDARD.getConstant());
		
		ceg.setCategoriaCapitolo(categoriaCapitoloStandard);
		ceg.setFlagImpegnabile(Boolean.TRUE);
		if(!daCduChiamante || !model.isGestioneUEB()) {
			ceg.setNumeroUEB(1);
			log.debug(methodName, "Default per il capitolo da inserire - UEB " + ceg.getNumeroUEB());
		}
		
		ImportiCapitoloEG importi0 = new ImportiCapitoloEG();
		ImportiCapitoloEG importi1 = new ImportiCapitoloEG();
		ImportiCapitoloEG importi2 = new ImportiCapitoloEG();
		
		importi0.setStanziamento(BigDecimal.ZERO);
		importi0.setStanziamentoResiduo(BigDecimal.ZERO);
		importi0.setStanziamentoCassa(BigDecimal.ZERO);
		importi0.setFondoPluriennaleVinc(BigDecimal.ZERO);
		log.debug(methodName, "Default per gli importi capitolo anno " + model.getAnnoEsercizio() + " -" + " Competenza " + importi0.getStanziamento()
				+ " Residui " + importi0.getStanziamentoResiduo() + " Cassa " + importi0.getStanziamentoCassa() + " FPV " + importi0.getFondoPluriennaleVinc());
		
		importi1.setStanziamento(BigDecimal.ZERO);
		importi2.setStanziamento(BigDecimal.ZERO);
		
		//task-86
		if(abilitaNumerazioneAutomaticaCapitolo()) {
			LeggiPropostaNumeroCapitolo req = model.leggiPropostaNumeroCapitolo();
			logServiceRequest(req);
			LeggiPropostaNumeroCapitoloResponse response = capitoloService.leggiPropostaNumeroCapitoloService(req);
			logServiceResponse(response);
			if(!response.hasErrori()) {
				model.getCapitoloEntrataGestione().setNumeroCapitolo(response.getNumeroPropostoCapitolo());
			}else {
				log.debug(methodName, "Errore nella risposta del servizio");
				addErrori(methodName, response);
				log.debug(methodName, "Model: " + model);
			}
		}
		
		model.setImportiCapitoloEntrataGestione0(importi0);
		model.setImportiCapitoloEntrataGestione1(importi1);
		model.setImportiCapitoloEntrataGestione2(importi2);
		
		// CR-2559
		impostaSiopeSeUnico();
		// SIAC-4724
		model.setTipoCapitoloCopia(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE);
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
	 * Inserisce il Capitolo di Uscita Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String inserisceCDU() {
		final String methodName = "inserisceCDU";
		/* Valorizzo i classificatori a partire dalla sessione */
		log.debug(methodName, "Valorizzo i classificatori");
		
		log.debug(methodName, "Creazione della request");
		
		Boolean daVariazione = model.getDaVariazione();
		
		StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio = Boolean.TRUE.equals(model.getDaVariazione()) ? StatoOperativoElementoDiBilancio.PROVVISORIO : StatoOperativoElementoDiBilancio.VALIDO;
		
		// SIAC-5582: Forzo il flag accertato per cassa a false nel caso in cui NON sia gestibile
		if(!model.isFlagAccertatoPerCassaVisibile()) {
			model.getCapitoloEntrataGestione().setFlagAccertatoPerCassa(Boolean.FALSE);
		}
		
		//task-244
		if(model.getCapitoloEntrataGestione().getCategoriaCapitolo().getUid() >0)
			model.getCapitoloEntrataGestione().setCategoriaCapitolo(caricaCategoriaCapitolo(model.getCapitoloEntrataGestione().getCategoriaCapitolo().getUid()));
		
		//task-244
		if(CategoriaCapitoloEnum.categoriaIsFPV(model.getCapitoloEntrataGestione().getCategoriaCapitolo().getCodice()) || 
				model.getCapitoloEntrataGestione().getCategoriaCapitolo().getCodice().equals(CategoriaCapitoloEnum.AAM.getCodice()) ) {
			model.getCapitoloEntrataGestione().setFlagImpegnabile(null);
		}
		
			
		InserisceCapitoloDiEntrataGestione request = model.creaRequestInserisceCapitoloDiEntrataGestione(statoOperativoElementoDiBilancio);
		
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di inserimento");
		InserisceCapitoloDiEntrataGestioneResponse response = capitoloEntrataGestioneService.inserisceCapitoloDiEntrataGestione(request);
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
		
		log.debug(methodName, "Capitolo di Uscita di Gestione inserito correttamente");
		
		/* Injetto il capitolo di uscita Gestione ottenuto dalla response */
		/* Injetto il capitolo di uscita previsione ottenuto dalla response */
		model.injettaDatiNelCapitolo(response.getCapitoloEntrataGestione());
		
		/* Pulisco le liste caricate in sessione */
		sessionHandler.cleanAllSafely();
		
		// Se arrivo dal cdu delle Variazioni, imposto i dati nella lista
		if(Boolean.TRUE.equals(daVariazione)) {
			impostaDatiPerVariazioni(model.getCapitoloEntrataGestione());
			String suffixInserimento = model.isGestioneUEB()? "ConUEB" : "SenzaUEB";
			//fatto cosi per compatibilita' con versione precedente
			String suffixAggiornamento =  model.isGestioneUEB()? "ConUEB" : "";
			String suffix = sessionHandler.getParametro(BilSessionParameter.INSERISCI_NUOVO_DA_AGGIORNAMENTO) != null ? "aggiornamento" + suffixAggiornamento : "inserimento" + suffixInserimento;
			return SUCCESS + "_" + suffix + "_variazione";
		}
		
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
		log.debug(methodName, "Capitolo da cercare: " + model.getCapitoloEntrataGestione());
		
		CapitoloEntrataGestione capitolo = model.getCapitoloEntrataGestione();
		
		checkNotNull(capitolo.getNumeroCapitolo(), "Capitolo");
		checkNotNull(capitolo.getNumeroArticolo(), "Articolo");
		if(model.isGestioneUEB()) {
			checkNotNull(capitolo.getNumeroUEB(), "UEB");
		}
		
		if(hasErrori()) {
			return INPUT;
		}
		
		log.debug(methodName, "Creazione della request");
		RicercaPuntualeCapitoloEntrataGestione request = model.creaRequestRicercaPuntualeCapitoloEntrataGestione(false);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di ricerca puntuale");
		RicercaPuntualeCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaPuntualeCapitoloEntrataGestione(request);
		log.debug(methodName, "Richiamato il WebService di ricerca puntuale");
		logServiceResponse(response);
		
		if(response.isFallimento()) {
			log.debug(methodName, "Nessun capitolo corrispondente trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
		} else if(response.hasErrori()) {
			log.debug(methodName, "Fallimento della response");
			addErrori(response);
		} else {
			log.debug(methodName, "Esiste un capitolo corrispondente trovato");
			
			String stringaErrore = "(" + 
					request.getRicercaPuntualeCapitoloEGest().getAnnoEsercizio() + "/" +
					request.getRicercaPuntualeCapitoloEGest().getAnnoCapitolo() + "/" +
					request.getRicercaPuntualeCapitoloEGest().getNumeroCapitolo() + "/" +
					request.getRicercaPuntualeCapitoloEGest().getNumeroArticolo() + "/" +
					// Sostituzione per le UEB
					"%%UEB%%" + 
					request.getRicercaPuntualeCapitoloEGest().getStatoOperativoElementoDiBilancio() +
					")";
			
			if(model.isGestioneUEB()) {
				stringaErrore = stringaErrore.replace("%%UEB%%", request.getRicercaPuntualeCapitoloEGest().getNumeroUEB() + "/");
				// TODO: Calcolo ultimo valore UEB libero
			} else {
				stringaErrore = stringaErrore.replace("%%UEB%%", "");
				/* Pulizia dei campi */
				log.debug(methodName, "Pulitura dei campi del form");
				model.getCapitoloEntrataGestione().setNumeroCapitolo(null);
				model.getCapitoloEntrataGestione().setNumeroArticolo(null);
			}
			
			Errore errore = ErroreCore.ENTITA_PRESENTE.getErrore("CAPITOLO ENTRATA GESTIONE", stringaErrore);
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
		
		if(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE.equals(model.getTipoCapitoloCopia())) {
			log.debug(methodName, "Copia da gestione");
			return copiaCapitoloEntrataGestione();
		}
		log.debug(methodName, "Copia da previsione");
		return copiaCapitoloEntrataPrevisione();
	}
	
	/**
	 * Copia dei dati del capitolo di uscita previsione
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	private String copiaCapitoloEntrataPrevisione() {
		final String methodName = "copiaCapitoloEntrataPrevisione";
		log.debug(methodName, "Creazione della request");
		RicercaPuntualeCapitoloEntrataPrevisione requestPuntuale = model.creaRequestRicercaPuntualeCapitoloEntrataPrevisione();
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
		RicercaPuntualeCapitoloEntrataGestione requestPuntuale = model.creaRequestRicercaPuntualeCapitoloEntrataGestione(true);
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
		CapitoloEntrataGestione ceg = model.getCapitoloEntrataGestione();
		if(ceg != null && !model.isGestioneUEB()){
			ceg.setNumeroUEB(Integer.valueOf(1));
		}
		
		// Carico i classificatori
		model.caricaClassificatoriDaSessione(sessionHandler);
		// Controllo che il capitolo ci sia
		checkNotNull(ceg, "Capitolo", true);
		
		checkNotNull(ceg.getNumeroCapitolo(), "Capitolo");
		checkNotNull(ceg.getNumeroArticolo(), "Articolo");
		checkNotNull(ceg.getNumeroUEB(), "UEB");
		checkNotNullNorEmpty(ceg.getDescrizione(), "Descrizione");
		checkNotNullNorInvalidUid(ceg.getCategoriaCapitolo(), "Tipo capitolo");
		
		// CR-2204: controllo la classificazione di bilancio solo per i tipi standard
		if(isCategoriaCapitoloStandard(ceg.getCategoriaCapitolo())) {
			checkNotNullNorInvalidUid(model.getTitoloEntrata(), "Titolo");
			checkNotNullNorInvalidUid(model.getTipologiaTitolo(), "Tipologia");
			checkNotNullNorInvalidUid(model.getCategoriaTipologiaTitolo(), "Categoria");
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
