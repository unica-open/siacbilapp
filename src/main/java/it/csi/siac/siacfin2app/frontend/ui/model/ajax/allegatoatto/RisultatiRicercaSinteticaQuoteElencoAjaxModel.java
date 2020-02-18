/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegato;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 *
 */
public class RisultatiRicercaSinteticaQuoteElencoAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 536690392966331498L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaSinteticaQuoteElencoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca sintetica quote elenco - AJAX");
	}
		
}
