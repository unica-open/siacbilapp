/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione.step1;

import java.util.Locale;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;

/**
 * Classe di model per la scelta della variazione. Contiene una mappatura del model.
 * 
 * @author Pro-Logic
 * @version 1.0.0 - 09/09/2013
 *
 */
public class SceltaVariazioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3609867205566051651L;

	private Scelta scelta;
	
	/** Costruttore vuoto di default */
	public SceltaVariazioneModel() {
		super();
	}
	
	/**
	 * @return the scelta
	 */
	public Scelta getScelta() {
		return scelta;
	}

	/**
	 * @param scelta the scelta to set
	 */
	public void setScelta(Scelta scelta) {
		this.scelta = scelta;
	}
	
	/**
	 * Metodo di utilit&agrave; per la restituzione di una stringa rappresentante la scelta.
	 * 
	 * @return the sceltaSt
	 */
	public String getSceltaSt() {
		return scelta.toString();
	}

	/**
	 * Metodo di utilit&agrave; per l'impostazione della scelta a partire da una stringa.
	 * 
	 * @param scelta the sceltaSt to set
	 */
	public void setSceltaSt(String scelta) {
		this.scelta = Scelta.valueOf(scelta.toUpperCase(Locale.ITALIAN));
	}
	
	/**
	 * Metodo di utilit&agrave; per controllare se la scelta effettuata sia relativa agli importi.
	 * 
	 * @return the importi
	 */
	public boolean isImporti() {
		return scelta.equals(Scelta.IMPORTI);
	}
	
	/**
	 * Metodo di utilit&agrave; per controllare se la scelta effettuata sia relativa alle codifiche.
	 * 
	 * @return the codifiche
	 */
	public boolean isCodifiche() {
		return scelta.equals(Scelta.CODIFICHE);
	}
	
}
