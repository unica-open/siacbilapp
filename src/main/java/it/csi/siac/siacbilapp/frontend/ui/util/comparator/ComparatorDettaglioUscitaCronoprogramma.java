/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;

/**
 * Comparatore per i dettagli di uscita del cronoprogramma.
 */
public final class ComparatorDettaglioUscitaCronoprogramma implements Comparator<DettaglioUscitaCronoprogramma>,Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8107148149823403514L;
	/** The ascending order instance */
	public static final ComparatorDettaglioUscitaCronoprogramma INSTANCE_ASC = new ComparatorDettaglioUscitaCronoprogramma(true);
	/** The descending order instance */
	public static final ComparatorDettaglioUscitaCronoprogramma INSTANCE_DESC = new ComparatorDettaglioUscitaCronoprogramma(false);
	
	private final boolean ascending;
	
	/**
	 * Costruttore di default.
	 * 
	 * @param ascending definisce se la comparazinoe debba essere in ordine ascendente o discendente.
	 */
	private ComparatorDettaglioUscitaCronoprogramma(boolean ascending) {
		super();
		this.ascending = ascending;
	}
	
	@Override
	public int compare(DettaglioUscitaCronoprogramma o1, DettaglioUscitaCronoprogramma o2) {
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
			ctb.append(o1.getAnnoEntrata(), o2.getAnnoEntrata())
			   .append(o1.getAnnoCompetenza(), o2.getAnnoCompetenza());
		} else {
			ctb.append(o2.getAnnoEntrata(), o1.getAnnoEntrata())
			   .append(o2.getAnnoCompetenza(), o1.getAnnoCompetenza());
		}
		return ctb.toComparison();
	}

}
