/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.listener;

import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.ConversionErrorInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

/**
 * Listener per l'impostazione degli errori di conversione all'interno dello stack prima della generazione del risultato.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 19/08/2013
 * @see ConversionErrorInterceptor
 *
 */
public class ConversionPreResultListener implements PreResultListener {

	private Map<Object, Object> fakie;

	/** Costruttore vuoto di default */
	public ConversionPreResultListener() {
		this(null);
	}

	/**
	 * Costruttore a partire dalla mappa di errori di conversione.
	 * 
	 * @param fakie the fakie to set
	 */
	public ConversionPreResultListener(Map<Object, Object> fakie) {
		this.fakie = fakie;
	}

	@Override
	public void beforeResult(ActionInvocation invocation, String resultCode) {
		if (fakie != null) {
			/* Inposta gli errori di conversione nello stack */
			invocation.getStack().setExprOverrides(fakie);
		}
	}

}
