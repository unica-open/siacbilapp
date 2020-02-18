/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentgest;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloEntrataAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentgest.AggiornaMassivaCapitoloEntrataGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloEntrata;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione massiva dell'aggiornamento del Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 24/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaMassivaCapitoloEntrataGestioneAction extends CapitoloEntrataAction<AggiornaMassivaCapitoloEntrataGestioneModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2977732598790956655L;

	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
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
			model.valutaModificabilitaClassificatori(responseControlloClassificatoriEditabili, true);
		}
		
		ControllaAttributiModificabiliCapitoloResponse responseControlloAttributiEditabili = sessionHandler.getParametroXmlType(BilSessionParameter.EDITABILITA_ATTRIBUTI, ControllaAttributiModificabiliCapitoloResponse.class);
		
		// Il controllo è necessario in caso di errore
		if(responseControlloAttributiEditabili != null) {
			log.debug(methodName, "Valuto l'editabilita dei campi");
			model.valutaModificabilitaAttributi(responseControlloAttributiEditabili, true);
		}
		
		log.debugEnd(methodName, "");
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		String methodName = "execute";
		log.debug(methodName, "Capitolo da aggiornare - anno: " + model.getAnnoCapitoloDaAggiornare() + "; numero capitolo: " + model.getNumeroCapitoloDaAggiornare() + 
				"; numero articolo: " + model.getNumeroArticoloDaAggiornare());
		
		/* Ricerca del capitolo */
		log.debug(methodName, "Creazione della request");
		RicercaDettaglioMassivaCapitoloEntrataGestione requestRicercaDettaglio = model.creaRequestRicercaDettaglioMassivaCapitoloEntrataGestione();
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio massiva");
		RicercaDettaglioMassivaCapitoloEntrataGestioneResponse responseRicercaDettaglio = capitoloEntrataGestioneService.ricercaDettaglioMassivaCapitoloEntrataGestione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio massiva");
		logServiceResponse(responseRicercaDettaglio);
		
		if(responseRicercaDettaglio.hasErrori()) {			
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, responseRicercaDettaglio);
			return INPUT;
		}
		
		log.debug(methodName, "Impostazione dei dati nel model");
		model.impostaDatiDaResponse(responseRicercaDettaglio);
		log.debug(methodName, "Caricamento delle ulteriori liste");
		caricaListaCodificheAggiornamento();
			
		ControllaClassificatoriModificabiliCapitoloResponse responseClassificatoriModificabili = 
				ottieniResponseControllaClassificatoriModificabiliCapitolo(model.getCapitoloEntrataGestione());
		ControllaAttributiModificabiliCapitoloResponse responseAttributiModificabili =
				ottieniResponseControllaAttributiModificabiliCapitolo(model.getCapitoloEntrataGestione());
		
		log.debug(methodName, "Valuto l'editabilita dei campi");
		model.valutaModificabilitaClassificatori(responseClassificatoriModificabili, true);
		model.valutaModificabilitaAttributi(responseAttributiModificabili, true);
		
		// controlla stati capitoli
		if(!controllaDatiCapitoli(responseRicercaDettaglio)) {
			log.debug(methodName, "Almeno un capitolo è in stato incongruente");
			addErrore(ErroreBil.CAPITOLO_NON_AGGIORNABILE_PERCHE_STATO_INCONGRUENTE.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Metto in sessione gli elementi già presenti nel model");
		sessionHandler.setParametro(BilSessionParameter.CLASSIFICATORI_AGGIORNAMENTO, ClassificatoreAggiornamentoFactory.getInstance(model));
		// Imposto la response per i classificatori modificabili in sessione
		sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, responseClassificatoriModificabili);
		sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_ATTRIBUTI, responseAttributiModificabili);
		
		return SUCCESS;
	}
	
	/**
	 * Aggiorna il Capitolo di Entrata Gestione
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String aggiornaCDU() {
		final String methodName = "aggiornaCDU";
		
		/* Valorizzo i classificatori a partire dalla sessione */
		log.debug(methodName, "Valorizzo i classificatori");
		
		// Carico i dati dalla sessione
		log.debug(methodName, "Caricamento dei dati originali dalla sessione");
		ClassificatoreAggiornamentoCapitoloEntrata classificatoreAggiornamento = 
				sessionHandler.getParametro(BilSessionParameter.CLASSIFICATORI_AGGIORNAMENTO, ClassificatoreAggiornamentoCapitoloEntrata.class);
		
		log.debug(methodName, "Creazione della request");
		AggiornaMassivoCapitoloDiEntrataGestione request = model.creaRequestAggiornaMassivoCapitoloDiEntrataGestione(classificatoreAggiornamento);
		logServiceRequest(request);
		
		log.debug(methodName,"Richiamo il WebService di aggiornamento massivo");
		AggiornaMassivoCapitoloDiEntrataGestioneResponse response = capitoloEntrataGestioneService.aggiornaMassivoCapitoloDiEntrataGestione(request);
		log.debug(methodName,"Richiamato il WebService di aggiornamento massivo");
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
		
		/* Aggiungo l'informazione di completamento */
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		CapitoloEntrataGestione ceg = model.getCapitoloEntrataGestione();
		// Carico i classificatori
		model.caricaClassificatoriDaSessione(sessionHandler);
		// Controllo che il capitolo ci sia
		checkNotNull(ceg, "Capitolo", true);
		
		checkNotNull(ceg.getNumeroCapitolo(), "Capitolo");
		checkNotNull(ceg.getNumeroArticolo(), "Articolo");
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
	 * Controlla che ogni capitolo sia in stato congruente.
	 * 
	 * @param response la ricerca di dettaglio da cui controllare i dati
	 * 
	 * @return <code>true</code> se ogni capitolo &eacute; in stato congruente; <code>false</code> altrimenti
	 */
	private boolean controllaDatiCapitoli(RicercaDettaglioMassivaCapitoloEntrataGestioneResponse response) {
		boolean almenoUnCapitoloInStatoValido = false;
		for(CapitoloEntrataGestione capitolo : response.getCapitoloMassivaEntrataGestione().getElencoCapitoli()) {
			boolean capitoloInStatoValido = StatoOperativoElementoDiBilancio.VALIDO.equals(capitolo.getStatoOperativoElementoDiBilancio());
			if(capitoloInStatoValido && !valutaVariazioniCapitolo(capitolo)) {
				return false;
			}
			
			// Controllo che almeno un capitolo sia in stato valido per effettuare l'aggiornamento
			almenoUnCapitoloInStatoValido = almenoUnCapitoloInStatoValido || capitoloInStatoValido;
		}
		return almenoUnCapitoloInStatoValido;
	}
	
	/**
	 * Controlla che il capitolo non abbia variazioni in stato non definitivo o non annullato collegate.
	 * 
	 * @param capitolo il capitolo per cui effettuare il controllo
	 * 
	 * @return <code>true</code> se non vi sono variazioni collegate in stato non definitivo o non annullate; <code>false</code> altrimenti
	 */
	private boolean valutaVariazioniCapitolo(CapitoloEntrataGestione capitolo) {
		final String methodName = "valutaVariazioniCapitolo";
		RicercaVariazioniCapitoloPerAggiornamentoCapitolo requestRicercaVariazioni = model.creaRequestRicercaVariazioniCapitoloPerAggiornamentoCapitolo(capitolo.getUid());
		logServiceRequest(requestRicercaVariazioni);
		RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse responseRicercaVariazioni = capitoloService.ricercaVariazioniCapitoloPerAggiornamentoCapitolo(requestRicercaVariazioni);
		logServiceResponse(responseRicercaVariazioni);
		
		if(responseRicercaVariazioni.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(requestRicercaVariazioni, responseRicercaVariazioni));
			addErrori(responseRicercaVariazioni);
			return false;
		}
		
		if(responseRicercaVariazioni.getNumeroVariazioniImporti() > 0 || responseRicercaVariazioni.getNumeroVariazioniCodifiche() > 0) {
			String str = getStringaErroreVariazioniNonDefinitiveCollegate(responseRicercaVariazioni.getVariazioneImportiNumero(), responseRicercaVariazioni.getVariazioneCodificheNumero());
			addMessaggio(ErroreBil.CAPITOLO_CON_VARIAZIONI_NON_DEFINITIVE_COLLEGATE.getErrore(str));
			//return false;
		}
		
		return true;
	}
	
}
