/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RicercaValidazionePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * @author Marchino Alessandro
 * @version 1.0.0 14/05/2015
 * @version 1.1.0 16/06/2015
 */
public class RicercaValidazionePrimaNotaIntegrataGSAModel extends RicercaValidazionePrimaNotaIntegrataBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6160744655211746090L;

	/** Costruttore vuoto di default */
	public RicercaValidazionePrimaNotaIntegrataGSAModel() {
		setTitolo("Validazione massiva prime note integrate");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
