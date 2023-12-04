/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.elaborazioniflusso;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.elaborazioniFlusso.ElementoElaborazioniFlusso;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 15/set/2014
 *
 */
public class RisultatiRicercaElaborazioniFlussoAjaxModel extends PagedDataTableAjaxModel<ElementoElaborazioniFlusso> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4029533272809633835L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaElaborazioniFlussoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Pago PA - AJAX");
	}
	
	
}
