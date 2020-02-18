/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentprev;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentprev.RisultatiRicercaCapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Action per i risultati di ricerca del capitolo entrata previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 08/08/2013
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloEntrataPrevisioneAction extends GenericBilancioAction<RisultatiRicercaCapitoloEntrataPrevisioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7025148602016620930L;
	
	@Autowired private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		int startPosition = 0;
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
			startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName,"startPosition = "+startPosition);
		
		model.setTotaleImporti(sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_PREVISIONE_IMPORTI, ImportiCapitoloEP.class));
		
		return SUCCESS;
	}

	/**
	 * Redirezione al metodo di aggiornamento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
 		log.debug(methodName, "Uid del capitolo da aggiornare: " + model.getUidDaAggiornare());
		int startPosition = 0;
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
			startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName,"startPosition = "+startPosition);
		
		return SUCCESS;
	}

	/**
	 * Redirezione al metodo di annullamento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		
		checkCasoDUsoApplicabile(model.getTitolo());
		
		int uid = model.getUidDaAnnullare();
		
		log.debug(methodName, "Uid del capitolo da annullare: " + uid);
		
		/* Reperimento dei dati */
		log.debug(methodName, "Creazione della request per la ricerca di dettaglio");
		RicercaDettaglioCapitoloEntrataPrevisione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloEntrataPrevisione(uid);
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloEntrataPrevisioneResponse responseRicercaDettaglio = capitoloEntrataPrevisioneService.ricercaDettaglioCapitoloEntrataPrevisione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseRicercaDettaglio);
		
		if(responseRicercaDettaglio.hasErrori()) {
			/* Non dovrebbe capitare. Presente primariamento per tutela. */
			log.info(methodName, "Nessuna entita corrispondente trovata");
			addErrori(responseRicercaDettaglio.getErrori());
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
			int startPosition = 0;
			if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
				startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
			}
			model.setSavedDisplayStart(startPosition);
			
			return INPUT;
		}
		model.setBilancio(responseRicercaDettaglio.getBilancio());
		model.setCapitoloEntrataPrevisione(responseRicercaDettaglio.getCapitoloEntrataPrevisione());

		
		/* Verifica dell'annullabilita' del capitolo */
		log.debug(methodName, "Creazione della request per la verifica dell'annullabilita");
		VerificaAnnullabilitaCapitoloEntrataPrevisione requestVerificaAnnullabilita = model.creaRequestVerificaAnnullabilitaCapitoloEntrataPrevisione();
		logServiceRequest(requestVerificaAnnullabilita);
		
		log.debug(methodName, "Richiamo il WebService di verifica annullabilita");
		VerificaAnnullabilitaCapitoloEntrataPrevisioneResponse responseVerificaAnnullabilita = capitoloEntrataPrevisioneService.verificaAnnullabilitaCapitoloEntrataPrevisione(requestVerificaAnnullabilita);
		log.debug(methodName, "Richiamato il WebService di verifica annullabilita");
		logServiceResponse(responseVerificaAnnullabilita);
		if(responseVerificaAnnullabilita.hasErrori()) {
			log.info(methodName, "Fallimento del servizio di verifica annullabilita");
			addErrori(responseVerificaAnnullabilita.getErrori());
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
			int startPosition = 0;
			if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
				startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
			}
			model.setSavedDisplayStart(startPosition);

			return INPUT;
		}
		
		/* Annullamento del capitolo */
		log.debug(methodName, "Creazione della request per l'annullamento");
		AnnullaCapitoloEntrataPrevisione requestAnnullamento = model.creaRequestAnnullaCapitoloEntrataPrevisione();
		logServiceRequest(requestAnnullamento);
		
		log.debug(methodName, "Richiamo il WebService di annullamento");
		AnnullaCapitoloEntrataPrevisioneResponse responseAnnullamento = 
				capitoloEntrataPrevisioneService.annullaCapitoloEntrataPrevisione(requestAnnullamento);
		log.debug(methodName, "Richiamato il WebService di annullamento");
		logServiceResponse(responseAnnullamento);
		
		if(responseAnnullamento.hasErrori()) {
			log.info(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, responseAnnullamento);
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
			int startPosition = 0;
			if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
				startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
			}
			model.setSavedDisplayStart(startPosition);
			return INPUT;
		}
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
		int startPosition = 0;
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
			startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName, "dopo annulla StartPosition = "+startPosition);
			
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;

	}



	/**
	 * Redirezione al metodo di consultazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid del capitolo da consultare: " + model.getUidDaConsultare());
		return SUCCESS;
	}

	
	/**
	 * Redirezione al metodo di eliminazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String elimina() {
		final String methodName = "elimina";
		
		int uid = model.getUidDaEliminare();
		
		log.debug(methodName, "Uid del capitolo da eliminare: " + uid);
		
		/* Reperimento dei dati */
		log.debug(methodName, "Creazione della request per la ricerca di dettaglio per uid " + uid);
		RicercaDettaglioCapitoloEntrataPrevisione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloEntrataPrevisione(uid);
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio per uid " + uid);
		RicercaDettaglioCapitoloEntrataPrevisioneResponse responseRicercaDettaglio = capitoloEntrataPrevisioneService.ricercaDettaglioCapitoloEntrataPrevisione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseRicercaDettaglio);
		
		if(responseRicercaDettaglio.hasErrori()) {
			/* Non dovrebbe capitare. Presente primariamento per tutela. */
			log.info(methodName, "Nessuna entita corrispondente trovata");
			addErrori(responseRicercaDettaglio.getErrori());
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
			int startPosition = 0;
			if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
				startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
			}
			model.setSavedDisplayStart(startPosition);
			// Anche se il risultato è SUCCESS, in realtà si ritorna alla pagina di partenza
			return INPUT;
		}
		
		/* Annullamento del capitolo */
		log.debug(methodName, "Creazione della request per l'eliminazione per uid " + uid);
		EliminaCapitoloEntrataPrevisione requestEliminazione = model.creaRequestEliminaCapitoloEntrataPrevisione(responseRicercaDettaglio.getBilancio(), responseRicercaDettaglio.getCapitoloEntrataPrevisione());
		logServiceRequest(requestEliminazione);
		
		log.debug(methodName, "Richiamo il WebService di eliminazione per uid " + uid);
		EliminaCapitoloEntrataPrevisioneResponse responseEliminazione = capitoloEntrataPrevisioneService.eliminaCapitoloEntrataPrevisione(requestEliminazione);
		log.debug(methodName, "Richiamato il WebService di annullamento");
		logServiceResponse(responseEliminazione);
		
		if(responseEliminazione.hasErrori()) {
			log.info(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, responseEliminazione);
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
			int startPosition = 0;
			if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
				startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
			}
			model.setSavedDisplayStart(startPosition);
			
			return INPUT;
		}
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
		int startPosition = 0;
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
			startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName, "dopo elimina StartPosition = "+startPosition);
			
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.CHIUSO.equals(faseBilancio) ||
				FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio) ||
				FaseBilancio.PLURIENNALE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
}
