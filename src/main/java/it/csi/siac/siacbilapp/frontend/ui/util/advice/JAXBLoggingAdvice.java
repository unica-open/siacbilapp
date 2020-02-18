/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.advice;

import javax.xml.bind.annotation.XmlType;

import org.aopalliance.intercept.MethodInvocation;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Advice di log per le invocazioni sugli XmlType.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 25/08/2015
 */
public class JAXBLoggingAdvice implements ClassAwareMethodInterceptor {

	private LogUtil log;
	
	@Override
	public Object invoke(MethodInvocation paramMethodInvocation) throws Throwable {
		Object[] arguments = paramMethodInvocation.getArguments();
		for(Object argument : arguments) {
			if(log.isDebugEnabled() && argument.getClass().isAnnotationPresent(XmlType.class)) {
				log.logXmlTypeObject(argument, "Request");
			}
		}
		Object response = paramMethodInvocation.proceed();
		if(log.isDebugEnabled() && response.getClass().isAnnotationPresent(XmlType.class)) {
			log.logXmlTypeObject(response, "Response");
		}
		return response;
	}

	@Override
	public void setAdvisedClass(Class<?> cls) {
		// Utilizzo la classe che mi e' fornita per inizializzare il logger
		log = new LogUtil(cls);
	}

}
