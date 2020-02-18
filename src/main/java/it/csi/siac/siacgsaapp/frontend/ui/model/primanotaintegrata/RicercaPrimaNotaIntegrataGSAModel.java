/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RicercaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Ricerca della prima nota integrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 *
 * @version 1.0.0 08/10/2015
 */
public class RicercaPrimaNotaIntegrataGSAModel extends RicercaPrimaNotaIntegrataBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8370554254085020373L;

	/** Costruttore vuoto di default */
	public RicercaPrimaNotaIntegrataGSAModel() {
		setTitolo("Ricerca prima nota integrata");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
