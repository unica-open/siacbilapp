/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.attivitaiva;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.attivitaiva.ElementoStampaIva;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 03/06/2014
 *
 */
public class RisultatiRicercaStampaIvaAjaxModel extends PagedDataTableAjaxModel<ElementoStampaIva> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6731953175593275491L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaStampaIvaAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Stampa Iva - AJAX");
	}
	
}
