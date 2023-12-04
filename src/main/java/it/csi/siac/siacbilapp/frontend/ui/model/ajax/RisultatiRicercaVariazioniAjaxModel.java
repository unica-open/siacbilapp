/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoVariazione;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 03/11/2013
 *
 */
public class RisultatiRicercaVariazioniAjaxModel extends PagedDataTableAjaxModel<ElementoVariazione> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1949840562109980124L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaVariazioniAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Variazioni - AJAX");
	}
		
}
