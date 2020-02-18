/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentprev;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentprev.EliminaCapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Classe di Action per la gestione dell'eliminazione del Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 07/10/2013 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class EliminaCapitoloEntrataPrevisioneAction extends GenericBilancioAction<EliminaCapitoloEntrataPrevisioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4547037195826616424L;
	
	@Autowired private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;
	
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
		RicercaDettaglioCapitoloEntrataPrevisione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloEntrataPrevisione();
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio per uid " + model.getUidDaEliminare());
		RicercaDettaglioCapitoloEntrataPrevisioneResponse responseRicercaDettaglio = 
				capitoloEntrataPrevisioneService.ricercaDettaglioCapitoloEntrataPrevisione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseRicercaDettaglio);
		
		if(responseRicercaDettaglio.hasErrori()) {
			/* Non dovrebbe capitare. Presente primariamento per tutela. */
			log.info(methodName, "Nessuna entita corrispondente trovata");
			addErrori(responseRicercaDettaglio.getErrori());
			return SUCCESS;
		}
		
		model.setBilancio(responseRicercaDettaglio.getBilancio());
		model.setCapitoloEntrataPrevisione(responseRicercaDettaglio.getCapitoloEntrataPrevisione());
		
		/* Eliminazione del capitolo */
		log.debug(methodName, "Creazione della request per l'eliminazione per uid " + model.getUidDaEliminare());
		EliminaCapitoloEntrataPrevisione requestEliminazione = model.creaRequestEliminaCapitoloEntrataPrevisione();
		logServiceRequest(requestEliminazione);
		
		log.debug(methodName, "Richiamo il WebService di eliminazione per uid " + model.getUidDaEliminare());
		EliminaCapitoloEntrataPrevisioneResponse responseEliminazione = capitoloEntrataPrevisioneService.eliminaCapitoloEntrataPrevisione(requestEliminazione);
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
