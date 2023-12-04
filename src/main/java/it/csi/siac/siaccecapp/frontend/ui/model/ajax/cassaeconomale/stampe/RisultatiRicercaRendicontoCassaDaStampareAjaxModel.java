/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.stampe;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.stampe.ElementoMovimentoStampa;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 03/06/2014
 */
public class RisultatiRicercaRendicontoCassaDaStampareAjaxModel extends PagedDataTableAjaxModel<ElementoMovimentoStampa> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6731953175593275491L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRendicontoCassaDaStampareAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Rendiconto Cassa Economale da stampare - AJAX");
	}
	
}
