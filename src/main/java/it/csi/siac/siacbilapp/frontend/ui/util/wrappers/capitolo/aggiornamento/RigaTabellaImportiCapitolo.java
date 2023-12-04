/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

import java.io.Serializable;


/**
 * The Class RigaComponenteImportoCapitolo.
 * @
 */
public abstract class RigaTabellaImportiCapitolo implements Serializable {

	/**
	 * Per la serializzaziome
	 */
	private static final long serialVersionUID = 966468747424472397L;

	public abstract String getDescrizioneImporto();
	
	public abstract int getSottoRigheSize();
	
	public abstract boolean getImportoEditabileCellaResiduo();
	public abstract boolean getImportoEditabileCellaAnnoBilancio();
	public abstract boolean getImportoEditabileCelleAnnoBilancioPiuUnoPiuDue();
	public abstract boolean getRigaModificabilePerTipoImporto();
	public abstract boolean getRigaEliminabile();
	public abstract String getFirstCellString();
	public abstract int getRowspanEdit();
	public abstract String getLastSottoRigaCssClass();
	
	public String getTrCssClass() {
		return "";
	}
	
		
}

