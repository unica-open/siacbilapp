/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax.classificatori;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacbilser.model.SiopeEntrata;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable per il SIOPE di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 16/12/2015
 *
 */
public class RisultatiRicercaSiopeEntrataAjaxModel extends PagedDataTableAjaxModel<SiopeEntrata> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6859587294402816097L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaSiopeEntrataAjaxModel() {
		setTitolo("Risultati di ricerca SIOPE - AJAX");
	}
}
