/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.interceptor.StrutsConversionErrorInterceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.conversion.impl.XWorkConverter;
import com.opensymphony.xwork2.interceptor.ConversionErrorInterceptor;
import com.opensymphony.xwork2.util.ValueStack;

import it.csi.siac.siacbilapp.frontend.ui.util.listener.ConversionPreResultListener;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonapp.action.GenericAction;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * Versione modificata dell'interceptor standard di Struts2 per gli errori di conversione. 
 * <br>
 * Oltre a injettare tali errori come fieldError, l'interceptor li injetta anche come un messaggio particolare negli actionError, 
 * s&iacute; da risultare pi&ugrave; agevole nella fase di error-forwarding.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 19/08/2013
 * @see ConversionErrorInterceptor
 */
public class ConversionErrorSIACInterceptor extends StrutsConversionErrorInterceptor {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7130958003704181828L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		final String methodName = invocation.getProxy().getMethod();
		LogUtil log = new LogUtil(invocation.getAction().getClass());
		
		/* Basato sul metodo intercept di com.opensymphony.xwork2.interceptor.ConversionErrorInterceptor */
		
		ActionContext invocationContext = invocation.getInvocationContext();
		// Mappa degli errori di conversione
		Map<String, Object> conversionErrors = invocationContext.getConversionErrors();
		// Stack di Struts2
		ValueStack stack = invocationContext.getValueStack();
		
		// Mappa rappresentante gli errori
		Map<Object, Object> fakie =  new HashMap<Object, Object>();
		
		/* Nel caso in cui vi siano errori di conversione */
		for(Map.Entry<String, Object> entry : conversionErrors.entrySet()) {
			String propertyName = entry.getKey();
			Object value = entry.getValue();
			
			// Controlla se l'errore debba essere effettivamente aggiunto
			if(shouldAddError(propertyName, value)) {
				// Messaggio standard di errore
				String message = XWorkConverter.getConversionErrorMessage(propertyName, stack);
				Object action = invocation.getAction();
				if(action instanceof GenericAction) {
					GenericAction<?> genericAction = (GenericAction<?>) action;
					log.info(methodName, "Errore di conversione: " + propertyName + " - " + message + " - " + value);
					// Impostazione dell'errore e del messaggio relativo nei fieldErrors
					genericAction.addFieldError(propertyName, message);
					
					// Aggiungo l'errore di conversione come ActionError della classe
					Errore erroreDiConversione = ErroreCore.FORMATO_NON_VALIDO.getErrore(propertyName, " ");
					genericAction.addActionError(erroreDiConversione.getTesto());
					genericAction.getModel().addErrore(erroreDiConversione);
				}
			}
			fakie.put(propertyName, getOverrideExpr(invocation, value));
		}
		
		if(!fakie.isEmpty()) {
			// Nel caso in cui vi siano degli errori, si rimettano i valori originali al posto giusto prima del risultato
			invocation.addPreResultListener(new ConversionPreResultListener(fakie));
		}
		
		return invocation.invoke();
	}
	
}
