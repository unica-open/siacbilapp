/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.capitoli;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;

/**
 * Action per i risultati di ricerca del capitoli di uscita gestione .
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 06/07/2016
 * 
 */
public class RisultatiRicercaCapitoloEntrataPrevisioneAjaxModel extends PagedDataTableAjaxModel<ElementoCapitolo> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5645177165696759094L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloEntrataPrevisioneAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Capitoli Entrata Previsione - AJAX");
	}
		
}
