/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.provvisoriocassa;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaAssociare;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 *
 */
public class RisultatiRicercaQuoteDaAssociareAProvvisorioAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoSubdocumentoDaAssociare> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5311967193645313463L;

	
	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteDaAssociareAProvvisorioAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Quote Da Associare A Provvisorio - AJAX");
	}
		
}
