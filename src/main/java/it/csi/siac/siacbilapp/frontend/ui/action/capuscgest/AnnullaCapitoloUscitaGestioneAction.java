/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscgest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.AnnullaCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Classe di Action per la gestione annullamento del Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 30/07/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AnnullaCapitoloUscitaGestioneAction extends GenericBilancioAction<AnnullaCapitoloUscitaGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5549852217296576309L;
	
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
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
		RicercaDettaglioCapitoloUscitaGestione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloUscitaGestione();
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaGestioneResponse responseRicercaDettaglio = capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseRicercaDettaglio);
		
		if(responseRicercaDettaglio.hasErrori()) {
			/* Non dovrebbe capitare. Presente primariamento per tutela. */
			log.debug(methodName, "Nessuna entita' corrispondente trovata");
			addErrori(responseRicercaDettaglio);
			return INPUT;
		}
		model.setBilancio(responseRicercaDettaglio.getBilancio());
		model.setCapitoloUscitaGestione(responseRicercaDettaglio.getCapitoloUscita());
		
		/* Verifica dell'annullabilita' del capitolo */
		log.debug(methodName, "Creazione della request per la verifica dell'annullabilità");
		VerificaAnnullabilitaCapitoloUscitaGestione requestVerificaAnnullabilita = model.creaRequestVerificaAnnullabilitaCapitoloUscitaGestione();
		logServiceRequest(requestVerificaAnnullabilita);
		
		log.debug(methodName, "Richiamo il WebService di verifica annullabilità");
		VerificaAnnullabilitaCapitoloUscitaGestioneResponse responseVerificaAnnullabilita = capitoloUscitaGestioneService.verificaAnnullabilitaCapitoloUscitaGestione(requestVerificaAnnullabilita);
		log.debug(methodName, "Richiamato il WebService di verifica annullabilità");
		logServiceResponse(responseVerificaAnnullabilita);
		if(responseVerificaAnnullabilita.hasErrori()) {
			log.info(methodName, "Fallimento del servizio di verifica annullabilita");
			addErrori(responseVerificaAnnullabilita.getErrori());
			return INPUT;
		}
		
		/* Annullamento del capitolo */
		log.debug(methodName, "Creazione della request per l'annullamento");
		AnnullaCapitoloUscitaGestione requestAnnullamento = model.creaRequestAnnullaCapitoloUscitaGestione();
		logServiceRequest(requestAnnullamento);
		
		log.debug(methodName, "Richiamo il WebService di annullamento");
		AnnullaCapitoloUscitaGestioneResponse responseAnnullamento = capitoloUscitaGestioneService.annullaCapitoloUscitaGestione(requestAnnullamento);
		log.debug(methodName, "Richiamato il WebService di annullamento");
		logServiceResponse(responseAnnullamento);
		
		if(responseAnnullamento.hasErrori()) {
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(responseAnnullamento);
			return INPUT;
		}
		
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;
	}
	
}
