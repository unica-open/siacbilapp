/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.exception;

import it.csi.siac.siaccommonapp.util.exception.FrontEndCheckedException;

/**
 * Eccezione rilanciata nel caso di errore di business critico del front-end.
 */
public class FrontEndBusinessException extends FrontEndCheckedException {

	private static final long serialVersionUID = 8700411154747682976L;

	/**
	 * @see Exception#Exception()
	 */
	public FrontEndBusinessException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public FrontEndBusinessException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public FrontEndBusinessException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public FrontEndBusinessException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
