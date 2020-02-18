/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Comparator per i wrapper del model.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/01/2013
 */
public final class ComparatorModelWrapper implements Comparator<ModelWrapper>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6166006322264786096L;
	
	/** The only accessible instance */
	public static final ComparatorModelWrapper INSTANCE = new ComparatorModelWrapper();
	
	/** Private constructor */
	private ComparatorModelWrapper() {
		super();
	}

	@Override
	public int compare(ModelWrapper o1, ModelWrapper o2) {
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
			.append(o1.getUid(), o2.getUid())
			.toComparison();
	}

}
