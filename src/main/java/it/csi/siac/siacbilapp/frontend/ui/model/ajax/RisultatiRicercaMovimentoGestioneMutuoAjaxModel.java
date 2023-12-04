/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoMovimentoGestione;



public class RisultatiRicercaMovimentoGestioneMutuoAjaxModel extends PagedDataTableAjaxModel<ElementoMovimentoGestione> {

	private static final long serialVersionUID = 8470088129080166325L;

	public RisultatiRicercaMovimentoGestioneMutuoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Movimento Contabile Mutuo - AJAX");
	}
		
}
