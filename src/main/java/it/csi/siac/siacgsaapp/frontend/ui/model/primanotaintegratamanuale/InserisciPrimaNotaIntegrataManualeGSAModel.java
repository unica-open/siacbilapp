/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale.InserisciPrimaNotaIntegrataManualeBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'inserimento della prima nota integrata manuale
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
public class InserisciPrimaNotaIntegrataManualeGSAModel extends InserisciPrimaNotaIntegrataManualeBaseModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -7518462714116271638L;

	/** Costruttore vuoto di default */
	public InserisciPrimaNotaIntegrataManualeGSAModel(){
		setTitolo("Inserisci Prima Nota Integrata Manuale - GSA");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

	@Override
	public String getAmbitoSuffix() {
		return "GSA";
	}

	
	@Override
	public boolean isValidazione() {
		return true;
	}

}
