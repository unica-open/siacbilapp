/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.extra;

import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.TipologiaRichiestaCassaEconomale;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;

/**
 * Tipologia di richiesta per la cassa, classe base per le azioni extra.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/12/2014
 *
 */
public abstract class TipologiaRichiestaCassaEconomaleExtra extends TipologiaRichiestaCassaEconomale {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6213040486486395907L;

	/**
	 * Popolamento del Tipo di richiesta economale.
	 * 
	 * @param codice            il codice della richiesta
	 */
	protected void populateTipoRichiestaEconomale(String codice) {
		TipoRichiestaEconomale tipoRichiestaEconomale = new TipoRichiestaEconomale("", codice);
		setTipoRichiestaEconomale(tipoRichiestaEconomale);
	}
	
}
