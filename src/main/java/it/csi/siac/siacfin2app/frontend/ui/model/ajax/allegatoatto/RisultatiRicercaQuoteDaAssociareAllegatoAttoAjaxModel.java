/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaAssociareAllegatoAtto;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 */
public class RisultatiRicercaQuoteDaAssociareAllegatoAttoAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoSubdocumentoDaAssociareAllegatoAtto<?, ?, ?, ?, ?>> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2423657356951859157L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteDaAssociareAllegatoAttoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Quote Da Associare - AJAX");
	}

}
