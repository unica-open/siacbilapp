/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * Classe di comparazione tra tra elencoDocumentiAllegato a partire da anno e numero elenco.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 */
public final class ComparatorElencoDocumentiAllegato implements Comparator<ElencoDocumentiAllegato>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7989640425803703285L;
	
	/** The ascending order instance */
	public static final ComparatorElencoDocumentiAllegato INSTANCE_ASC = new ComparatorElencoDocumentiAllegato(true);
	/** The descending order instance */
	public static final ComparatorElencoDocumentiAllegato INSTANCE_DESC = new ComparatorElencoDocumentiAllegato(false);
	
	private final boolean ascending;
	
	/** 
	 * Private constructor.
	 * @param ascending whether the comparison is in ascending or descending order
	 */
	private ComparatorElencoDocumentiAllegato(boolean ascending) {
		super();
		this.ascending = ascending;
	}

	@Override
	public int compare(ElencoDocumentiAllegato o1, ElencoDocumentiAllegato o2) {
		if(o1 == null && o2 == null){
			return 0;
		}
		if(o1 == null){ 
			return ascending ? 1 : -1;
		}
		if(o2 == null){
			return ascending ? -1 : 1;
		}
		if(o1.equals(o2)) {
			return 0;
		}
		CompareToBuilder ctb = new CompareToBuilder();
		if(ascending) {
			ctb.append(o1.getAnno(), o2.getAnno())
				.append(o1.getNumero(), o2.getNumero());
		} else {
			ctb.append(o2.getAnno(), o1.getAnno())
				.append(o2.getNumero(), o1.getNumero());
		}
		return ctb.toComparison();
	}
	
}
