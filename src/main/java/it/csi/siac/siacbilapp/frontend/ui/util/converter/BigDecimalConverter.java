/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.converter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * @deprecated
 * @see it.csi.siac.siaccommon.util.number.NumberUtil
 *
 */
@Deprecated
@SuppressWarnings("rawtypes")
public class BigDecimalConverter extends StrutsTypeConverter {

	@Override
	public Object convertFromString(Map context, String[] values, Class clazz) {
		if(values.length != 1) {
			// Nel caso in cui non vi sia esattamente un parametro da convertire, si effettui il fallback
			super.performFallbackConversion(context, values, clazz);
		}
		
		LogUtil log = new LogUtil(getClass());
		final String methodName = "convertFromString";
		
		String str = values[0];
		
		BigDecimal result = null;
		try {
			if(str.contains(",")) {
				// Sono nel caso di una formulazione numerica con locale italiano
				result = FormatUtils.parseCurrency(str);
			} else {
				// Non ho un locale ben definito (probabilmente sono in un locale inglese). Converto in maniera standard
				result = new BigDecimal(str);
			}
		} catch (ParseException e) {
			log.info(methodName, "Conversione in java.math.BigDecimal fallita causa ParseException: " + e.getMessage());
		} catch (NumberFormatException e) {
			log.info(methodName, "Conversione in java.math.BigDecimal fallita causa NumberFormatException: " + e.getMessage());
		} catch (NullPointerException e) {
			log.info(methodName, "Conversione in java.math.BigDecimal fallita causa NullPointerException: " + e.getMessage());
		}
		// Mangio l'eccezione e continuo
		return result;
	}

	@Override
	public String convertToString(Map context, Object value) {
		try {
			return FormatUtils.formatCurrency((BigDecimal)value);
		} catch(IllegalArgumentException e) {
			/* Non ho grosse necessita' di implementare il catch: va bene anche il caso in cui non possa fare nulla */
			return null;
		}
	}
	
}
