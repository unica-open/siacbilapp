/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.exception;

/**
 * Eccezione di esecuzione di un servizio cached.
 * 
 * @author Domenico
 * @version 1.0.0 - 01/10/2014
 *
 */
public class CachedServiceExecutorException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3619650588084845515L;
	
	/**
	 * Instantiates a new cached service executor exception.
	 */
	public CachedServiceExecutorException() {
		super();
	}

	/**
	 * Instantiates a new cached service executor exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public CachedServiceExecutorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new cached service executor exception.
	 *
	 * @param message the message
	 */
	public CachedServiceExecutorException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new cached service executor exception.
	 *
	 * @param cause the cause
	 */
	public CachedServiceExecutorException(Throwable cause) {
		super(cause);
	}
	
}
