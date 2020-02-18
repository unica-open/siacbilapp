/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.interceptor;

import java.lang.reflect.InvocationTargetException;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.interceptor.PrefixMethodInvocationUtil;
import com.opensymphony.xwork2.validator.ActionValidatorManager;
import com.opensymphony.xwork2.validator.ValidationInterceptor;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;

/**
 * Modified interceptor to handle validation.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 04/11/2014
 * @see ValidationInterceptor
 */
public class ValidationSIACInterceptor extends MethodFilterInterceptor {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7130958003704181828L;
	
	private boolean validateAnnotatedMethodOnly;
	private boolean alwaysInvokeValidate;
	private boolean programmatic;
	private boolean declarative;
	private transient ActionValidatorManager actionValidatorManager;
	
	/** Default empty constructor */
	public ValidationSIACInterceptor() {
		this.alwaysInvokeValidate = true;
		this.programmatic = true;
		this.declarative = true;
	}
	
	/**
	 * @param actionValidatorManager the actionValidatorManager to set
	 */
	@Inject
	public void setActionValidatorManager(ActionValidatorManager actionValidatorManager) {
		this.actionValidatorManager = actionValidatorManager;
	}
	
	/**
	 * @return the validateAnnotatedMethodOnly
	 */
	public boolean isValidateAnnotatedMethodOnly() {
		return validateAnnotatedMethodOnly;
	}

	/**
	 * @param validateAnnotatedMethodOnly the validateAnnotatedMethodOnly to set
	 */
	public void setValidateAnnotatedMethodOnly(boolean validateAnnotatedMethodOnly) {
		this.validateAnnotatedMethodOnly = validateAnnotatedMethodOnly;
	}

	/**
	 * @param alwaysInvokeValidate the alwaysInvokeValidate to set
	 */
	public void setAlwaysInvokeValidate(boolean alwaysInvokeValidate) {
		this.alwaysInvokeValidate = alwaysInvokeValidate;
	}

	/**
	 * @param programmatic the programmatic to set
	 */
	public void setProgrammatic(boolean programmatic) {
		this.programmatic = programmatic;
	}

	/**
	 * @param declarative the declarative to set
	 */
	public void setDeclarative(boolean declarative) {
		this.declarative = declarative;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		final String methodName = "doIntercept";
		final LogUtil logUtil = new LogUtil(invocation.getAction().getClass());
		
		try {
			doBeforeInvocation(invocation, logUtil);
		} catch(ParamValidationException pve) {
			// The framework invokes the validate() method directly, so we shall (eventually) get a ParamValidationException
			if(logUtil.isInfoEnabled()) {
				logUtil.info(methodName, "Validation exception: " + pve.getMessage());
			}
		} catch (InvocationTargetException ite) {
			// The framework invokes the validate+, validateDo+ methods via reflection, so we shall (eventually) get an InvocationTargetException
			if(ite != null && ite.getCause() instanceof ParamValidationException) {
				// We have a validation exception. We shall not unroll it (quite useless), but we log it, and keep going with the invocation
				if(logUtil.isInfoEnabled()) {
					logUtil.info(methodName, "Validation exception: " + ite.getCause().getMessage());
				}
			} else {
				// We had a different kind of Exception. As we do not know how to handle it, it'll be re-thrown
				throw ite;
			}
		}
		
		if(invocation.getAction() instanceof GenericBilancioAction && logUtil.isDebugEnabled() && ((GenericBilancioAction) invocation.getAction()).hasErrori()) {
			// Log for the eventual number of errors
			logUtil.debug(methodName, "There are " + ((GenericBilancioAction) invocation.getAction()).getModel().getErrori().size() + " validation errors for the action");
		}
		
		return invocation.invoke();
	}
	
	/**
	 * Before the invocation of the action.
	 * 
	 * @param invocation the invocation of the action
	 * @param logUtil    the logger for the action
	 * @throws Exception in case of an exception
	 */
	private void doBeforeInvocation(ActionInvocation invocation, LogUtil logUtil) throws Exception {
		final String methodName = "doBeforeInvocation";
		Object action = invocation.getAction();
		ActionProxy proxy = invocation.getProxy();
		String context = proxy.getActionName();
		String method = proxy.getMethod();
		
		if (logUtil.isDebugEnabled()) {
			logUtil.debug(methodName, "Validating " + invocation.getProxy().getNamespace() + "/" + invocation.getProxy().getActionName() + " with method " + method + ".");
		}
		if (declarative) {
			if (validateAnnotatedMethodOnly) {
				actionValidatorManager.validate(action, context, method);
			} else {
				actionValidatorManager.validate(action, context);
			}
		}
		if ((!(action instanceof Validateable)) || (!(programmatic))) {
			return;
		}
		Exception exception = null;
		Validateable validateable = (Validateable)action;
		if (logUtil.isDebugEnabled()) {
			logUtil.debug(methodName, "Invoking validate() on action " + validateable);
		}
		try {
			PrefixMethodInvocationUtil.invokePrefixMethod(invocation, new String[] { "validate", "validateDo" });
		} catch (Exception e) {
			exception = e;
		}
		if (alwaysInvokeValidate) {
			validateable.validate();
		}
		if (exception == null) {
			return;
		}
		throw exception;
	}
	
}
