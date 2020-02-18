/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ConsultaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
/**
 * Classe di model per la consultazione della prima nota integrata. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/05/2015
 * @version 1.1.0 - 08/10/2015
 *
 */
public class ConsultaPrimaNotaIntegrataFINModel extends ConsultaPrimaNotaIntegrataBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5193487827100162252L;

	/** Costruttore vuoto di default */
	public ConsultaPrimaNotaIntegrataFINModel() {
		setTitolo("Consulta prima nota integrata");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
