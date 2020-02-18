/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

/**
 * Classe di comparazione tra Codifiche FIN via il campo codice.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 */
public final class ComparatorCodificaFin implements Comparator<CodificaFin>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7668265424072499909L;
	
	/** The only accessible instance */
	public static final ComparatorCodificaFin INSTANCE = new ComparatorCodificaFin();

	/** Costruttore vuoto di default */
	private ComparatorCodificaFin() {
		super();
	}
	
	@Override
	public int compare(CodificaFin o1, CodificaFin o2) {
		int result = 0;
		if (o1 == null) {
			result = (o2 == null ? 0 : -1);
		} else if (o2 == null) {
			result = 1;
		} else {
			CompareToBuilder compareToBuilder = new CompareToBuilder();
			compareToBuilder.append(o1.getCodice(), o2.getCodice());
			result = compareToBuilder.toComparison();
		}
		return result;
	}
	
}
