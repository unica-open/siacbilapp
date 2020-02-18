/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.threadlocal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Thread local per il number format
 * @author Marchino Alessandro
 *
 */
public class NumberFormatThreadLocal extends ThreadLocal<NumberFormat> {

	@Override
	protected NumberFormat initialValue() {
		DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(Locale.ITALY);
		df.setParseBigDecimal(true);
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		return df;
	}
	
}
