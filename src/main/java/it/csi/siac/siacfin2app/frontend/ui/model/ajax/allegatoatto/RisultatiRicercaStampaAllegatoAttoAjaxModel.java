/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoStampaAllegatoAtto;

/**
 * Classe di model ajax per i risultati di ricerca della stampa dell'allegato atto.
 */
public class RisultatiRicercaStampaAllegatoAttoAjaxModel extends PagedDataTableAjaxModel<ElementoStampaAllegatoAtto> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 7695657314647218823L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaStampaAllegatoAttoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Stampa Allegato atto - AJAX");
	}
}
