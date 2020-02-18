/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.ajax.registroa;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa.ElementoPrimaNotaRegistroA;

/**
 * Classe di model per i risultati della ricerca del registro A del cespite, gestione dell'AJAX.
 * @author elisa
 * @version 1.0.0 - 29-11-2018
 */
public class RisultatiRicercaScrittureRegistroAByCespiteAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoPrimaNotaRegistroA> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3019819285391153286L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaScrittureRegistroAByCespiteAjaxModel() {
		setTitolo("Ricerca scritture by cespite - AJAX");
	}

}
