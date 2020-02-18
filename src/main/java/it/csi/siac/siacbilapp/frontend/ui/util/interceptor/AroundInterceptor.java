/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Interceptor that exposes two methods around the invocation.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/02/2014
 */
public abstract class AroundInterceptor extends AbstractInterceptor {

	/** For serialization purpose */
	private static final long serialVersionUID = 1197919198907926033L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		beforeIntercept(invocation);
		String result = null;
		try {
			result = invocation.invoke();
		} catch(Exception e) {
			catchInterceptor(invocation, e);
		} finally {
			finallyIntercept(invocation, result);
		}
		afterInterceptor(invocation, result);
		return result;
	}
	
	/**
	 * Method to be executed before the invocation of the following interceptor/action.
	 * 
	 * @param actionInvocation the invocation
	 */
	protected abstract void beforeIntercept(ActionInvocation actionInvocation);
	
	/**
	 * Method to be executed after the invocation of the following interceptor/action.
	 * 
	 * @param actionInvocation the invocation
	 * @param result           the result of the action invocation
	 */
	protected abstract void afterInterceptor(ActionInvocation actionInvocation, String result);
	
	/**
	 * Method to be executed in case of an exception in the invocation of the action.
	 * 
	 * @param actionInvocation the invocation
	 * @param exception        the thrown exception
	 * 
	 * @throws Exception the eventual exception to be thrown
	 */
	protected void catchInterceptor(ActionInvocation actionInvocation, Exception exception) throws Exception {
		throw exception;
	}
	
	/**
	 * Method to be executed in case of an exception in the invocation of the action, as a finally clause.
	 * 
	 * @param actionInvocation the invocation
	 * @param result           the result of the action invocation
	 */
	protected void finallyIntercept(ActionInvocation actionInvocation, String result) {
		/* Empty */
	}

}
