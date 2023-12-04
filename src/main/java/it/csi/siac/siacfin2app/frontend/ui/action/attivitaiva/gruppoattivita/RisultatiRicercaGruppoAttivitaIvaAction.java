/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.gruppoattivita;

import java.util.ArrayList;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.gruppoattivita.RisultatiRicercaGruppoAttivitaIvaModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.attivitaiva.ElementoAnnualizzazioneGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAnnualizzataGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAnnualizzataGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

/**
 * Action per i risultati di ricerca del GruppoAttivitaIva
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/05/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaGruppoAttivitaIvaAction extends GenericGruppoAttivitaIvaAction<RisultatiRicercaGruppoAttivitaIvaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 950335396478119525L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		// Ottengo il displayStart dalla sessione, se presente
		Integer posizioneStart = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		if(posizioneStart == null) {
			posizioneStart = Integer.valueOf(0);
		}
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart.intValue());
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		int startPosition = 0;
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
		
		log.debug(methodName, "StartPosition = " + startPosition);
		
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		
		return SUCCESS;
	}

	/**
	 * Redirezione al metodo di aggiornamento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Uid del gruppo da aggiornare: " + model.getUidDaAggiornare());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}

	/**
	 * Eliminazione del gruppo.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String elimina() {
		final String methodName = "elimina";
		
		EliminaGruppoAttivitaIva request = model.creaRequestEliminaGruppoAttivitaIva();
		logServiceRequest(request);
		EliminaGruppoAttivitaIvaResponse response = gruppoAttivitaIvaService.eliminaGruppoAttivitaIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio EliminaGruppoAttivitaIva");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Eliminato Gruppo con uid " + model.getUidDaEliminare());
		// Imposto il parametro DaRientro per avvertire la action di ricaricare la lista
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		// Imposto il successo in sessione
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	/**
	 * Consultazione del gruppo.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		
		RicercaDettaglioAnnualizzataGruppoAttivitaIva request = model.creaRequestRicercaDettaglioAnnualizzataGruppoAttivitaIva();
		logServiceRequest(request);
		RicercaDettaglioAnnualizzataGruppoAttivitaIvaResponse response = gruppoAttivitaIvaService.ricercaDettaglioAnnualizzataGruppoAttivitaIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioAnnualizzataGruppoAttivitaIva.class, response));
			addErrori(response);
			return INPUT;
		}
		if(response.getGruppiAttivitaIva().isEmpty()) {
			log.info(methodName, "Nessuna annualizzazione trovata per il gruppo " + model.getGruppoAttivitaIva().getUid());
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("gruppo attivita iva", "uid " + model.getGruppoAttivitaIva().getUid()));
			return INPUT;
		}
		
		// Prendo i dati non annualizzati
		GruppoAttivitaIva gruppoAttivitaIva = response.getGruppiAttivitaIva().get(0);
		
		// Ricerca effettuata con successo. Creo i wrapper
		List<ElementoAnnualizzazioneGruppoAttivitaIva> listaElementoAnnualizzazioneGruppoAttivitaIva = new ArrayList<ElementoAnnualizzazioneGruppoAttivitaIva>();
		for(GruppoAttivitaIva gai : response.getGruppiAttivitaIva()) {
			ElementoAnnualizzazioneGruppoAttivitaIva wrapper = new ElementoAnnualizzazioneGruppoAttivitaIva(gai);
			listaElementoAnnualizzazioneGruppoAttivitaIva.add(wrapper);
		}
		
		// Imposto i wrapper
		model.setGruppoAttivitaIva(gruppoAttivitaIva);
		model.setListaElementoAnnualizzazioneGruppoAttivitaIva(listaElementoAnnualizzazioneGruppoAttivitaIva);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #consulta()}.
	 */
	public void validateConsulta() {
		checkNotNullNorInvalidUid(model.getGruppoAttivitaIva(), "gruppo attivita iva");
	}
	
}
