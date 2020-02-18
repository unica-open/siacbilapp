/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseInserisciAggiornaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * Classe base di model per l'inserimento e l'aggiornamento dell'anticipo spese .
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 *
 */
public abstract class BaseInserisciAggiornaAnticipoSpeseCassaEconomaleModel extends BaseInserisciAggiornaRichiestaEconomaleModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8033553253132827209L;

	@Override
	protected String computaStringaSospeso(RichiestaEconomale richiestaEconomale) {
		String result = "";
		if(richiestaEconomale.getSospeso() != null) {
			result = " - Sospeso N. " + richiestaEconomale.getSospeso().getNumeroSospeso();
		}
		return result;
	}
}
