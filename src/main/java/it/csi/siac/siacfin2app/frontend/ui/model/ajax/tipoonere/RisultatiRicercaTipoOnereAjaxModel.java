/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.tipoonere;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.tipoonere.ElementoTipoOnere;

/**
 * Classe di model per i risultati della ricerca del tipo onere, risultati AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/11/2014
 *
 */
public class RisultatiRicercaTipoOnereAjaxModel extends PagedDataTableAjaxModel<ElementoTipoOnere> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5266875856516021682L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaTipoOnereAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Onere - AJAX");
	}
	
}
