/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.stornoueb.ElementoStornoUEB;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 19/09/2013
 *
 */
public class RisultatiRicercaStornoUEBAjaxModel extends PagedDataTableAjaxModel<ElementoStornoUEB> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1949840562109980124L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaStornoUEBAjaxModel() {
		super();
		setTitolo("Risultati di ricerca storni UEB - AJAX");
	}
		
}
