/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.attivitaiva;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.attivitaiva.ElementoGruppoAttivitaIva;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 24/04/2014
 *
 */
public class RisultatiRicercaGruppoAttivitaIvaAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoGruppoAttivitaIva> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1949840562109980124L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaGruppoAttivitaIvaAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Gruppo Attività Iva - AJAX");
	}
		
}
