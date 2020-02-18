/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 22/09/2014
 *
 */
public class RisultatiRicercaQuoteEntrataAjaxModel extends GenericRisultatiRicercaAjaxModel<SubdocumentoEntrata> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2683210424353313326L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteEntrataAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Quote Entrata - AJAX");
	}
		
}
