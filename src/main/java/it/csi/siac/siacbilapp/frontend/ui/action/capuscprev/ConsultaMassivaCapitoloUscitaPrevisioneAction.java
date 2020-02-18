/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscprev;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.ConsultaMassivaCapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse;

/**
 * Classe di Action per la gestione della consultazione massiva del Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 24/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaMassivaCapitoloUscitaPrevisioneAction extends CapitoloUscitaAction<ConsultaMassivaCapitoloUscitaPrevisioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5538265779844859520L;
	
	@Autowired private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	
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
			responseClassificatoriModificabili = ottieniResponseControllaClassificatoriModificabiliCapitolo(model.getCapitoloUscitaPrevisione());
		}
		
		RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse responseRicercaDettaglioMassiva = ottieniResponseRicercaDettaglioMassivaCapitoloUscitaPrevisione();
		
		if(responseRicercaDettaglioMassiva.hasErrori()) {
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, responseRicercaDettaglioMassiva);
			return INPUT;
		}
		
		log.debug(methodName, "Valuto l'editabilita dei campi");
		model.valutaConsultabilitaClassificatori(responseClassificatoriModificabili);
		
		log.debug(methodName, "Impostazione dei dati nel model");
		model.impostaDatiDaResponseESessione(responseRicercaDettaglioMassiva, sessionHandler);
		log.debug(methodName, "Dati impostati nel model");
		
		// Imposto in sessione i dati relativi alla lista paginata, sì che possa gestire il tutto via AJAX
		sessionHandler.setParametro(BilSessionParameter.LISTA_UEB_COLLEGATE, responseRicercaDettaglioMassiva.getCapitoloMassivaUscitaPrevisione().getElencoCapitoli());
		
		return SUCCESS;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di 
	 * {@link RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse}.
	 * 
	 * @return la response corrispondente
	 */
	private RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse ottieniResponseRicercaDettaglioMassivaCapitoloUscitaPrevisione() {
		final String methodName = "ottieniResponseRicercaDettaglioMassivaCapitoloUscitaPrevisione";
		log.debug(methodName, "Creo la request");
		RicercaDettaglioMassivaCapitoloUscitaPrevisione request = model.creaRequestRicercaDettaglioMassivaCapitoloUscitaPrevisione();
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService");
		RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.ricercaDettaglioMassivaCapitoloUscitaPrevisione(request);
		logServiceResponse(response);
		
		return response;
	}
	
}
