/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers;

/**
 * Interfaccia definente un wrapper per le classi di model.
 * <br>
 * Espone un unico metodo, {@link #getUid()}, che restituisce l'uid del wrapper in questione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/01/2013
 *
 */
public interface ModelWrapper {
	
	/**
	 * @return the uid
	 */
	int getUid();
}
