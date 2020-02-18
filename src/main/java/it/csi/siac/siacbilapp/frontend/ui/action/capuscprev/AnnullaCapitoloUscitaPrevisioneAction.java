/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscprev;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.AnnullaCapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Classe di Action per la gestione annullamento del Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.1.0 07/10/2013 - Modifica per le chiamate e le risposte via AJAX 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AnnullaCapitoloUscitaPrevisioneAction extends GenericBilancioAction<AnnullaCapitoloUscitaPrevisioneModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1882827034383962284L;
	
	@Autowired private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	
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
		
		// Imposto il rientro dall'annullamento
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
		
		/* Reperimento dei dati */
		log.debug(methodName, "Creazione della request per la ricerca di dettaglio");
		RicercaDettaglioCapitoloUscitaPrevisione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloUscitaPrevisione();
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaPrevisioneResponse responseRicercaDettaglio = capitoloUscitaPrevisioneService.ricercaDettaglioCapitoloUscitaPrevisione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseRicercaDettaglio);
		
		if(responseRicercaDettaglio.hasErrori()) {
			/* Non dovrebbe capitare. Presente primariamento per tutela. */
			log.info(methodName, "Nessuna entita corrispondente trovata");
			addErrori(responseRicercaDettaglio.getErrori());
			return SUCCESS;
		}
		model.setBilancio(responseRicercaDettaglio.getBilancio());
		model.setCapitoloUscitaPrevisione(responseRicercaDettaglio.getCapitoloUscitaPrevisione());

		/* Verifica dell'annullabilita' del capitolo */
		log.debug(methodName, "Creazione della request per la verifica dell'annullabilita");
		VerificaAnnullabilitaCapitoloUscitaPrevisione requestVerificaAnnullabilita = model.creaRequestVerificaAnnullabilitaCapitoloUscitaPrevisione();
		logServiceRequest(requestVerificaAnnullabilita);
		
		log.debug(methodName, "Richiamo il WebService di verifica annullabilita");
		VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse responseVerificaAnnullabilita = capitoloUscitaPrevisioneService.verificaAnnullabilitaCapitoloUscitaPrevisione(requestVerificaAnnullabilita);
		log.debug(methodName, "Richiamato il WebService di verifica annullabilita");
		logServiceResponse(responseVerificaAnnullabilita);
		if(responseVerificaAnnullabilita.hasErrori()) {
			log.info(methodName, "Fallimento del servizio di verifica annullabilita");
			addErrori(responseVerificaAnnullabilita.getErrori());
			
			return SUCCESS;
		}
		if(!responseVerificaAnnullabilita.isAnnullabilitaCapitolo()) {
			log.info(methodName, "Entita non annullabile");
			addErrori(responseVerificaAnnullabilita.getErrori());
			
			return SUCCESS;
		}
		
		/* Annullamento del capitolo */
		log.debug(methodName, "Creazione della request per l'annullamento");
		AnnullaCapitoloUscitaPrevisione requestAnnullamento = model.creaRequestAnnullaCapitoloUscitaPrevisione();
		logServiceRequest(requestAnnullamento);
		
		log.debug(methodName, "Richiamo il WebService di annullamento");
		AnnullaCapitoloUscitaPrevisioneResponse responseAnnullamento = capitoloUscitaPrevisioneService.annullaCapitoloUscitaPrevisione(requestAnnullamento);
		log.debug(methodName, "Richiamato il WebService di annullamento");
		logServiceResponse(responseAnnullamento);
		
		if(responseAnnullamento.hasErrori()) {
			log.info(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, responseAnnullamento);
		}
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;
	}
	
	/* richiamare questo metodo dal caso d'uso "inserimento/aggiornamento variazioni" */
	
	
}
