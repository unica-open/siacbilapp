/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.exception;

import it.csi.siac.siaccommonapp.util.exception.FrontEndCheckedException;

/**
 * Eccezione lanciata nel caso in cui l'utente non sia abilitato all'esecuzione dell'azione
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/05/2015
 *
 */
public class UnauthorizedUserException extends FrontEndCheckedException {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5498629646952666659L;

	/**
	 * @see Exception#Exception()
	 */
	public UnauthorizedUserException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public UnauthorizedUserException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public UnauthorizedUserException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public UnauthorizedUserException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
