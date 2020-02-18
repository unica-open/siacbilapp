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

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.ConsultaMassivaCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestioneResponse;

/**
 * Classe di Action per la gestione della consultazione massiva del Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 24/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaMassivaCapitoloUscitaGestioneAction extends CapitoloUscitaAction<ConsultaMassivaCapitoloUscitaGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9121752108750619563L;

	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		log.debugEnd(methodName, "");
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		/* Effettuo la ricerca dei classificatori consultabili */
		ControllaClassificatoriModificabiliCapitoloResponse responseClassificatoriModificabili = 
				sessionHandler.getParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, ControllaClassificatoriModificabiliCapitoloResponse.class);
		if(responseClassificatoriModificabili == null) {
			responseClassificatoriModificabili = ottieniResponseControllaClassificatoriModificabiliCapitolo();
		}
		RicercaDettaglioMassivaCapitoloUscitaGestioneResponse responseRicercaDettaglioMassiva = ottieniResponseRicercaDettaglioMassivaCapitoloUscitaGestione();
		
		log.debug(methodName, "Valuto l'editabilita dei campi");
		model.valutaConsultabilitaClassificatori(responseClassificatoriModificabili);
		
		if(responseRicercaDettaglioMassiva.hasErrori()) {
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, responseRicercaDettaglioMassiva);
			return INPUT;
		}
		
		log.debug(methodName, "Impostazione dei dati nel model");
		model.impostaDatiDaResponseESessione(responseRicercaDettaglioMassiva, sessionHandler);
		log.debug(methodName, "Dati impostati nel model");
		
		// Imposto in sessione i dati relativi alla lista paginata, sì che possa gestire il tutto via AJAX
		sessionHandler.setParametro(BilSessionParameter.LISTA_UEB_COLLEGATE, responseRicercaDettaglioMassiva.getCapitoloMassivaUscitaGestione().getElencoCapitoli());
		
		return SUCCESS;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di 
	 * {@link ControllaClassificatoriModificabiliCapitolo}.
	 * 
	 * @return la response corrispondente
	 */
	private ControllaClassificatoriModificabiliCapitoloResponse ottieniResponseControllaClassificatoriModificabiliCapitolo() {
		final String methodName = "ottieniResponseControllaClassificatoriModificabiliCapitolo";
		log.debug(methodName, "Controllo quali classificatori siano modificabili");
		ControllaClassificatoriModificabiliCapitolo request = model.creaRequestControllaClassificatoriModificabiliCapitolo();
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di controllo dei classificatori");
		ControllaClassificatoriModificabiliCapitoloResponse response = capitoloService.controllaClassificatoriModificabiliCapitolo(request);
		logServiceResponse(response);
		
		if(!response.hasErrori()) {
			sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, ControllaClassificatoriModificabiliCapitoloResponse.class);
		}
		
		return response;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di 
	 * {@link RicercaDettaglioMassivaCapitoloUscitaGestioneResponse}.
	 * 
	 * @return la response corrispondente
	 */
	private RicercaDettaglioMassivaCapitoloUscitaGestioneResponse ottieniResponseRicercaDettaglioMassivaCapitoloUscitaGestione() {
		final String methodName = "ottieniResponseRicercaDettaglioMassivaCapitoloUscitaGestione";
		log.debug(methodName, "Creo la request");
		RicercaDettaglioMassivaCapitoloUscitaGestione request = model.creaRequestRicercaDettaglioMassivaCapitoloUscitaGestione();
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService");
		RicercaDettaglioMassivaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaDettaglioMassivaCapitoloUscitaGestione(request);
		logServiceResponse(response);
		
		return response;
	}
	
}
