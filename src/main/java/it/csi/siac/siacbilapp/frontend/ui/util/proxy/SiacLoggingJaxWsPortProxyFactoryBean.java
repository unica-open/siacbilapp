/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.proxy;

import javax.xml.bind.annotation.XmlType;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;

import it.csi.siac.siaccommonapp.util.log.LogWebUtil;

/**
 * Factory bean per il proxy verso JAX-WS.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/12/2014
 * @see JaxWsPortProxyFactoryBean
 *
 */
@Deprecated
public class SiacLoggingJaxWsPortProxyFactoryBean extends JaxWsPortProxyFactoryBean {
	
	private LogWebUtil log;
	
	@Override
	public void prepare() {
		super.prepare();
		log = new LogWebUtil(getServiceInterface());
	}
	
	@Override
	public Object doInvoke(MethodInvocation invocation) throws Throwable {
		Object[] arguments = invocation.getArguments();
		for(Object argument : arguments) {
			if(argument.getClass().isAnnotationPresent(XmlType.class)) {
				log.logXmlTypeObject(argument, "Request");
			}
		}
		Object response = super.doInvoke(invocation);
		if(response.getClass().isAnnotationPresent(XmlType.class)) {
			log.logXmlTypeObject(response, "Response");
		}
		return response;
	}
	
}
