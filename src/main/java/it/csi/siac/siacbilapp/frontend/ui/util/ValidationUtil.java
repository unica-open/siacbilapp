/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommon.util.DataValidator;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfinser.model.MovimentoGestione;

/**
 * Classe di utilit&agrave; per la validazione delle Actions.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 16/07/2013
 *
 */
public final class ValidationUtil {
	
	/** Non instanziare la classe */
	private ValidationUtil() {
	}
	
	
	/**
	 * Controlla se la data fornita in input sia all'interno del range fornito.
	 * 
	 * @param testDate  la data da controllare
	 * @param startDate l'inizio del range
	 * @param endDate   la fine del range
	 * 
	 * @return <code>true</code> se la data &eacute; nel range fornito; <code>false</code> in caso contrario
	 * 
	 */
	public static boolean checkDateInRange(Date testDate, Date startDate, Date endDate) {
		return !(testDate.before(startDate) || testDate.after(endDate));
	}
	
	/**
	 * Controlla se la data fornita in input sia all'interno dell'anno fornito.
	 * <br>
	 * <code>null</code>-safe.
	 * 
	 * @param dataCompetenza la data da controllare
	 * @param year           l'anno
	 * 
	 * @return <code>true</code> se la data &eacute; nell'anno fornito; <code>false</code> in caso contrario
	 * 
	 */
	public static boolean checkDateInYear(Date dataCompetenza, Integer year) {
		// Se non ho la data, essa è trivialmente all'interno dell'anno
		// Se non ho l'anno, allora la data è trivialmente valida
		if(dataCompetenza == null || year == null) {
			return true;
		}
		Date startDate = new GregorianCalendar(year.intValue(), Calendar.JANUARY, 1).getTime();
		Date endDate = new GregorianCalendar(year.intValue(), Calendar.DECEMBER, 31).getTime();
		return ValidationUtil.checkDateInRange(dataCompetenza, startDate, endDate);
	}
	
	/**
	 * Controlla che l'anno della data fornita sia pari o superiore all'anno fornito.
	 * 
	 * @param dateToCheck la data da controllare
	 * @param year        l'anno rispetto a cui fare il controllo
	 * 
	 * @return <code>true</code> se la data dell'anno &eacute; pari o superiore alla fornita; <code>false</code> altrimenti.
	 */
	public static boolean checkDateSameOrGreaterYear(Date dateToCheck, Integer year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateToCheck);
		int yearDate = cal.get(Calendar.YEAR);
		return yearDate >= year.intValue();
	}
	
	/**
	 * Controlla che l'anno della data fornita sia pari o inferiore all'anno fornito.
	 * 
	 * @param dateToCheck la data da controllare
	 * @param year        l'anno rispetto a cui fare il controllo
	 * 
	 * @return <code>true</code> se la data dell'anno &eacute; pari o inferiore alla fornita; <code>false</code> altrimenti.
	 */
	public static boolean checkDateSameOrPreviousYear(Date dateToCheck, Integer year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateToCheck);
		int yearDate = cal.get(Calendar.YEAR);
		return yearDate <= year.intValue();
	}
	
	/**
	 * Controlla se l'entita sia valida per l'anno fornito. Il controllo da effettuare &eacute;:
	 * <ul>
	 *     <li>Data cancellazione = <code>null</code></li>
	 *     <li>Data inizio validit&agrave; &lt;= anno</li>
	 *     <li>Data fine validit&agrave; = <code>null</code> o &gt;= anno</li>
	 * </ul>
	 * 
	 * @param entita l'entita da controllare
	 * @param year   l'anno
	 * 
	 * @return <code>true</code> se l'entita &eacute; valida; <code>false</code> altrimenti
	 */
	public static boolean isEntitaValidaPerAnnoEsercizio(Entita entita, Integer year) {
		return year != null && entita != null
				&& entita.getDataCancellazione() == null
				&& (entita.getDataInizioValidita() != null && checkDateSameOrPreviousYear(entita.getDataInizioValidita(), year))
				&& (entita.getDataFineValidita() == null || checkDateSameOrGreaterYear(entita.getDataFineValidita(), year));
	}
	
	/**
	 * Controlla che il dato codice fiscale sia valido anche nel caso in cui esso sia temporaneo.
	 * 
	 * @param codiceFiscale il codice fiscale da validare
	 * 
	 * @return <code>true</code> se il codice fiscale &eacute; valido; <code>false</code> in caso contrario
	 */
	public static boolean isValidCodiceFiscaleEvenTemporary(String codiceFiscale) {
		return isValidCodiceFiscale(StringUtils.upperCase(codiceFiscale)) || isValidCodiceFiscaleTemporaneo(StringUtils.upperCase(codiceFiscale));
	}
	
	/**
	 * Controlla se il codice fiscale sia valido.
	 * 
	 * @param codiceFiscale il codice fiscale da controllare
	 * 
	 * @return <code>true</code> se il codice fiscale &eacute; valido; <code>false</code> in caso contrario
	 */
	public static boolean isValidCodiceFiscale(String codiceFiscale) {
		return DataValidator.isValidCodiceFiscale(StringUtils.upperCase(codiceFiscale));
	}
	
	/**
	 * Controlla se la partita iva sia valida.
	 * 
	 * @param partitaIva la partita iva da controllare
	 * 
	 * @return <code>true</code> se la partita iva &eacute; valida; <code>false</code> in caso contrario
	 */
	public static boolean isValidPartitaIva(String partitaIva) {
		// Il controllo e' il medesimo del codice fiscale temporaneo
		return isValidCodiceFiscaleTemporaneo(StringUtils.upperCase(partitaIva));
	}
	
	/**
	 * Controlla se il codice fiscale temporaneo sia valido.
	 * 
	 * @param codiceFiscale il codice fiscale da controllare
	 * 
	 * @return <code>true</code> se il codice fiscale &eacute; valido; <code>false</code> in caso contrario
	 */
	private static boolean isValidCodiceFiscaleTemporaneo(String codiceFiscale) {
		return StringUtils.isBlank(codiceFiscale)
				|| DataValidator.isValidMask(codiceFiscale, "^\\d{11}$")
				&& isValidControlCharCodiceFiscaleTemporaneo(codiceFiscale);
	}

	/**
	 * Controlla se il carattere di controllo del codice fiscale temporaneo sia valido.
	 * 
	 * @param codiceFiscale il codice fiscale da controllare
	 * 
	 * @return <code>true</code> se il codice fiscale &eacute; valido; <code>false</code> in caso contrario
	 */
	private static boolean isValidControlCharCodiceFiscaleTemporaneo(String codiceFiscale) {
		int carattereControllo = 10;
		return codiceFiscale.charAt(carattereControllo) == calcControlCharCodiceFiscale(StringUtils.substring(codiceFiscale, 0, carattereControllo));
	}
	
	/**
	 * Calcola il carattere di controllo del codice fiscale.
	 * 
	 * @param string il codice fiscale senza l'ultimo carattere
	 * 
	 * @return il carattere di controllo
	 * 
	 * @see <a href="http://www.agenziaentrate.gov.it/wps/content/Nsilib/Nsi/Home/CosaDeviFare/Richiedere/Codice+fiscale+e+tessera+sanitaria/Richiesta+TS_CF/SchedaI/Informazioni+codificazione+pf/">Agenzia delle Entrate</a>
	 */
	private static char calcControlCharCodiceFiscale(String string) {
		int sum = 0;

		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			int x = Character.getNumericValue(c);
			sum += ((i + 1) % 2 == 0 ? toSingleDigit(2 * x) : x);
		}
		
		int result = 10 - (sum % 10);
		
		return Integer.toString(result % 10).charAt(0);
	}

	/**
	 * Reduces a number to a single-digit one.
	 * 
	 * @param i the number to reduce
	 * 
	 * @return the reduced number
	 */
	private static int toSingleDigit(int i) {
		if(i < 0) {
			return toSingleDigit(-i);
		}
		if(i < 10) {
			return i;
		}
		
		int result = 0;
		int temp = i;
		while(temp > 0) {
			result += temp % 10;
			temp /= 10;
		}
		
		return toSingleDigit(result);
	}

	/**
	 * Controlla se il movimento di gestione sia valido.
	 * 
	 * @param movimentoGestioneSession il movimento di gestione della sessione
	 * @param movimentoGestioneModel   il movimento di gestione del model
	 * 
	 * @return <code>true</code> se il movimento &eacute; valido per la ricerca; <code>false</code> altrimenti
	 */
	public static boolean isValidMovimentoGestioneFromSession(MovimentoGestione movimentoGestioneSession, MovimentoGestione movimentoGestioneModel) {
		return movimentoGestioneSession != null && movimentoGestioneModel != null
				&& movimentoGestioneSession.getNumero() != null && movimentoGestioneModel.getNumero() != null
				&& movimentoGestioneSession.getAnnoMovimento() == movimentoGestioneModel.getAnnoMovimento()
				&& movimentoGestioneSession.getNumero().compareTo(movimentoGestioneModel.getNumero()) == 0;
	}
	
	
	
	/**
	 * Reduces a number to a single-digit one.
	 * 
	 * @param value the number to reduce
	 * 
	 * @return the reduced number
	 */
	public static boolean isIntegerValid(Integer value) {
		return value != null && value.intValue() != 0;
	}
	
}
