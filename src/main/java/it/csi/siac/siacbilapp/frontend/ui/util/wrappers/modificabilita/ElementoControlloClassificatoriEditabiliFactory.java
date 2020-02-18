/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.modificabilita;

import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;

/**
 * Factory per i wrapper per il controllo dell'editabilit&agrave;.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 08/10/2013
 *
 */
public final class ElementoControlloClassificatoriEditabiliFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoControlloClassificatoriEditabiliFactory() {
	}
	
	/**
	 * Ottiene un'istanza del wrapper.
	 * 
	 * @param response la response da wrappare
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoControlloClassificatoriEditabili getInstance(ControllaClassificatoriModificabiliCapitoloResponse response) {
		ElementoControlloClassificatoriEditabili wrapper = new ElementoControlloClassificatoriEditabili();
		wrapper.setClassificatoriNonModificabili(response.getClassificatoriNonModificabili());
		return wrapper;
	}
	
}
