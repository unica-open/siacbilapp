/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.movimentogestione;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Classe di model per la gestione dei risultati ricerca di impegni e subimpegni
 * @author Elisa Chiari
 * @version 1.0.0 - 29/08/2016
 *
 */
public class RisultatiRicercaSubAccertamentiAjaxModel extends GenericRisultatiRicercaAjaxModel<SubAccertamento> {
	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -863275681126863712L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaSubAccertamentiAjaxModel() {
		super();
		setTitolo("Risultati di ricerca  subAccertamenti- AJAX");
	}


}
