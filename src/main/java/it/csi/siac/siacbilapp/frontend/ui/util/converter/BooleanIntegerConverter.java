/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.converter;

/**
 * Converter tra boolean e integer
 * @author Marchino Alessandro
 *
 */
public final class BooleanIntegerConverter {

	/** Costruttore privato per non permettere istanziazione */
	private BooleanIntegerConverter() {
		// Non instanziare
	}
	
	/**
	 * Da int a boolean
	 * <br/>
	 * Qualsiasi valore differente da 0 &eacute; true
	 * @param value l'intero da convertire
	 * @return se il numero sia differente da zero
	 */
	public static boolean toBoolean(int value) {
		return value != 0;
	}
	
	/**
	 * Da boolean a int.
	 * <br/>
	 * Il valore true viene tradotto in 1, false in 0
	 * @param value il boolean da convertire
	 * @return 1 o 0
	 */
	public static int toInteger(boolean value) {
		return value ? 1 : 0;
	}
	
	/**
	 * Da Integer a Boolean
	 * <br/>
	 * Qualsiasi valore differente da 0 &eacute; true
	 * @param value l'intero da convertire
	 * @return se il numero sia differente da zero
	 */
	public static Boolean toBoolean(Integer value) {
		return value != null ? Boolean.valueOf(toBoolean(value.intValue())) : null;
	}
	
	/**
	 * Da Boolean a Integer.
	 * <br/>
	 * Il valore true viene tradotto in 1, false in 0
	 * @param value il boolean da convertire
	 * @return 1 o 0
	 */
	public static Integer toInteger(Boolean value) {
		return value != null ? Integer.valueOf(toInteger(value.booleanValue())) : null;
	}
}
