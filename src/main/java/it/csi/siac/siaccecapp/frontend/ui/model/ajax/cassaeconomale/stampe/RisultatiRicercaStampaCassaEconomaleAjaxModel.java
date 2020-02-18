/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.stampe;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.stampe.ElementoStampeCassaFile;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Valentina Triolo
 * @version 1.0.0 03/06/2014
 *
 */
public class RisultatiRicercaStampaCassaEconomaleAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoStampeCassaFile> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6731953175593275491L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaStampaCassaEconomaleAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Stampe Cassa Economale - AJAX");
	}
	
}
