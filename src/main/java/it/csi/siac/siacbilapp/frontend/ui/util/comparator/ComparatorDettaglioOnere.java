/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siacfin2ser.model.DettaglioOnere;

/**
 * Classe di comparazione tra DettaglioOnere via il campo codice.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 */
public final class ComparatorDettaglioOnere implements Comparator<DettaglioOnere>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7989640425803703285L;
	
	/** The instance to be accessed from outside */
	public static final ComparatorDettaglioOnere INSTANCE = new ComparatorDettaglioOnere();

	/** Costruttore vuoto di default */
	private ComparatorDettaglioOnere() {
		super();
	}
	
	@Override
	public int compare(DettaglioOnere o1, DettaglioOnere o2) {
		if(o1 == null && o2 == null) {
			return 0;
		}
		if(o1 == null) {
			return -1;
		}
		if(o2 == null) {
			return 1;
		}
		if(o1.equals(o2)) {
			return 0;
		}
		// TipoOnere
		if(o1.getTipoOnere() == null && o2.getTipoOnere() == null) {
			return 0;
		}
		if(o1.getTipoOnere() == null) {
			return -1;
		}
		if(o2.getTipoOnere() == null) {
			return 1;
		}
		// Codice
		if(o1.getTipoOnere().getCodice() == null && o2.getTipoOnere().getCodice() == null) {
			return 0;
		}
		if(o1.getTipoOnere().getCodice() == null) {
			return -1;
		}
		if(o2.getTipoOnere().getCodice() == null) {
			return 1;
		}
		return o1.getTipoOnere().getCodice().compareToIgnoreCase(o2.getTipoOnere().getCodice());
	}
	
}
