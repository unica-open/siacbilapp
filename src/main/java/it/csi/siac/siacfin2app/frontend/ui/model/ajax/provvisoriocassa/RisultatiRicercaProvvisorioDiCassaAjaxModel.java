/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.provvisoriocassa;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 10/09/2014
 *
 */
public class RisultatiRicercaProvvisorioDiCassaAjaxModel extends PagedDataTableAjaxModel<ProvvisorioDiCassa> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3341073866091955070L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaProvvisorioDiCassaAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Provvisorio di cassa - AJAX");
	}
		
}
