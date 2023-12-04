/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Classe di model per i risultati della ricerca dei tipi giustificativo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/12/2014
 *
 */
public class RisultatiRicercaPianoDeiContiAjaxModel extends PagedDataTableAjaxModel<Conto> {
	
	private static final long serialVersionUID = -3121692694523561518L;
	

	/** Costruttore vuoto di default */
	public RisultatiRicercaPianoDeiContiAjaxModel() {
		setTitolo("Risultati ricerca piano dei conti - AJAX");
	}

}
