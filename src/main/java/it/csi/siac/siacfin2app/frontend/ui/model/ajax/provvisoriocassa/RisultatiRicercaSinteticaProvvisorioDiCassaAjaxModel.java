/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.provvisoriocassa;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.provvisoriodicassa.ElementoProvvisorioDiCassa;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 11/03/2016
 *
 */
public class RisultatiRicercaSinteticaProvvisorioDiCassaAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoProvvisorioDiCassa> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3341073866091955070L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaSinteticaProvvisorioDiCassaAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Provvisorio di cassa - AJAX");
	}
}
