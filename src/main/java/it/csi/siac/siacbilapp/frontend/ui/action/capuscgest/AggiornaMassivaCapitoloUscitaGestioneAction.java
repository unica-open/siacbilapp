/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscgest;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.AggiornaMassivaCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloUscita;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione massiva dell'aggiornamento del Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 24/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaMassivaCapitoloUscitaGestioneAction extends CapitoloUscitaAction<AggiornaMassivaCapitoloUscitaGestioneModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6459820720046148162L;

	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE);
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
		RicercaDettaglioMassivaCapitoloUscitaGestione requestRicercaDettaglio = model.creaRequestRicercaDettaglioMassivaCapitoloUscitaGestione();
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio massiva");
		RicercaDettaglioMassivaCapitoloUscitaGestioneResponse responseRicercaDettaglio = capitoloUscitaGestioneService.ricercaDettaglioMassivaCapitoloUscitaGestione(requestRicercaDettaglio);
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
		
		ControllaClassificatoriModificabiliCapitoloResponse responseClassificatoriModificabili = ottieniResponseControllaClassificatoriModificabiliCapitolo(model.getCapitoloUscitaGestione());
		ControllaAttributiModificabiliCapitoloResponse responseAttributiModificabili = ottieniResponseControllaAttributiModificabiliCapitolo(model.getCapitoloUscitaGestione());
		
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
	 * Aggiorna il Capitolo di Uscita Gestione
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String aggiornaCDU() {
		final String methodName = "aggiornaCDU";
		
		/* Valorizzo i classificatori a partire dalla sessione */
		log.debug(methodName, "Valorizzo i classificatori");
		model.caricaClassificatoriDaSessione(sessionHandler);
		
		// Carico i dati dalla sessione
		log.debug(methodName, "Caricamento dei dati originali dalla sessione");
		ClassificatoreAggiornamentoCapitoloUscita classificatoreAggiornamento = 
				sessionHandler.getParametro(BilSessionParameter.CLASSIFICATORI_AGGIORNAMENTO, ClassificatoreAggiornamentoCapitoloUscita.class);
		
		log.debug(methodName, "Creazione della request");
		AggiornaMassivoCapitoloDiUscitaGestione request = model.creaRequestAggiornaMassivoCapitoloDiUscitaGestione(classificatoreAggiornamento);
		logServiceRequest(request);
		
		log.debug(methodName,"Richiamo il WebService di aggiornamento massivo");
		AggiornaMassivoCapitoloDiUscitaGestioneResponse response = capitoloUscitaGestioneService.aggiornaMassivoCapitoloDiUscitaGestione(request);
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
		
		log.debug(methodName, "Capitolo di Uscita di Gestione aggiornato correttamente");
		/* Ripulisco la sessione */
		// Mantengo i risultati di ricerca per tornare alla pagina dei risultati
		// Mantengo la posizione di rientro
		sessionHandler.cleanSafelyExcluding(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE, BilSessionParameter.RIENTRO_POSIZIONE_START);
		
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
		CapitoloUscitaGestione cug = model.getCapitoloUscitaGestione();
		// Controllo che il capitolo ci sia
		checkNotNull(cug, "Capitolo", true);
		
		checkNotNull(cug.getNumeroCapitolo(), "Capitolo");
		checkNotNull(cug.getNumeroArticolo(), "Articolo");
		checkNotNullNorEmpty(cug.getDescrizione(), "Descrizione");
		checkCondition(!model.isCategoriaCapitoloEditabile() || (cug.getCategoriaCapitolo() != null && cug.getCategoriaCapitolo().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo capitolo"));
		
		// CR-2204: controllo la classificazione di bilancio solo per i tipi standard
		if(isCategoriaCapitoloStandard(cug.getCategoriaCapitolo())) {
			checkCondition(!model.isMissioneEditabile() || (model.getMissione() != null && model.getMissione().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Missione"));
			checkCondition(!model.isProgrammaEditabile() || (model.getProgramma() != null && model.getProgramma().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Programma"));
			checkCondition(!model.isTitoloSpesaEditabile() || (model.getTitoloSpesa() != null && model.getTitoloSpesa().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Titolo"));
			checkCondition(!model.isMacroaggregatoEditabile() || (model.getMacroaggregato() != null && model.getMacroaggregato().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Macroaggregato"));
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
	private boolean controllaDatiCapitoli(RicercaDettaglioMassivaCapitoloUscitaGestioneResponse response) {
		boolean almenoUnCapitoloInStatoValido = false;
		for(CapitoloUscitaGestione capitolo : response.getCapitoloMassivaUscitaGestione().getElencoCapitoli()) {
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
	private boolean valutaVariazioniCapitolo(CapitoloUscitaGestione capitolo) {
		final String methodName = "valutaVariazioniCapitolo";
		RicercaVariazioniCapitoloPerAggiornamentoCapitolo requestRicercaVariazioni = model.creaRequestRicercaVariazioniCapitoloPerAggiornamentoCapitolo(capitolo.getUid());
		logServiceRequest(requestRicercaVariazioni);
		RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse responseRicercaVariazioni = capitoloService.ricercaVariazioniCapitoloPerAggiornamentoCapitolo(requestRicercaVariazioni);
		logServiceResponse(responseRicercaVariazioni);
		
		if(responseRicercaVariazioni.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(RicercaVariazioniCapitoloPerAggiornamentoCapitolo.class, responseRicercaVariazioni));
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
