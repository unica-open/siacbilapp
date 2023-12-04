/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.dismissionecespite;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite.ConsultaDismissioneCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;


/**
 * The Class ConsultaDismissioneCespiteAction.
 * @author elisa
 * @version 1.0.0 - 09-08-2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaDismissioneCespiteAction extends GenericDismissioneCespiteAction<ConsultaDismissioneCespiteModel> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = 6723729473275279798L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setDismissioneCespite(null);
		RicercaDettaglioDismissioneCespiteResponse response = ottieniRicercaDettaglioDismissioneCespiteResponse();
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		model.setDismissioneCespite(response.getDismissioneCespite());
		return SUCCESS;		
	}
	
	/**
	 * Ottieni cespiti collegati.
	 *
	 * @return the string
	 */
	public String ottieniCespitiCollegati() {
		final String methodName = "caricaCespitiCollegati";
		RicercaSinteticaCespite req = model.creaRequestRicercaSinteticaCespite();
		RicercaSinteticaCespiteResponse res = cespiteService.ricercaSinteticaCespite(req);
		if(res.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(res);
			return SUCCESS;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");

		// Imposto in sessione i dati
		impostaParametriInSessione(req, res);
		return SUCCESS;
	}
	
	/**
	 * Impostazione dei parametri in sessione
	 * @param request  la request da impostare
	 * @param response la response con i parametri da impostare
	 */
	private void impostaParametriInSessione(RicercaSinteticaCespite request, RicercaSinteticaCespiteResponse response) {
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CESPITE_DA_DISMISSIONE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CESPITE_DA_DISMISSIONE, response.getListaCespite());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
	}
}
