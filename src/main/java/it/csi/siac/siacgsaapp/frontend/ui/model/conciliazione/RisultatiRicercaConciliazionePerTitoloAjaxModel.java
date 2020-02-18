/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.conciliazione.ElementoConciliazionePerTitolo;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 28/10/2015
 *
 */
public class RisultatiRicercaConciliazionePerTitoloAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoConciliazionePerTitolo> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8870758211440788621L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaConciliazionePerTitoloAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Conciliazione per titolo - AJAX");
	}
}
