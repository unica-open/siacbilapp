/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfelapp.frontend.ui.util.converter;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import it.csi.siac.sirfelser.model.RiepilogoBeniFEL;

/**
 * Comparatore per il riepilogo beni a partire dal progressivo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/06/2015
 *
 */
public final class RiepilogoBeniFELComparator implements Comparator<RiepilogoBeniFEL>, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4962369592543990853L;
	
	/** L'unica istanza esposta per il comparatore */
	public static final RiepilogoBeniFELComparator INSTANCE = new RiepilogoBeniFELComparator();
	
	/** Costruttore privato: non permetto l'istanziazione della classe */
	private RiepilogoBeniFELComparator() {
	}

	@Override
	public int compare(RiepilogoBeniFEL o1, RiepilogoBeniFEL o2) {
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
		return new CompareToBuilder()
			.append(o1.getProgressivo(), o2.getProgressivo())
			.toComparison();
	}

}
