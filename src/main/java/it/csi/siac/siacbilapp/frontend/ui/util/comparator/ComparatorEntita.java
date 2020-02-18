/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import it.csi.siac.siaccorser.model.Entita;

/**
 * Classe di comparazione tra Entit&agrave; via il campo codice.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 */
public final class ComparatorEntita implements Comparator<Entita>, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7836999775212659631L;
	
	/** The only accessible instance */
	public static final ComparatorEntita INSTANCE = new ComparatorEntita();

	/** Costruttore vuoto di default */
	private ComparatorEntita() {
		super();
	}

	@Override
	public int compare(Entita entita1, Entita entita2) {
		if(entita1 == null && entita2 == null) {
			return 0;
		}
		// Check dei valori null. Un valore null e' considerato inferiore di un qualsiasi valore non-null
		if(entita1 == null) {
			return -1;
		}
		if(entita2 == null) {
			return 1;
		}
		if(entita1.equals(entita2)) {
			return 0;
		}
		return new CompareToBuilder()
			.append(entita1.getUid(), entita2.getUid())
			.toComparison();
	}
	
}
