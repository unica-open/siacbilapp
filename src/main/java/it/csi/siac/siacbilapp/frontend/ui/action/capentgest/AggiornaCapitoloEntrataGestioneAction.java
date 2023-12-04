/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentgest;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import xyz.timedrain.arianna.plugin.BreadCrumbTrail;
import xyz.timedrain.arianna.plugin.Crumb;
/*
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;
import org.softwareforge.struts2.breadcrumb.Crumb;
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloEntrataAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentgest.AggiornaCapitoloEntrataGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloEntrata;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CategoriaCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonapp.handler.session.CommonSessionParameter;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione dell'aggiornamento del Capitolo di Uscita gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 30/07/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaCapitoloEntrataGestioneAction extends CapitoloEntrataAction<AggiornaCapitoloEntrataGestioneModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7948006005000067832L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	/** Indica se sia necessario tornare indietro di due crumbs a termine dell'aggiornamento */
	private boolean tornareDiDueIndietroDopoAggiornamento;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE);
		ControllaClassificatoriModificabiliCapitoloResponse responseControlloClassificatoriEditabili = sessionHandler.getParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, ControllaClassificatoriModificabiliCapitoloResponse.class);
		
		// Il controllo è necessario in caso di errore
		if(responseControlloClassificatoriEditabili != null) {
			log.debug(methodName, "Valuto l'editabilita dei campi");
			model.valutaModificabilitaClassificatori(responseControlloClassificatoriEditabili, false);
		}
		
		ControllaAttributiModificabiliCapitoloResponse responseControlloAttributiEditabili = 
				sessionHandler.getParametroXmlType(BilSessionParameter.EDITABILITA_ATTRIBUTI, ControllaAttributiModificabiliCapitoloResponse.class);
		
		// Il controllo è necessario in caso di errore
		if(responseControlloAttributiEditabili != null) {
			log.debug(methodName, "Valuto l'editabilita dei campi");
			model.valutaModificabilitaAttributi(responseControlloAttributiEditabili, false);
		}
		
		log.debugEnd(methodName, "");
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		String methodName = "execute";
		
		checkCasoDUsoApplicabile(model.getTitolo());
		log.debug(methodName, "Capitolo da aggiornare - uid: " + model.getUidDaAggiornare());
		
		/* Ricerca del capitolo */
		log.debug(methodName, "Creazione della request");
		RicercaDettaglioCapitoloEntrataGestione request = model.creaRequestRicercaDettaglioCapitoloEntrataGestione();
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(request);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		log.debug(methodName, "Impostazione dei dati nel model");
		model.impostaDatiDaResponse(response);
		log.debug(methodName, "Caricamento delle ulteriori liste");
		
		caricaListaCodificheAggiornamento();
		
		//siac-5003
		StatoOperativoElementoDiBilancio stato = response.getCapitoloEntrataGestione().getStatoOperativoElementoDiBilancio();
		if (!StatoOperativoElementoDiBilancio.VALIDO.equals(stato) && !StatoOperativoElementoDiBilancio.PROVVISORIO.equals(stato)){
			addErrore(ErroreBil.CAPITOLO_NON_AGGIORNABILE_PERCHE_STATO_INCONGRUENTE.getErrore(""));
			return INPUT;
		}
		
		//TODO: martellata, per favore mettere a posto
		if(StatoOperativoElementoDiBilancio.VALIDO.equals(stato)){
			RicercaVariazioniCapitoloPerAggiornamentoCapitolo requestRicercaVariazioni = model.creaRequestRicercaVariazioniCapitoloPerAggiornamentoCapitolo(model.getUidDaAggiornare());
			logServiceRequest(requestRicercaVariazioni);
			RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse responseRicercaVariazioni = capitoloService.ricercaVariazioniCapitoloPerAggiornamentoCapitolo(requestRicercaVariazioni);
			logServiceResponse(responseRicercaVariazioni);
			
			if(responseRicercaVariazioni.hasErrori()) {
				log.debug(methodName, createErrorInServiceInvocationString(RicercaVariazioniCapitoloPerAggiornamentoCapitolo.class, responseRicercaVariazioni));
				addErrori(responseRicercaVariazioni);
				return INPUT;
			}
			
			if(responseRicercaVariazioni.getNumeroVariazioniImporti() > 0 || responseRicercaVariazioni.getNumeroVariazioniCodifiche() > 0) {
				String str = getStringaErroreVariazioniNonDefinitiveCollegate(responseRicercaVariazioni.getVariazioneImportiNumero(), responseRicercaVariazioni.getVariazioneCodificheNumero());
				addMessaggio(ErroreBil.CAPITOLO_CON_VARIAZIONI_NON_DEFINITIVE_COLLEGATE.getErrore(str));
				//return INPUT;
			}
		}
		log.debug(methodName, "Metto in sessione gli elementi già presenti nel model");
		sessionHandler.setParametro(BilSessionParameter.CLASSIFICATORI_AGGIORNAMENTO, ClassificatoreAggiornamentoFactory.getInstance(model));
		
		ControllaClassificatoriModificabiliCapitoloResponse responseClassificatoriModificabili = 
				ottieniResponseControllaClassificatoriModificabiliCapitolo(model.getCapitoloEntrataGestione());
		
		log.debug(methodName, "Valuto l'editabilita dei campi");
		model.valutaModificabilitaClassificatori(responseClassificatoriModificabili, false);
		
		ControllaAttributiModificabiliCapitoloResponse responseAttributiModificabili =
				ottieniResponseControllaAttributiModificabiliCapitolo(model.getCapitoloEntrataGestione());
		log.debug(methodName, "Valuto l'editabilita degli attributi");
		model.valutaModificabilitaAttributi(responseAttributiModificabili, false);
		
		// Imposto la response per i classificatori modificabili in sessione
		sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, responseClassificatoriModificabili);
		
		return SUCCESS;
	}
	
	/**
	 * Aggiorna il Capitolo di Uscita Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String aggiornaCDU() {
		final String methodName = "aggiornaCDU";
		/* Valorizzo i classificatori a partire dalla sessione */
		log.debug(methodName, "Valorizzo i classificatori");
		
		// Carico i dati dalla sessione
		log.debug(methodName, "Caricamento dei dati originali dalla sessione");
		ClassificatoreAggiornamentoCapitoloEntrata classificatoreAggiornamento = sessionHandler.getParametro(BilSessionParameter.CLASSIFICATORI_AGGIORNAMENTO, ClassificatoreAggiornamentoCapitoloEntrata.class);
		
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
		
		log.debug(methodName, "Creazione della request");
		AggiornaCapitoloDiEntrataGestione request = model.creaRequestAggiornaCapitoloDiEntrataGestione(classificatoreAggiornamento);
		logServiceRequest(request);
		
		log.debug(methodName,"Richiamo il WebService di aggiornamento");
		AggiornaCapitoloDiEntrataGestioneResponse response = capitoloEntrataGestioneService.aggiornaCapitoloDiEntrataGestione(request);
		log.debug(methodName,"Richiamato il WebService di aggiornamento");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			caricaListaCodificheAggiornamento();
			return INPUT;
		}
		log.debug(methodName, "Capitolo di Entrata di Gestione aggiornato correttamente");
		
		/* Ripulisco la sessione */
		// Mantengo i risultati di ricerca per tornare alla pagina dei risultati
		// Mantengo la posizione di rientro
		sessionHandler.cleanSafelyExcluding(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE, BilSessionParameter.RIENTRO_POSIZIONE_START);
		
		/* 
		 * Imposto in sessione il dato di rientro dall'aggiornamento, 
		 * utile nel caso in cui si debba rientrare sui risultati di ricerca
		 */
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
		
		// Ripopolo gli eventuali campi che sono disabilitati
		model.setParametriDisabilitati(classificatoreAggiornamento);
		
		/* Imposta la necessita' di tornare indietro due volte a termine dell'esecuzione */
		if(!model.isDaAggiornamento()) {
			tornareDiDueIndietroDopoAggiornamento = true;
		}
		
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
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
		checkCondition(!model.isCategoriaCapitoloEditabile() || (ceg.getCategoriaCapitolo() != null && ceg.getCategoriaCapitolo().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo capitolo"));
		
		// CR-2204: controllo la classificazione di bilancio solo per i tipi standard
		if(isCategoriaCapitoloStandard(ceg.getCategoriaCapitolo())) {
			checkCondition(!model.isTitoloEntrataEditabile() || (model.getTitoloEntrata() != null && model.getTitoloEntrata().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Titolo"));
			checkCondition(!model.isTipologiaTitoloEditabile() || (model.getTipologiaTitolo() != null && model.getTipologiaTitolo().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipologia"));
			checkCondition(!model.isCategoriaTipologiaTitoloEditabile() || (model.getCategoriaTipologiaTitolo() != null && model.getCategoriaTipologiaTitolo().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Categoria"));
			checkCondition(!model.isElementoPianoDeiContiEditabile() || (model.getElementoPianoDeiConti() != null && model.getElementoPianoDeiConti().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Elemento del Piano dei Conti"));
		}
		
		checkCondition(!model.isStrutturaAmministrativoContabileEditabile() || (model.getStrutturaAmministrativoContabile() != null && model.getStrutturaAmministrativoContabile().getUid() != 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Struttura Amministrativa Responsabile"));
		
	}
	
	/**
	 * Ottiene il crumb precedente a partire dallo stack dei breadcrumbs.
	 * 
	 * @return the previousCrumb
	 */
	@Override
	public Crumb getPreviousCrumb() {
		// Fallback in caso di mancata necessità
		if(!tornareDiDueIndietroDopoAggiornamento) {
			return super.getPreviousCrumb();
		}
		
		final String methodName = "getPreviousAction";
		// Il valore di default quando non vi sono precedenti actions è null
		// Qualora sulla pagina si trovi un null, l'indietro deve redirigere verso il cruscotto
		Crumb previousCrumb = null;
		BreadCrumbTrail trail = sessionHandler.getParametro(CommonSessionParameter.BREADCRUMB_TRAIL, BreadCrumbTrail.class);
		try {
			int numeroCrumbs = trail.getCrumbs().size();
			// Ottengo DUE crumb precedenti rispetto a quello attuale
			previousCrumb = trail.getCrumbs().get(numeroCrumbs - 3);
		} catch(Exception e) {
			// Non dovrebbe succedere
			log.debug(methodName, "Il trail delle azioni precedenti non contiene sufficienti crumbs");
		}
		return previousCrumb;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = 
				FaseBilancio.GESTIONE.equals(faseBilancio) ||
				FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio);
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
		
}
