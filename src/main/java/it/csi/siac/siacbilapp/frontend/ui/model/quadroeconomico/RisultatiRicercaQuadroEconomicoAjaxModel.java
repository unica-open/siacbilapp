/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.quadroeconomico;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.quadroeconomico.ElementoQuadroEconomico;
//import it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.classifgsa.ElementoClassificatoreGSA;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 04/01/2018
 *
 */
public class RisultatiRicercaQuadroEconomicoAjaxModel extends PagedDataTableAjaxModel<ElementoQuadroEconomico> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8080704160199932886L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaQuadroEconomicoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Quadro Economico - AJAX");
	}

}
