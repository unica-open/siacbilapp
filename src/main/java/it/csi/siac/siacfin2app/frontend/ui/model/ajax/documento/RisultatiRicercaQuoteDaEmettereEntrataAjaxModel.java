/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaEmettereEntrata;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 20/11/2014
 *
 */
public class RisultatiRicercaQuoteDaEmettereEntrataAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoSubdocumentoDaEmettereEntrata> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8691339354547506465L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteDaEmettereEntrataAjaxModel() {
		super();
		setTitolo("Risultati di Ricerca Quote da Emettere Entrata - AJAX");
	}
	
}
