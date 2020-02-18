/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.advice;

import org.aopalliance.intercept.MethodInvocation;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Advice per la temporizzazione dell'invocazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 25/08/2015
 */
public class TimingAdvice implements ClassAwareMethodInterceptor {

	private static final LogUtil LOG = new LogUtil(TimingAdvice.class);
	private Class<?> advisedClass;
	
	@Override
	public Object invoke(MethodInvocation paramMethodInvocation) throws Throwable {
		long time = System.currentTimeMillis();
		try {
			return paramMethodInvocation.proceed();
		} finally {
			time = System.currentTimeMillis() - time;
			if(LOG.isDebugEnabled()) {
				LOG.debug(advisedClass.getSimpleName() + ":" + paramMethodInvocation.getMethod().getName(), "Elapsed time: " + time + " ms");
			}
		}
	}

	@Override
	public void setAdvisedClass(Class<?> cls) {
		this.advisedClass = cls;
	}

}