/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoProgetto;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 06/02/2014
 *
 */
public class RisultatiRicercaProgettoAjaxModel extends PagedDataTableAjaxModel<ElementoProgetto> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1949840562109980124L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaProgettoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Progetto - AJAX");
	}
		
}
