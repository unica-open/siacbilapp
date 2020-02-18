/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import it.csi.siac.siacbilapp.frontend.ui.exception.UnauthorizedUserException;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.EnabledForCDU;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Checks whether the action/method annotated via the {@link EnabledForCDU} annotation is enabled for the user.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 25/05/2015
 */
public class EnabledForCDUInterceptor extends AbstractInterceptor {
	
	/** For serialization purposes */
	private static final long serialVersionUID = -8457309041759915441L;
	
	private String sessionParamName;

	/**
	 * @return the sessionParamName
	 */
	public String getSessionParamName() {
		return sessionParamName;
	}

	/**
	 * @param sessionParamName the sessionParamName to set
	 */
	public void setSessionParamName(String sessionParamName) {
		this.sessionParamName = sessionParamName;
	}

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		// Parameters
		final Object action = actionInvocation.getAction();
		final Class<?> clazz = action.getClass();
		final Method method = clazz.getMethod(actionInvocation.getProxy().getMethod());
		
		// Check first for annotated method
		EnabledForCDU annotation = method.getAnnotation(EnabledForCDU.class);
		if(annotation == null) {
			// Check the class for annotation
			annotation = clazz.getAnnotation(EnabledForCDU.class);
		}
		
		checkEnabled(actionInvocation, annotation);
		return actionInvocation.invoke();
	}

	/**
	 * Checks whether the invocation is enabled for the given CDU.
	 * 
	 * @param actionInvocation the invocation
	 * @param annotation       the annotation having the CDU informations to check
	 * 
	 * @throws UnauthorizedUserException if the user is not enabled for the execution
	 */
	private void checkEnabled(ActionInvocation actionInvocation, EnabledForCDU annotation) throws UnauthorizedUserException {
		if(annotation == null) {
			// If the annotation is not present, we simply return
			return;
		}
		String[] cdus = annotation.value();
		if(cdus != null && cdus.length > 0) {
			// Take the values from the session
			Map<String, Object> session = actionInvocation.getInvocationContext().getSession();
			@SuppressWarnings("unchecked")
			List<AzioneConsentita> azioniConsentite = (List<AzioneConsentita>) session.get(getSessionParamName());
			if(azioniConsentite != null) {
				for(String cdu : cdus) {
					for(AzioneConsentita ac : azioniConsentite) {
						if(ac.getAzione() != null && ac.getAzione().getNome().equals(cdu)) {
							// The action is enabled for the user
							return;
						}
					}
				}
			}
			Class<?> clazz = actionInvocation.getAction().getClass();
			String methodName = actionInvocation.getProxy().getMethod();
			new LogUtil(clazz).info(methodName, "Metodo non permesso per l'utente: non abilitato per i CDU " + Arrays.toString(cdus));
			throw new UnauthorizedUserException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'utente non e' abilitato all'esecuzione").getTesto());
		}
	}

}