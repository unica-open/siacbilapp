/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.RicercaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca della prima nota libera GSA
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 10/10/2015
 */

public class RicercaPrimaNotaLiberaGSAModel extends RicercaPrimaNotaLiberaBaseModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 1459117340501727526L;

	/** Costruttore vuoto di default */
	public RicercaPrimaNotaLiberaGSAModel(){
		setTitolo("Ricerca Prima Nota Libera");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
