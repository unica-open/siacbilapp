/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.ajax.registroa;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa.ElementoMovimentoDettaglioRegistroA;

/**
 * Classe di model per i risultati della ricerca del registro A del cespite, gestione dell'AJAX.
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/10/2018
 */
public class RisultatiRicercaMovimentoDettaglioRegistroACespiteAjaxModel extends PagedDataTableAjaxModel<ElementoMovimentoDettaglioRegistroA> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2046513623283477993L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaMovimentoDettaglioRegistroACespiteAjaxModel() {
		setTitolo("Ricerca registro prime note definitive verso inventario contabile - AJAX");
	}

}
