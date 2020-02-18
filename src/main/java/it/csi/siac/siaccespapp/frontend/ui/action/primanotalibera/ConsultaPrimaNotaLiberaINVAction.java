/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.primanotalibera;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.ConsultaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.primanotalibera.ConsultaPrimaNotaLiberaINVModel;
import it.csi.siac.siaccespser.frontend.webservice.PrimaNotaCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespiteResponse;
/**
 * Classe di action per la consultazione della prima nota libera FIN
 * 
 * @author Elisa Chiari
 * @version 1.0.1 - 08/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaPrimaNotaLiberaINVAction extends ConsultaPrimaNotaLiberaBaseAction <ConsultaPrimaNotaLiberaINVModel> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -7014277979603667010L;

	@Autowired private transient PrimaNotaCespiteService primaNotaCespiteService;
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Uso il metodo solo per forzare il breadcrumb
		leggiEventualiInformazioniAzionePrecedente();
		return super.execute();
	}
	
	/**
	 * Validazione della Prima Nota.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String valida() {
		final String methodName = "valida";
		ValidaPrimaNotaCespite request = model.creaRequestValidaPrimaNotaCespite();
		logServiceRequest(request);
		
		
		ValidaPrimaNotaCespiteResponse response = primaNotaCespiteService.validaPrimaNotaCespite(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		// Imposto il parametro di rientro, si' da ricalcolare i dati
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		log.info(methodName, "STOP");
		return SUCCESS;
	}

}
