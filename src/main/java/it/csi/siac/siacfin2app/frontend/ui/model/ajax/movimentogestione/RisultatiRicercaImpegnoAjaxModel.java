/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.movimentogestione;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 20/01/2016
 *
 */
public class RisultatiRicercaImpegnoAjaxModel extends GenericRisultatiRicercaAjaxModel<Impegno> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1949840562109980124L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaImpegnoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Impegni - AJAX");
	}
		
}
