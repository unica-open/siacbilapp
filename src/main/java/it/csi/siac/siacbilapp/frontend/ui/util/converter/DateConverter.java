/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.converter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;

/**
 * Classe di conversione per i {@link Date} da e verso {@link String}.
 * 
 * @author Marchino Alessandro
 *
 */
@SuppressWarnings("rawtypes")
public class DateConverter extends StrutsTypeConverter {

	@Override
	public Object convertFromString(Map context, String[] values, Class clazz) {
		
		if(values.length != 1) {
			// Nel caso in cui non vi sia esattamente un parametro da convertire, si effettui il fallback
			super.performFallbackConversion(context, values, clazz);
		}
		
		String str = values[0];
		// Fallback nel caso in cui non stia passando alcuna data
		if("".equalsIgnoreCase(str)) {
			return null;
		}
		
		// Provo a parsificare
		Date result = null;
		try {
			result = FormatUtils.parseDate(str);
		} catch (ParseException e) {
			// Riprovo per una data strana, con formato yyyy-MM-dd'T'HH:mm:ss (causa: JavaScript e JSON)
			try {
				result = FormatUtils.parseDateTimeJSON(str);
			} catch(ParseException ex) {
				throw new TypeConversionException("Conversione in java.util.Date fallita", ex);
			}
		}
		Date dataDel1900 = new GregorianCalendar(1900, Calendar.DECEMBER, 31).getTime();
		if(!result.after(dataDel1900)) {
			throw new TypeConversionException("Conversione in java.util.Date fallita");
		}
		return result;
	}

	@Override
	public String convertToString(Map context, Object value) {
		try {
			return FormatUtils.formatDate((Date)value);
		} catch(IllegalArgumentException e) {
			/* Non ho grosse necessita' di implementare il catch: va bene anche il caso in cui non possa fare nulla */
			return null;
		}
	}
	
}
