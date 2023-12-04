/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.elaborazioniflusso;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.elaborazioniFlusso.ElementoRiconciliazioneDoc;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Vincenzo Gambino
 * @version 1.0.0 20/07/2020
 *
 */
public class RisultatiRiconciliazioneDocAjaxModel extends PagedDataTableAjaxModel<ElementoRiconciliazioneDoc> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4029533272809633835L;

	/** Costruttore vuoto di default */
	public RisultatiRiconciliazioneDocAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Pago PA - AJAX");
	}
	
	
}
