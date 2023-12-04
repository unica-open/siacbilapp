/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaEmettereSpesa;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 20/11/2014
 *
 */
public class RisultatiRicercaQuoteDaEmettereSpesaAjaxModel extends PagedDataTableAjaxModel<ElementoSubdocumentoDaEmettereSpesa> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6590237316576643770L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteDaEmettereSpesaAjaxModel() {
		super();
		setTitolo("Risultati di Ricerca Quote da Emettere Entrata - AJAX");
	}
	
}
