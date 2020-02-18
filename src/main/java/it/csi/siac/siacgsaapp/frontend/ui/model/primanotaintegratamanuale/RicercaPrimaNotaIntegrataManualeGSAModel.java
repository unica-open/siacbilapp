/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale.RicercaPrimaNotaIntegrataManualeBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca della prima nota libera GSA
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */

public class RicercaPrimaNotaIntegrataManualeGSAModel extends RicercaPrimaNotaIntegrataManualeBaseModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 1459117340501727526L;

	/** Costruttore vuoto di default */
	public RicercaPrimaNotaIntegrataManualeGSAModel(){
		setTitolo("Ricerca Prima Nota Integrata Manuale - GSA");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
