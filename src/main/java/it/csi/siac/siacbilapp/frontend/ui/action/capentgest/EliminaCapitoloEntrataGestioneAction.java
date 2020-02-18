/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentgest;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentgest.EliminaCapitoloEntrataGestioneModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Classe di Action per la gestione dell'eliminazione del Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 07/10/2013 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class EliminaCapitoloEntrataGestioneAction extends GenericBilancioAction<EliminaCapitoloEntrataGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4547037195826616424L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		log.debugEnd(methodName, "");
	}
	
	@Override
	@SkipValidation
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Imposto il rientro dall'eliminazione
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
		
		/* Reperimento dei dati */
		log.debug(methodName, "Creazione della request per la ricerca di dettaglio per uid " + model.getUidDaEliminare());
		RicercaDettaglioCapitoloEntrataGestione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloEntrataGestione();
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio per uid " + model.getUidDaEliminare());
		RicercaDettaglioCapitoloEntrataGestioneResponse responseRicercaDettaglio = 
				capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseRicercaDettaglio);
		
		if(responseRicercaDettaglio.hasErrori()) {
			/* Non dovrebbe capitare. Presente primariamento per tutela. */
			log.info(methodName, "Nessuna entita corrispondente trovata");
			addErrori(responseRicercaDettaglio.getErrori());
			return SUCCESS;
		}
		
		model.setBilancio(responseRicercaDettaglio.getBilancio());
		model.setCapitoloEntrataGestione(responseRicercaDettaglio.getCapitoloEntrataGestione());
		
		/* Eliminazione del capitolo */
		log.debug(methodName, "Creazione della request per l'eliminazione per uid " + model.getUidDaEliminare());
		EliminaCapitoloEntrataGestione requestEliminazione = model.creaRequestEliminaCapitoloEntrataGestione();
		logServiceRequest(requestEliminazione);
		
		log.debug(methodName, "Richiamo il WebService di eliminazione per uid " + model.getUidDaEliminare());
		EliminaCapitoloEntrataGestioneResponse responseEliminazione = 
				capitoloEntrataGestioneService.eliminaCapitoloEntrataGestione(requestEliminazione);
		log.debug(methodName, "Richiamato il WebService di annullamento");
		logServiceResponse(responseEliminazione);
		
		if(responseEliminazione.hasErrori()) {
			log.info(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, responseEliminazione);
			return SUCCESS;
		}
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;
	}
	
}
