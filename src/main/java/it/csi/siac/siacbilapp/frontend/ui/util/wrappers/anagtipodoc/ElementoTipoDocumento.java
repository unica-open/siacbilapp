/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.anagtipodoc;



import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.TipoDocFEL;

/**
 * Wrapper per TipoDocumento.
 * 
 * @author Lobue Filippo
 * @version 1.0.0 - 12/09/2020
 *
 */
/**
 * @author filippo
 *
 */
public class ElementoTipoDocumento implements ModelWrapper, Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final TipoDocFEL tipoDocumentoFel;
	private String azioni;
	
	/**
	 * Costruttore a partire dalla superclasse.
	 * 
	 * @param allegatoAtto l'oggetto da wrappare
	 */
	public ElementoTipoDocumento(TipoDocFEL tipoDocumentoFel) {
		this.tipoDocumentoFel = tipoDocumentoFel;
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	
	@Override
	public int getUid() {
		return tipoDocumentoFel == null ? 0 : tipoDocumentoFel.getUid();
	}
	
	 
	
	// Utilita' per il Javascript
 
	/**
	 * @return the stringaCodice
	 */
	public String getStringaCodice() {
		return tipoDocumentoFel != null ? tipoDocumentoFel.getCodice() : "";
	}
	
	public String getStringaDescrizione() {
		return tipoDocumentoFel != null && tipoDocumentoFel.getDescrizione() != null ? tipoDocumentoFel.getDescrizione() : "";
	}
	
	public String getStringaTipoDocSpesa() {
		return tipoDocumentoFel != null && tipoDocumentoFel.getTipoDocContabiliaSpesa() != null ?  tipoDocumentoFel.getTipoDocContabiliaSpesa().getCodice() +" - "+ tipoDocumentoFel.getTipoDocContabiliaSpesa().getDescrizione() : "";
	}
	
	public String getStringaTipoDocEntrata() {
		return tipoDocumentoFel != null && tipoDocumentoFel.getTipoDocContabiliaEntrata() != null ?tipoDocumentoFel.getTipoDocContabiliaEntrata().getCodice() +" - "+  tipoDocumentoFel.getTipoDocContabiliaEntrata().getDescrizione() : "";
	}
	 
 
	 
}
