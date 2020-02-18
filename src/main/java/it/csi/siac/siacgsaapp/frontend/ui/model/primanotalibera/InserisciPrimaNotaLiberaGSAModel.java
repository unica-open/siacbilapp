/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.InserisciPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * @author Elisa Chiari
 * @version 1.0.0 - 15/10/2015
 *
 */
public class InserisciPrimaNotaLiberaGSAModel extends InserisciPrimaNotaLiberaBaseModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -7518462714116271638L;

	/** Costruttore vuoto di default */
	public InserisciPrimaNotaLiberaGSAModel(){
		setTitolo("Inserisci Prima Nota Libera");
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
		return StatoOperativoPrimaNota.PROVVISORIO.equals(getPrimaNotaLibera().getStatoOperativoPrimaNota());
	}
}
