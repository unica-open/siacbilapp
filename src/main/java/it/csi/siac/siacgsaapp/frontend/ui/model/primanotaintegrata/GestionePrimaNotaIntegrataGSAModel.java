/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.GestionePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Model per la gestione della prima nota integrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 */
public class GestionePrimaNotaIntegrataGSAModel extends GestionePrimaNotaIntegrataBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4892966738943371097L;

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
