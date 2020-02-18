/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.progetto;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.progetto.ConsultaProgettoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloProspettoRiassuntivoCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgettoResponse;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;

/**
 * Classe di Action per la gestione della consultazione del Progetto.
 * 
 * @author Alessandra Osorio,Nazha Ahmad
 * @version 1.0.0 - 10/02/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaProgettoAction extends GenericProgettoAction<ConsultaProgettoModel> {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	@Override
	public void prepareExecute() throws Exception {
		super.prepare();
		caricaListeCodifiche();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		log.debug(methodName, "Ricerco il progetto");
		RicercaDettaglioProgetto request = model.creaRequestRicercaDettaglioProgetto();
		logServiceRequest(request);
		
		RicercaDettaglioProgettoResponse response = progettoService.ricercaDettaglioProgetto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Progetto ottenuto");
		
		List<Cronoprogramma> listaCronoprogrammi;

		try {
			listaCronoprogrammi = ricercaCronoprogrammi(response.getProgetto());
		} catch(WebServiceInvocationFailureException e) {
			log.error(methodName, e.getMessage());
			return INPUT;
		}
		model.setListaCronoprogrammiCollegatiAlProgetto(listaCronoprogrammi);
		model.setCronoprogrammaDiGestione(model.aggiungiCronoprogrammaDaGestione());
		model.impostaDati(response, listaCronoprogrammi);
		sessionHandler.setParametro(BilSessionParameter.UID_PROGETTO, model.getProgetto().getUid());
		return SUCCESS;
	}
	
	/**
	 * Consulta i totali di Entrata del Cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String consultaTotaliEntrata() {
		final String methodName = "consultaTotaliEntrata";
		
		RicercaDettaglioCronoprogramma request = model.creaRequestRicercaDettaglioCronoprogramma();
		RicercaDettaglioCronoprogrammaResponse response = progettoService.ricercaDettaglioCronoprogramma(request);
		
		if(checkErroriResponse(response, methodName)) {
			return SUCCESS;
		}
		
		model.popolaMappaTotaliEntrata(response.getCronoprogramma());
		return SUCCESS;
	}
	
	/**
	 * Consulta i totali di Uscita del Cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String consultaTotaliUscita() {
		final String methodName = "consultaTotaliUscita";
		
		RicercaDettaglioCronoprogramma request = model.creaRequestRicercaDettaglioCronoprogramma();
		RicercaDettaglioCronoprogrammaResponse response = progettoService.ricercaDettaglioCronoprogramma(request);
		
		if(checkErroriResponse(response, methodName)) {
			return SUCCESS;
		}
		
		model.popolaMappaTotaliUscita(response.getCronoprogramma());
		return SUCCESS;
	}
	
	/**
	 * Consulta il cronoprogramma fornito dalla pagina.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String consultaCronoprogrammaGestione() {
		// implementata e modificata il 23/03/2015 ahmad
		final String methodName = "consultaCronoprogrammaGestione";
		log.debug(methodName, "Consultazione cronoprogrammaGestione");

		Integer uidProgetto = sessionHandler.getParametro(BilSessionParameter.UID_PROGETTO);
		CalcoloProspettoRiassuntivoCronoprogramma request = model.creaRequestCalcoloProspettoRiassuntivoCronoprogramma(uidProgetto);
		logServiceRequest(request);

		CalcoloProspettoRiassuntivoCronoprogrammaResponse response = progettoService.calcoloProspettoRiassuntivoCronoprogramma(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			log.info(
					methodName,
					createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}

		model.setListaProspettoRiassuntivoCronoprogramma(response
				.getListaProspettoRiassuntivoCronoprogramma());

		log.debug(methodName, "Consultazione effettuata con successo");
		return SUCCESS;
	}
	
}
