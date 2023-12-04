/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 22/09/2014
 *
 */
public class RisultatiRicercaQuoteSpesaAjaxModel extends PagedDataTableAjaxModel<SubdocumentoSpesa> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 443977794643814047L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteSpesaAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Quote Spesa - AJAX");
	}
		
}
