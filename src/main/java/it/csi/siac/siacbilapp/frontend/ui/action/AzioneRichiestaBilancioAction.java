/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacbilapp.frontend.ui.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccommonapp.action.AzioneRichiestaAction;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.ParametroAzioneRichiesta;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.VariabileProcesso;
import it.csi.siac.siaccorser.util.Costanti;

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
		// C'e' solo questa variabile per discriminare
		VariabileProcesso variabileProcessoTipoVariazione =
				azioneRichiesta.findVariabileProcesso(BilConstants.VARIABILE_PROCESSO_TIPO_VARIAZIONE_BILANCIO.getConstant());
		StringBuilder varibileDiProcesso = new StringBuilder();	
		// Se vi e' una variabile di processo, la appongo al risultato
		if(variabileProcessoTipoVariazione != null) {
			log.debug(methodName, "La variabile di processo trovata e': " + variabileProcessoTipoVariazione.getValore());
			varibileDiProcesso.append("_").append(variabileProcessoTipoVariazione.getValore().toString().toUpperCase(getLocale()));
		}
		
		return result + varibileDiProcesso.toString();
	}
	
	@Override
	protected void logServiceRequest(ServiceRequest request) {
		// In questa classe non faccio nulla
	}
	
	@Override
	protected void logServiceResponse(ServiceResponse response) {
		// In questa classe non faccio nulla
	}
	
	
	private String getUrlVariaizonieDecentrato(List<ParametroAzioneRichiesta> listParam){
		String res = null;
		if(listParam!= null && !listParam.isEmpty()){
						for(int k=0;k<listParam.size();k++){
				if(Costanti.URL_NAME_AZIONE_RICHIESTA.equals(listParam.get(k).getNome())){
					res = listParam.get(k).getValore();
					break;
				}
			}
		}
		return res;
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

