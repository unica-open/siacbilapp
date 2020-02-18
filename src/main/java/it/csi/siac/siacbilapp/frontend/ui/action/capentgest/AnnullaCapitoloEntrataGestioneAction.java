/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentgest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentgest.AnnullaCapitoloEntrataGestioneModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Classe di Action per la gestione annullamento del Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 15/07/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AnnullaCapitoloEntrataGestioneAction extends GenericBilancioAction<AnnullaCapitoloEntrataGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -622664603680164897L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		log.debugEnd(methodName, "");
	}
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Imposto il rientro dall'annullamento
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
		
		/* Reperimento dei dati */
		log.debug(methodName, "Creazione della request per la ricerca di dettaglio");
		RicercaDettaglioCapitoloEntrataGestione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloEntrataGestione();
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloEntrataGestioneResponse responseRicercaDettaglio = capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseRicercaDettaglio);
		
		if(responseRicercaDettaglio.hasErrori()) {
			/* Non dovrebbe capitare. Presente primariamento per tutela. */
			log.debug(methodName, "Nessuna entita' corrispondente trovata");
			addErrori(responseRicercaDettaglio.getErrori());
			return INPUT;
		}
		model.setBilancio(responseRicercaDettaglio.getBilancio());
		model.setCapitoloEntrataGestione(responseRicercaDettaglio.getCapitoloEntrataGestione());
		
		/* Verifica dell'annullabilita' del capitolo */
		log.debug(methodName, "Creazione della request per la verifica dell'annullabilità");
		VerificaAnnullabilitaCapitoloEntrataGestione requestVerificaAnnullabilita = model.creaRequestVerificaAnnullabilitaCapitoloEntrataGestione();
		logServiceRequest(requestVerificaAnnullabilita);
		
		log.debug(methodName, "Richiamo il WebService di verifica annullabilità");
		VerificaAnnullabilitaCapitoloEntrataGestioneResponse responseVerificaAnnullabilita = capitoloEntrataGestioneService.verificaAnnullabilitaCapitoloEntrataGestione(requestVerificaAnnullabilita);
		log.debug(methodName, "Richiamato il WebService di verifica annullabilità");
		logServiceResponse(responseVerificaAnnullabilita);
		if(responseVerificaAnnullabilita.hasErrori()) {
			log.info(methodName, "Fallimento del servizio di verifica annullabilita");
			addErrori(responseVerificaAnnullabilita.getErrori());
			return INPUT;
		}
		
		/* Annullamento del capitolo */
		log.debug(methodName, "Creazione della request per l'annullamento");
		AnnullaCapitoloEntrataGestione requestAnnullamento = model.creaRequestAnnullaCapitoloEntrataGestione();
		logServiceRequest(requestAnnullamento);
		
		log.debug(methodName, "Richiamo il WebService di annullamento");
		AnnullaCapitoloEntrataGestioneResponse responseAnnullamento = capitoloEntrataGestioneService.annullaCapitoloEntrataGestione(requestAnnullamento);
		log.debug(methodName, "Richiamato il WebService di annullamento");
		logServiceResponse(responseAnnullamento);
		
		if(responseAnnullamento.hasErrori()) {
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, responseAnnullamento);
			return INPUT;
		}
		
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;
	}
	
}
