/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;

/**
 * Classe di comparazione tra Classificazioni Gerarchiche via il campo ordine.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 */
public final class ComparatorClassificatoreGerarchico implements Comparator<ClassificatoreGerarchico>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6901341292042716902L;
	/** The only accessible instance */
	public static final ComparatorClassificatoreGerarchico INSTANCE = new ComparatorClassificatoreGerarchico();

	/** Costruttore vuoto di default */
	private ComparatorClassificatoreGerarchico() {
		super();
	}
	
	@Override
	public int compare(ClassificatoreGerarchico o1, ClassificatoreGerarchico o2) {
		if(o1 == null && o2 == null) {
			return 0;
		}
		// Check dei valori null. Un valore null e' considerato inferiore di un qualsiasi valore non-null
		if(o1 == null) {
			return -1;
		}
		if(o2 == null) {
			return 1;
		}
		if(o1.equals(o2)) {
			return 0;
		}
		return new CompareToBuilder()
			.append(o1.getOrdine(), o2.getOrdine())
			.toComparison();
	}

}
