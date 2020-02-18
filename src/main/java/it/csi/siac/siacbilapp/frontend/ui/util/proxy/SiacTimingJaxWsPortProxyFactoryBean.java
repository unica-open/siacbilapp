/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.proxy;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Factory bean per il proxy verso JAX-WS.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/12/2014
 * @see JaxWsPortProxyFactoryBean
 *
 */
public class SiacTimingJaxWsPortProxyFactoryBean extends JaxWsPortProxyFactoryBean {
	
	private LogUtil log;
	
	@Override
	public void prepare() {
		super.prepare();
		log = new LogUtil(getServiceInterface());
	}
	
	@Override
	public Object doInvoke(MethodInvocation invocation) throws Throwable {
		final long initTime = System.currentTimeMillis();
		try {
			return super.doInvoke(invocation);
		} finally {
			final long finalTime = System.currentTimeMillis();
			if(log.isDebugEnabled()) {
				final String methodName = invocation.getMethod().getName();
				log.debug(methodName, "Elapsed time for invocation: " + (finalTime - initTime) + "ms");
			}
		}
	}
	
}
