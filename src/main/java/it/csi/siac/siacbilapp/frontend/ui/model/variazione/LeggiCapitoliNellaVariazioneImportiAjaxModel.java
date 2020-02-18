/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;

/**
 * Classe di model per la gestione della paginazione dei capitoli della variazione
 * @author Elisa Chiari
 *
 */
public class LeggiCapitoliNellaVariazioneImportiAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoCapitoloVariazione> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -6433323987772862385L;

	/** Costruttore vuoto di default */
	public LeggiCapitoliNellaVariazioneImportiAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Allegato Atto - AJAX");
	}
}
