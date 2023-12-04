/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.format;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.threadlocal.DateFormatMapThreadLocal;
import it.csi.siac.siacbilapp.frontend.ui.util.threadlocal.NumberFormatMapThreadLocal;
import it.csi.siac.siaccommon.util.threadlocal.ThreadLocalUtil;
import it.csi.siac.siaccorser.model.Codifica;

/**
 * Classe di utilit&agrave; per le varie formattazioni.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/05/2015
 *
 */
public final class FormatUtils {
	
	// Standard formatters
	private static final ThreadLocal<Map<String, DateFormat>> TL_DATE_FORMATS = ThreadLocalUtil.registerThreadLocal(DateFormatMapThreadLocal.class);
	private static final ThreadLocal<Map<Integer, NumberFormat>> TL_NUMBER_FORMATS = ThreadLocalUtil.registerThreadLocal(NumberFormatMapThreadLocal.class);
	
	/** Il contesto di calcolo per i BigDecimal standard. */
	public static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.DOWN);
	/** Il contesto fi calcolo per i BigDecimal, con approssimazione {@link RoundingMode#HALF_UP} */
	public static final MathContext MATH_CONTEXT_HALF_UP = new MathContext(2, RoundingMode.HALF_UP);
	
	/** Do not instantiate */
	private FormatUtils() {
	}
	
	/**
	 * Ottiene il date format per il thread releativo al formato specificato.
	 * 
	 * @param format il formato
	 * @return il date format per il formato
	 */
	private static DateFormat getDateFormat(String format) {
		DateFormat df = TL_DATE_FORMATS.get().get(format);
		if(df == null) {
			df = new SimpleDateFormat(format, Locale.ITALY);
			TL_DATE_FORMATS.get().put(format, df);
		}
		return df;
	}
	
	/**
	 * Formattazione della data nel formato standard italiano <code>dd/MM/yyyy</code>.
	 * 
	 * @param date la data da formattare
	 * 
	 * @return la data formattata
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "dd/MM/yyyy");
	}
	
	
	/**
	 * Formattazione della data nel formato standard italiano con orario <code>dd/MM/yyyy hh:mm</code>.
	 * 
	 * @param date la data da formattare
	 * 
	 * @return la data formattata
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "dd/MM/yyyy HH:mm");
	}
	
	/**
	 * Parsificazione della data per il formato standard italiano <code>dd/MM/yyyy</code>.
	 * 
	 * @param string la string da parsificare
	 * 
	 * @return la data corrispondente alla stringa
	 * 
	 * @throws ParseException in caso di fallimento nella parsificazione
	 */
	public static Date parseDate(String string) throws ParseException {
		return parseDate(string, "dd/MM/yyyy");
	}
	
	/**
	 * Formattazione della data nel formato <code>yyyy</code>.
	 * 
	 * @param date la data da formattare
	 * 
	 * @return la data formattata
	 */
	public static String formatDateYear(Date date) {
		return formatDate(date, "yyyy");
	}
	
	/**
	 * Parsificazione della data per il formato <code>yyyy</code>.
	 * 
	 * @param string la string da parsificare
	 * 
	 * @return la data corrispondente alla stringa
	 * 
	 * @throws ParseException in caso di fallimento nella parsificazione
	 */
	public static Date parseDateYear(String string) throws ParseException {
		return parseDate(string, "yyyy");
	}
	
	/**
	 * Formattazione della data nel formato standard JSON/Javascript.
	 * 
	 * @param date la data da formattare
	 * 
	 * @return la data formattata
	 */
	public static String formatDateTimeJSON(Date date) {
		return formatDate(date, "yyyy-MM-dd'T'HH:mm:ss");
	}
	
	/**
	 * Parsificazione della data per il formato JSON/Javascript.
	 * 
	 * @param string la string da parsificare
	 * 
	 * @return la data corrispondente alla stringa
	 * 
	 * @throws ParseException in caso di fallimento nella parsificazione
	 */
	public static Date parseDateTimeJSON(String string) throws ParseException {
		return parseDate(string, "yyyy-MM-dd'T'HH:mm:ss");
	}
	
	/**
	 * Formattazione della data nel formato richiesto.
	 * 
	 * @param date   la data da formattare
	 * @param format il formato della data
	 * 
	 * @return la data formattata
	 * @throws IllegalArgumentException se il formato non &eacute; compatibilie con quello utilizzato da {@link SimpleDateFormat}.
	 */
	public static String formatDate(Date date, String format) {
		if(date == null || StringUtils.isBlank(format)) {
			return "";
		}
		return getDateFormat(format).format(date);
	}
	
	/**
	 * Parsificazione della data per il formato standard italiano <code>dd/MM/yyyy</code>.
	 * 
	 * @param string la string da parsificare
	 * @param format il formato della data
	 * 
	 * @return la data corrispondente alla stringa
	 * 
	 * @throws ParseException in caso di fallimento nella parsificazione
	 */
	public static Date parseDate(String string, String format) throws ParseException {
		if(StringUtils.isBlank(string) || StringUtils.isBlank(format)) {
			return null;
		}
		return getDateFormat(format).parse(string);
	}
	
	/**
	 * Formattazione del bigdecimal come stringa piana.
	 * @param value il valore da formattare
	 * @return il valore formattato
	 */
	public static String formatPlain(BigDecimal value) {
		if(value == null) {
			return "";
		}
		return value.toPlainString();
	}
	
	/**
	 * Formattazione del bigdecimal come importo con locale italiano.
	 * 
	 * @param value il valore da formattare
	 * 
	 * @return il valore formattato
	 */
	public static String formatCurrency(BigDecimal value) {
		return formatNumber(value, 2);
	}
	
	/**
	 * Parsificazione dell'importo con locale italiano in BigDecimal.
	 * 
	 * @param string il valore da parsificare
	 * 
	 * @return l'importo
	 * 
	 * @throws ParseException in caso di fallimento nella parsificazione
	 */
	public static BigDecimal parseCurrency(String string) throws ParseException {
		return parseNumber(string, 2);
	}
	
	/**
	 * Formattazione del bigdecimal come importo con locale italiano.
	 * 
	 * @param value il valore da formattare
	 * @param decimalPlaces il numero di decimali
	 * @return il valore formattato
	 */
	public static String formatNumber(BigDecimal value, int decimalPlaces) {
		if(value == null) {
			return "";
		}
		NumberFormat nf = NumberFormatMapThreadLocal.getOrInitializeFormatter(TL_NUMBER_FORMATS, decimalPlaces);
		return nf.format(value);
	}
	
	/**
	 * Parsificazione dell'importo con locale italiano in BigDecimal.
	 * 
	 * @param string il valore da parsificare
	 * @param decimalPlaces il numero di decimali
	 * @return l'importo
	 * 
	 * @throws ParseException in caso di fallimento nella parsificazione
	 */
	public static BigDecimal parseNumber(String string, int decimalPlaces) throws ParseException {
		if(StringUtils.isBlank(string)) {
			return null;
		}
		NumberFormat nf = NumberFormatMapThreadLocal.getOrInitializeFormatter(TL_NUMBER_FORMATS, decimalPlaces);
		return (BigDecimal) nf.parse(string);
	}
	
	/**
	 * Formattazione del valore booleano.
	 * 
	 * @param value       il booleano da formattare
	 * @param trueString  la stringa da restituire nel caso in cui il booleano sia <code>true</code>
	 * @param falseString la stringa da restituire nel caso in cui il booleano sia <code>false</code>
	 * @return la stringa corrispondente al booleano
	 */
	public static String formatBoolean(boolean value, String trueString, String falseString) {
		return value ? trueString : falseString;
	}
	
	/**
	 * Formattazione del valore booleano.
	 * 
	 * @param value       il booleano da formattare
	 * @param trueString  la stringa da restituire nel caso in cui il booleano sia <code>true</code>
	 * @param falseString la stringa da restituire nel caso in cui il booleano sia <code>false</code>
	 * @param nullString la stringa da restituire nel caso in cui il booleano sia <code>null</code>
	 * @return la stringa corrispondente al booleano
	 */
	public static String formatBoolean(Boolean value, String trueString, String falseString, String nullString) {
		return value == null
				? nullString
				: Boolean.TRUE.equals(value)
					? trueString
					: falseString;
	}
	
	/**
	 * Formattazione del valore booleano, possibilmente null.
	 * 
	 * @param value       il booleano da formattare
	 * @param trueString  la stringa da restituire nel caso in cui il booleano sia <code>true</code>
	 * @param falseString la stringa da restituire nel caso in cui il booleano sia <code>false</code>
	 * @return la stringa corrispondente al booleano
	 */
	public static String formatNullableBoolean(Boolean value, String trueString, String falseString) {
		return formatBoolean(value, trueString, falseString, null);
	}
	
	/**
	 * Parsificazione in boolean
	 * 
	 * @param value       la stringa da formattare in boolean
	 * @param trueString  la stringa da restituire nel caso in cui il booleano sia <code>true</code>
	 * @param falseString la stringa da restituire nel caso in cui il booleano sia <code>false</code>
	 * @return il booleano corrispondente alla stringa
	 */
	public static Boolean parseBoolean(String value, String trueString, String falseString) {
		return trueString.equals(value)
			? Boolean.TRUE
			: falseString.equals(value)
				? Boolean.FALSE
				: null;
	}
	
	/**
	 * Parsificazione in boolean dei valori "S", "N"
	 * 
	 * @param value       la stringa da formattare in boolean
	 * @return il booleano corrispondente alla stringa
	 */
	public static Boolean parseBooleanSN(String value) {
		return parseBoolean(value, "S", "N");
	}
	
	/**
	 * Calcola codice e descrizione della codifica se presente
	 * @param codifica la codifica per cui estrarre codice e descrcizione
	 * @return codice e descrizione della codifica
	 */
	public static String formatCodificaCodiceDescrizione(Codifica codifica) {
		if(codifica == null) {
			return "";
		}
		return new StringBuilder().append(StringUtils.defaultString(codifica.getCodice(), "")).append(" - ").append(StringUtils.defaultString(codifica.getDescrizione(), "")).toString();
	}
	
	/**
	 * Formattazione di una stringa per l'utilizzo in un attributo HTML
	 * @param value la stringa da formattare
	 * @return la stringa formattata
	 */
	public static String formatHtmlAttributeString(String value) {
		return StringEscapeUtils.escapeHtml4(value);
	}
	
	
}
