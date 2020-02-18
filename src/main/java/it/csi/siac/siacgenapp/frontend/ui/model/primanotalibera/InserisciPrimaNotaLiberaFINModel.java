/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.InserisciPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * @author paggio
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 *
 */
public class InserisciPrimaNotaLiberaFINModel extends InserisciPrimaNotaLiberaBaseModel{

	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -6034321361490049887L;

	/** Costruttore vuoto di default */
	public InserisciPrimaNotaLiberaFINModel(){
		setTitolo("Inserisci Prima Nota Libera");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
