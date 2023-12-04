/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacbilapp.frontend.ui.action;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccommonapp.action.AzioneRichiestaAction;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.VariabileProcesso;

/**
 * Action per azione richiesta
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AzioneRichiestaBilancioAction extends AzioneRichiestaAction {

	private static final long serialVersionUID = -7152300961767870228L;
	private static final String ERRORE_AZIONE_RICHIESTA = "Errore nell'ottenimento e parsificazione dell'azione richiesta";
	
	@Resource(name = "logoutUrl")
	private String logoutUrl;
	
	/**
	 * Ottiene l'azione richiesta da Cruscotto discriminando per il tipo di variabile
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 * 
	 * @throws FrontEndBusinessException in caso di eccezione nell'esecuzione del processo
	 */
	@AnchorAnnotation(value = "%{model.nomeAzione}", afterAction = true)
	public String executeProcess() throws FrontEndBusinessException {
		final String methodName = "executeProcess";
		String result;
		try {
			result = super.execute();
		} catch(Exception e) {
			log.error(methodName, ERRORE_AZIONE_RICHIESTA + ": " + e.getMessage(), e);
			throw new FrontEndBusinessException(ERRORE_AZIONE_RICHIESTA, e);
		}
		AzioneRichiesta azioneRichiesta = sessionHandler.getAzioneRichiesta();
		
		log.debug(methodName, "Discrimino il risultato sulla base della variabile di processo");
		//SIAC-8332
		String idAttivita = azioneRichiesta.getIdAttivita();
		if(StringUtils.isNotBlank(idAttivita)) {
			String[] splitted = StringUtils.split(idAttivita, "%&");
			if(splitted.length > 1) {
				return result + "_" + splitted[1];
			}
		}
		// C'e' solo questa variabile per discriminare
		/*
		VariabileProcesso variabileProcessoTipoVariazione =
				azioneRichiesta.findVariabileProcesso(BilConstants.VARIABILE_PROCESSO_TIPO_VARIAZIONE_BILANCIO.getConstant());
		StringBuilder varibileDiProcesso = new StringBuilder();	
		// Se vi e' una variabile di processo, la appongo al risultato
		if(variabileProcessoTipoVariazione != null) {
			log.debug(methodName, "La variabile di processo trovata e': " + variabileProcessoTipoVariazione.getValore());
			varibileDiProcesso.append("_").append(variabileProcessoTipoVariazione.getValore().toString().toUpperCase(getLocale()));
			
		}
		return result + varibileDiProcesso.toString();
		*/
		//SIA-8332 qui non dovrei mai finire
		return result;
	}
	
	@Override
	public void logServiceRequest(ServiceRequest request) {
		// In questa classe non faccio nulla
	}
	
	@Override
	public void logServiceResponse(ServiceResponse response) {
		// In questa classe non faccio nulla
	}
	
	
	/**
	 * @return the logoutUrl
	 */
	public String getLogoutUrl() {
		return logoutUrl;
	}

	/**
	 * @param logoutUrl
	 *            the logoutUrl to set
	 */
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
	
}

