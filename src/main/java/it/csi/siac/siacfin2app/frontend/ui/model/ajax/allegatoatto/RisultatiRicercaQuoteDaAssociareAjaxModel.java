/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaAssociare;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 *
 */
public class RisultatiRicercaQuoteDaAssociareAjaxModel extends PagedDataTableAjaxModel<ElementoSubdocumentoDaAssociare> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 443977794643814047L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteDaAssociareAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Quote Da Associare - AJAX");
	}
		
}
