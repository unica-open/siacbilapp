/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.async.AsyncModel;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetOperazioneAsincResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.util.async.AsyncOperationHelper;

/**
 * Classe di Action relativa alle azioni asincrone.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 07/05/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AsyncAction extends GenericBilancioAction<AsyncModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5501813182942264930L;
	
	@Autowired private transient AsyncOperationHelper asyncOperationHelper;
	
	/**
	 * Effettua il polling dell'operazione asincrona.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String poll() {
		final String methodName = "poll";
		
		GetOperazioneAsincResponse response;
		try {
			response = asyncOperationHelper.getOperazioneAsinc(model.getIdOperazioneAsync(), model.getRichiedente());
		} catch (WebServiceInvocationFailureException e) {
			log.error(methodName, "Eccezione nell'invocazione del servizio", e);
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Eccezione nell'invocazione del servizio GetOperazioneAsincResponse: " + e.getCause().getMessage()));
			return SUCCESS;
		}
		
		if(response.isFallimento()) {
			log.debug(methodName, "Fallimento dell'invocazione");
			addErrori(response.getErrori());
			return SUCCESS;
		}
		
		// Imposto la disponibilità nel model
		model.setMessaggio(response.getMessaggio());
		model.setStato(response.getStato());
		return SUCCESS;
	}
	
}
