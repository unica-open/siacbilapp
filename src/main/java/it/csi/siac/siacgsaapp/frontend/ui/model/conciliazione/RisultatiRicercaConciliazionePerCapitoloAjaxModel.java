/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.conciliazione.ElementoConciliazionePerCapitolo;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 28/10/2015
 *
 */
public class RisultatiRicercaConciliazionePerCapitoloAjaxModel extends PagedDataTableAjaxModel<ElementoConciliazionePerCapitolo> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2577545549170749561L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaConciliazionePerCapitoloAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Conciliazione per capitolo - AJAX");
	}
}
