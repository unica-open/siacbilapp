/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import it.csi.siac.siaccorser.model.Codifica;

/**
 * Classe di comparazione tra Codifiche via il campo codice.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 */
public final class ComparatorCodifica implements Comparator<Codifica>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7989640425803703285L;
	
	/** The only accessible instance */
	public static final ComparatorCodifica INSTANCE = new ComparatorCodifica();

	/** Costruttore vuoto di default */
	private ComparatorCodifica() {
		super();
	}
	
	@Override
	public int compare(Codifica codifica1, Codifica codifica2) {
		if(codifica1 == null && codifica2 == null) {
			return 0;
		}
		// Check dei valori null. Un valore null e' considerato inferiore di un qualsiasi valore non-null
		if(codifica1 == null) {
			return -1;
		}
		if(codifica2 == null) {
			return 1;
		}
		if(codifica1.equals(codifica2)) {
			return 0;
		}
		return new CompareToBuilder()
			.append(codifica1.getCodice(), codifica2.getCodice())
			.toComparison();
	}
	
}
