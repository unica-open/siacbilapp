/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegato;

/**
 * Classe di comparazione tra tra elencoDocumentiAllegato a partire da anno e numero elenco.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 */
public final class ComparatorElementoElencoDocumentiAllegato implements Comparator<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7989640425803703285L;
	
	/** The ascending order instance */
	public static final ComparatorElementoElencoDocumentiAllegato INSTANCE_ASC = new ComparatorElementoElencoDocumentiAllegato(true);
	/** The descending order instance */
	public static final ComparatorElementoElencoDocumentiAllegato INSTANCE_DESC = new ComparatorElementoElencoDocumentiAllegato(false);
	
	private final boolean ascending;
	
	/** 
	 * Private constructor.
	 * @param ascending whether the comparison is in ascending or descending order
	 */
	private ComparatorElementoElencoDocumentiAllegato(boolean ascending) {
		super();
		this.ascending = ascending;
	}

	@Override
	public int compare(ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> o1, ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> o2) {
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
		
		return ascending ? o1.compareTo(o2) : o2.compareTo(o1);
	}
	
}
