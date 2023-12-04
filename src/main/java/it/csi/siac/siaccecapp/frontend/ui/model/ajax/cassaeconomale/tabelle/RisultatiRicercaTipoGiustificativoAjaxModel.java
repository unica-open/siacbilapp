/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.tabelle;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siaccecser.model.TipoGiustificativo;

/**
 * Classe di model per i risultati della ricerca dei tipi giustificativo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/12/2014
 *
 */
public class RisultatiRicercaTipoGiustificativoAjaxModel extends PagedDataTableAjaxModel<TipoGiustificativo> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6957989232504880200L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaTipoGiustificativoAjaxModel() {
		setTitolo("Risultati ricerca tipo giustificativo - AJAX");
	}

}
