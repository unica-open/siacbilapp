/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.movimentogestione;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoSubImpegnoRegistrazioneMovFin;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;

/**
 * Classe di model per la gestione dei risultati ricerca di impegni e subimpegni
 * @author Nazha Ahmad
 * @version 1.0.0 - 15/09/2016
 *
 */
public class RisultatiRicercaSubImpegniMovimentoGestioneAjaxModel extends PagedDataTableAjaxModel<ElementoSubImpegnoRegistrazioneMovFin> {
	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -863275681126863712L;


	/** Costruttore vuoto di default */
	public RisultatiRicercaSubImpegniMovimentoGestioneAjaxModel() {
		super();
		setTitolo("Risultati di ricerca  - AJAX");
	}


}
