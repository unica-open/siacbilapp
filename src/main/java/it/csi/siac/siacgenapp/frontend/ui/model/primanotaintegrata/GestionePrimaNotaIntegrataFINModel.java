/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.GestionePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Model per la gestione della prima nota integrata. Modulo GEN
 * 
 * @author Paggio Simona
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * @version 1.1.0 - gestione GEN/GSA
 */
public class GestionePrimaNotaIntegrataFINModel extends GestionePrimaNotaIntegrataBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6446396669946073936L;

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
