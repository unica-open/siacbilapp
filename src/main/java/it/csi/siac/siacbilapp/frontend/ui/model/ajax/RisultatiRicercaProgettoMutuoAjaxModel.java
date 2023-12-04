/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoProgetto;



public class RisultatiRicercaProgettoMutuoAjaxModel extends PagedDataTableAjaxModel<ElementoProgetto> {

	private static final long serialVersionUID = 8470088129080166325L;

	public RisultatiRicercaProgettoMutuoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Progetto Mutuo - AJAX");
	}
		
}
