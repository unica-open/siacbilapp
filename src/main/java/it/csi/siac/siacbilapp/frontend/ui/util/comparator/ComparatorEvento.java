/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siacgenser.model.Evento;

/**
 * Classe di comparazione tra tra aliquoteSubdocumentoIva a partire dal codice dell'aliquotaIva associata.
 * 
 * @author Nazha Ahmad
 * @version 1.0.0
 */
public final class ComparatorEvento implements Comparator<Evento>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7989640425803703285L;
	
	/** The only accessible instance */
	public static final ComparatorEvento INSTANCE = new ComparatorEvento();
	
	/** Private constructor */
	private ComparatorEvento() {
		super();
	}

	@Override
	public int compare(Evento o1, Evento o2) {
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
		// Codice
		if(o1.getCodice() == null && o2.getCodice() == null) {
			return 0;
		}
		if(o1.getCodice() == null) {
			return -1;
		}
		if(o2.getCodice() == null) {
			return 1;
		}
		return o1.getCodice().compareToIgnoreCase(o2.getCodice());
	}
	
}
