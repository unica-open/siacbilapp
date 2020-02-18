/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.provvedimento;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.provvedimento.ConsultaProvvedimentoModel;

/**
 * Classe di action per la consultazione del provvedimento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 26/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaProvvedimentoAction extends GenericBilancioAction<ConsultaProvvedimentoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4624907365264008773L;
	
	@Autowired private transient ProvvedimentoService provvedimentoService;
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Creo la request
		log.debug(methodName, "Creo la request per la ricerca");
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		
		// Invoco il servizio
		log.debug(methodName, "Richiamo il WebService per la ricerca");
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		log.debug(methodName, "Richiamato il WebService per la ricerca");
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Non dovrebbe succedere
			log.debug(methodName, createErrorInServiceInvocationString(request, response));
			return INPUT;
		}
		
		// Ottengo il provvedimento come il primo della lista ricercata
		AttoAmministrativo provvedimentoDaConsultare = response.getListaAttiAmministrativi().get(0);
		
		// Imposto il risultato nel model
		model.setAttoAmministrativo(provvedimentoDaConsultare);
		
		//SIAC 6929
		model.getAttoAmministrativo().setBloccoRagioneria(provvedimentoDaConsultare.getBloccoRagioneria());
		model.getAttoAmministrativo().setInseritoManualmente(provvedimentoDaConsultare.getInseritoManualmente());
		model.getAttoAmministrativo().setProvenienza(provvedimentoDaConsultare.getProvenienza());
		
		return SUCCESS;
	}
	
}
