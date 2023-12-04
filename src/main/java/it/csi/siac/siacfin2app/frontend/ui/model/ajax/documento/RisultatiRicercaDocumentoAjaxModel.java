/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumento;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 10/03/2014
 *
 */
public class RisultatiRicercaDocumentoAjaxModel extends PagedDataTableAjaxModel<ElementoDocumento> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1949840562109980124L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaDocumentoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Documenti - AJAX");
	}
		
}
