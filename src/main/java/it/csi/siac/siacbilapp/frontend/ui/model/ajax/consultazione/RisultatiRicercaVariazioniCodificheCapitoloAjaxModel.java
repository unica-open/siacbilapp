/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax.consultazione;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione.ElementoDettaglioVariazioneCodificaCapitolo;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 02/01/2014
 *
 */
public class RisultatiRicercaVariazioniCodificheCapitoloAjaxModel extends PagedDataTableAjaxModel<ElementoDettaglioVariazioneCodificaCapitolo> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1949840562109980124L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaVariazioniCodificheCapitoloAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Variazioni codifiche del capitolo - AJAX");
	}
		
}
