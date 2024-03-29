/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.ajax.registroa;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa.ElementoPrimaNotaRegistroA;

/**
 * Classe di model per i risultati della ricerca del registro A del cespite, gestione dell'AJAX.
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/10/2018
 */
public class RisultatiRicercaRegistroACespiteAjaxModel extends PagedDataTableAjaxModel<ElementoPrimaNotaRegistroA> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 649412662932709388L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistroACespiteAjaxModel() {
		setTitolo("Ricerca registro prime note definitive verso inventario contabile - AJAX");
	}

}
