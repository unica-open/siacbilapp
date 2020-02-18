/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;

/**
 * Classe di comparazione tra tra aliquoteSubdocumentoIva a partire dal codice dell'aliquotaIva associata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 */
public final class ComparatorAliquotaSubdocumentoIva implements Comparator<AliquotaSubdocumentoIva>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7989640425803703285L;
	
	/** The only accessible instance */
	public static final ComparatorAliquotaSubdocumentoIva INSTANCE = new ComparatorAliquotaSubdocumentoIva();
	
	/** Private constructor */
	private ComparatorAliquotaSubdocumentoIva() {
		super();
	}

	@Override
	public int compare(AliquotaSubdocumentoIva o1, AliquotaSubdocumentoIva o2) {
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
		// TipoOnere
		if(o1.getAliquotaIva() == null && o2.getAliquotaIva() == null) {
			return 0;
		}
		if(o1.getAliquotaIva() == null) {
			return -1;
		}
		if(o2.getAliquotaIva() == null) {
			return 1;
		}
		// Codice
		if(o1.getAliquotaIva().getCodice() == null && o2.getAliquotaIva().getCodice() == null) {
			return 0;
		}
		if(o1.getAliquotaIva().getCodice() == null) {
			return -1;
		}
		if(o2.getAliquotaIva().getCodice() == null) {
			return 1;
		}
		return o1.getAliquotaIva().getCodice().compareToIgnoreCase(o2.getAliquotaIva().getCodice());
	}
	
}
