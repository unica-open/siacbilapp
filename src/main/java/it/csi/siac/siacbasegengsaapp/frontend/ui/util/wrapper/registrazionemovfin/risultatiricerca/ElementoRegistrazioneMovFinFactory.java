/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.risultatiricerca;

import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Factory per il wrapper della registrazione movfin.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/01/2016
 *
 */
public final class ElementoRegistrazioneMovFinFactory {
	
	/** Private constructor */
	private ElementoRegistrazioneMovFinFactory() {
	}

	/**
	 * Ottiene l'istanza di tipo corretto.
	 * 
	 * @param registrazioneMovFin la registrazione da wrappare
	 * @return il wrapper
	 */
	public static ElementoRegistrazioneMovFin getInstance(RegistrazioneMovFin registrazioneMovFin) {
		// TODO
		return new ElementoRegistrazioneMovFin(registrazioneMovFin);
	}
	
}
