/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.comune;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.comune.ListaComuniModel;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;

/**
 * Classe di action per la lista dei comuni.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/12/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ListaComuniAction extends GenericBilancioAction<ListaComuniModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3573100340479901402L;
	
	@Autowired private transient GenericService genericService;
	
	@Override
	public String execute() {
		final String methodName = "execute";
		ListaComuni request = model.creaRequestListaComuni();
		logServiceRequest(request);
		ListaComuniResponse response = genericService.cercaComuni(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(ListaComuni.class, response));
			addErrori(response);
			return SUCCESS;
		}
		// Successo. Imposto i comuni nel model
		model.setListaComuni(response.getListaComuni());
		return "success";
	}

}
