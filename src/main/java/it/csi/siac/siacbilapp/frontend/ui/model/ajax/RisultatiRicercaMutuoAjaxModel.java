/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.mutuo.ElementoMutuo;



public class RisultatiRicercaMutuoAjaxModel extends PagedDataTableAjaxModel<ElementoMutuo> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 194721110144079568L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaMutuoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Mutuo - AJAX");
	}
		
}
