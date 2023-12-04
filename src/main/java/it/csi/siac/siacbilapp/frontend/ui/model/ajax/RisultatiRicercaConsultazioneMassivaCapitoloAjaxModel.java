/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable, per la consultazione massiva del capitolo.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 04/10/2013
 *
 */
public class RisultatiRicercaConsultazioneMassivaCapitoloAjaxModel extends PagedDataTableAjaxModel<ElementoCapitolo> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4156581885644595296L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaConsultazioneMassivaCapitoloAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Capitolo - AJAX");
	}

}
