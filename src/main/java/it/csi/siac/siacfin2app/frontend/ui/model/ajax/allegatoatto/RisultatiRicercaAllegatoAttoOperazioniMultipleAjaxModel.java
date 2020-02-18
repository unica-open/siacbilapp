/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoAllegatoAtto;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author elisa
 * @version 1.0.0 26/feb/2018
 *
 */
public class RisultatiRicercaAllegatoAttoOperazioniMultipleAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoAllegatoAtto> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4123720164469249806L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaAllegatoAttoOperazioniMultipleAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Allegato Atto - AJAX");
	}
	
}
