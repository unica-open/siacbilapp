/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.threadlocal;

import com.opensymphony.xwork2.conversion.impl.XWorkConverter;

/**
 * Thread local per un XWork converter
 * @author Marchino Alessandro
 *
 */
public class XWorkConverterThreadLocal extends ThreadLocal<XWorkConverter> {

	@Override
	protected XWorkConverter initialValue() {
		return new XWorkConverterThreadLocal.Converter();
	}
	
	/**
	 * Converter interno. Serve per esporre l'XWorkConverter, avente un costruttore protected e quindi non istanziabile canonicamente
	 * @author Marchino Alessandro
	 *
	 */
	private static class Converter extends XWorkConverter {
		/** Costruttore vuoto */
		Converter() {
			super();
		}
	}
	
}
