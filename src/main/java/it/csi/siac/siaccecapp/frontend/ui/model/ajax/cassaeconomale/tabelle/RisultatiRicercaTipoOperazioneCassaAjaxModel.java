/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.tabelle;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;

/**
 * Classe di model per i risultati della ricerca dei tipi di operazione di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/12/2014
 *
 */
public class RisultatiRicercaTipoOperazioneCassaAjaxModel extends PagedDataTableAjaxModel<TipoOperazioneCassa> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -802030838238794647L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaTipoOperazioneCassaAjaxModel() {
		setTitolo("Risultati ricerca tipo operazione cassa - AJAX");
	}

}
