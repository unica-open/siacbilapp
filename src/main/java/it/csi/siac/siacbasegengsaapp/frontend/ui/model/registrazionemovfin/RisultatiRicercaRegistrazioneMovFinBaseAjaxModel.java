/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.risultatiricerca.ElementoRegistrazioneMovFin;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Valentina
 * @version 1.0.0 04/05/2015
 * @version 1.1.0 05/10/2015 - portata a fattor comune per GEN/GSA
 *
 */
public class RisultatiRicercaRegistrazioneMovFinBaseAjaxModel extends PagedDataTableAjaxModel<ElementoRegistrazioneMovFin> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1264952741721075029L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistrazioneMovFinBaseAjaxModel() {
		super();
		setTitolo("Risultati di ricerca RegistrazioneMovFin - AJAX");
	}
}
