/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * Classe di base per le varie Factory del modulo BIL.
 * <br>
 * Espone i metodi canonici e comuni a varie Factory.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/03/2014
 *
 */
public abstract class BaseFactory {
	
	/** Costruttore vuoto, presente solo per questioni di ereditariet&agrave; */
	protected BaseFactory() {
		// Solo per eventuale ereditarieta'
	}
	
	/**
	 * Capitalizza una Stringa. In particolare, rende l'intera stringa come caratteri minuscoli, e capitalizza quanto ottenuto.
	 * 
	 * @param string la stringa da capitalizzare
	 * @return la stringa capitalizzata
	 */
	protected static String capitaliseString(String string) {
		return StringUtils.capitalize(string.toLowerCase(Locale.ITALIAN));
	}
	
}
