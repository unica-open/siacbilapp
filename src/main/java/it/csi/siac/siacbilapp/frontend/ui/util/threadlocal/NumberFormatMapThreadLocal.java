/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.threadlocal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Thread local per il number format
 * @author Marchino Alessandro
 *
 */
public class NumberFormatMapThreadLocal extends ThreadLocal<Map<Integer, NumberFormat>> {

	@Override
	protected Map<Integer, NumberFormat> initialValue() {
		return new HashMap<Integer, NumberFormat>();
	}
	
	public static NumberFormat getOrInitializeFormatter(ThreadLocal<Map<Integer, NumberFormat>> threadLocal, int decimalPlaces) {
		NumberFormat nf = threadLocal.get().get(decimalPlaces);
		if(nf == null) {
			DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(Locale.ITALY);
			df.setParseBigDecimal(true);
			df.setMinimumFractionDigits(decimalPlaces);
			df.setMaximumFractionDigits(decimalPlaces);
			
			nf = df;
			threadLocal.get().put(decimalPlaces, nf);
		}
		return nf;
	}
	
}
