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

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.RisultatiRicercaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.primanotalibera.RisultatiRicercaPrimaNotaLiberaINVModel;
import it.csi.siac.siaccespser.frontend.webservice.PrimaNotaCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespiteResponse;

/**
 * Classe di action per i risultati di ricerca della prima nota libera.
 * 
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrimaNotaLiberaINVAction extends RisultatiRicercaPrimaNotaLiberaBaseAction <RisultatiRicercaPrimaNotaLiberaINVModel> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -8283316480603327211L;

	@Autowired private transient PrimaNotaCespiteService primaNotaCespiteService;
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Uso il metodo solo per valorizzare il breadcrumb
		return super.execute();
	}
	
	
	/**
	 * Validazione della Prima Nota.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@Override
	public String valida() {
		final String methodName = "valida";
		log.info(methodName, "START");
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
	
	/**
	 * Rifiuta.
	 *
	 * @return the string
	 */
	public String rifiuta() {
		final String methodName = "rifiuta";
		log.info(methodName, "START");
		RifiutaPrimaNotaCespite request = model.creaRequestRifiutaPrimaNotaCespite();
		logServiceRequest(request);
		RifiutaPrimaNotaCespiteResponse response = primaNotaCespiteService.rifiutaPrimaNotaCespite(request);
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
