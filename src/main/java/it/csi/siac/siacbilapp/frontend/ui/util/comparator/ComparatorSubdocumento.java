/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siacfin2ser.model.Subdocumento;

/**
 * Classe di comparazione tra Classificazioni Gerarchiche via il campo ordine.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 */
public final class ComparatorSubdocumento implements Comparator<Subdocumento<?, ?>>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6901341292042716902L;
	/** The only accessible instance */
	public static final ComparatorSubdocumento INSTANCE = new ComparatorSubdocumento();

	/** Costruttore vuoto di default */
	private ComparatorSubdocumento() {
		super();
	}
	
	@Override
	public int compare(Subdocumento<?, ?> o1, Subdocumento<?, ?> o2) {
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
		if(o1.getNumero() == null && o2.getNumero() == null) {
			return 0;
		}
		if(o1.getNumero() == null) {
			return -1;
		}
		if(o2.getNumero() == null) {
			return 1;
		}
		return o1.getNumero().compareTo(o2.getNumero());
	}
	
}
