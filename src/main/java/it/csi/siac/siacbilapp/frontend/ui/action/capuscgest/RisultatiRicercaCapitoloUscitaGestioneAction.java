/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscgest;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.RisultatiRicercaCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Action per i risultati di ricerca del capitolo uscita gestione.
 * 
 * @author Alessandro Marchino
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloUscitaGestioneAction extends GenericBilancioAction<RisultatiRicercaCapitoloUscitaGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4694552578831284452L;
	
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;

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
		
		model.setTotaleImporti(sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE_IMPORTI, ImportiCapitoloUG.class));

		return SUCCESS;

	}
	
	/**
	 * Redirezione al metodo di aggiornamento.
	 * <br>
	 * <strong>Nota</strong>: il metodo di eliminazione non &egrave; ancora stato implementato.
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
		RicercaDettaglioCapitoloUscitaGestione requestRicercaDettaglio = 
				model.creaRequestRicercaDettaglioCapitoloUscitaGestione(uid);
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaGestioneResponse responseRicercaDettaglio = 
				capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseRicercaDettaglio);
		
		if(responseRicercaDettaglio.hasErrori()) {
			/* Non dovrebbe capitare. Presente primariamento per tutela. */
			log.info(methodName, "Nessuna entita corrispondente trovata");
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
			int startPosition = 0;
			if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
				startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
			}
			model.setSavedDisplayStart(startPosition);

			addErrori(responseRicercaDettaglio.getErrori());
			return INPUT;
		}
		model.setBilancio(responseRicercaDettaglio.getBilancio());
		model.setCapitoloUscitaGestione(responseRicercaDettaglio.getCapitoloUscita());

		
		/* Verifica dell'annullabilita' del capitolo */
		log.debug(methodName, "Creazione della request per la verifica dell'annullabilita");
		VerificaAnnullabilitaCapitoloUscitaGestione requestVerificaAnnullabilita = 
				model.creaRequestVerificaAnnullabilitaCapitoloUscitaGestione();
		logServiceRequest(requestVerificaAnnullabilita);
		
		log.debug(methodName, "Richiamo il WebService di verifica annullabilita");
		VerificaAnnullabilitaCapitoloUscitaGestioneResponse responseVerificaAnnullabilita =
				capitoloUscitaGestioneService.verificaAnnullabilitaCapitoloUscitaGestione(requestVerificaAnnullabilita);
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
		if(!responseVerificaAnnullabilita.isAnnullabilitaCapitolo()) {
			log.info(methodName, "Entita non annullabile");
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
		AnnullaCapitoloUscitaGestione requestAnnullamento = model.creaRequestAnnullaCapitoloUscitaGestione();
		logServiceRequest(requestAnnullamento);
		
		log.debug(methodName, "Richiamo il WebService di annullamento");
		AnnullaCapitoloUscitaGestioneResponse responseAnnullamento = 
				capitoloUscitaGestioneService.annullaCapitoloUscitaGestione(requestAnnullamento);
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
		log.debug(methodName, "dopo annulla StartPosition = " + startPosition);
			
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
		RicercaDettaglioCapitoloUscitaGestione requestRicercaDettaglio = 
				model.creaRequestRicercaDettaglioCapitoloUscitaGestione(uid);
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio per uid " + uid);
		RicercaDettaglioCapitoloUscitaGestioneResponse responseRicercaDettaglio = 
				capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(requestRicercaDettaglio);
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
		EliminaCapitoloUscitaGestione requestEliminazione = 
				model.creaRequestEliminaCapitoloUscitaGestione(responseRicercaDettaglio.getBilancio(),
						responseRicercaDettaglio.getCapitoloUscita());
		logServiceRequest(requestEliminazione);
		
		log.debug(methodName, "Richiamo il WebService di eliminazione per uid " + uid);
		EliminaCapitoloUscitaGestioneResponse responseEliminazione = 
				capitoloUscitaGestioneService.eliminaCapitoloUscitaGestione(requestEliminazione);
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
