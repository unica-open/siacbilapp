/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Times the execution of the action.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 21/12/2015
 */
public class TimingInterceptor extends AbstractInterceptor {

	private static final LogUtil LOG = new LogUtil(TimingInterceptor.class);
	/** For serialization purpose */
	private static final long serialVersionUID = -8413034926167900226L;
	
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		// Inserito all'interno del metodo in quanto non utilizzato altrove
		final long initTime = System.currentTimeMillis();
		try{
			return actionInvocation.invoke();
		} finally {
			final long endTime = System.currentTimeMillis();
			if(LOG.isDebugEnabled()) {
				LOG.debug(actionInvocation.getAction().getClass().getSimpleName() + ":" + actionInvocation.getProxy().getMethod(), "Elapsed time: " + (endTime - initTime) + " ms");
			}
		}
	}

}