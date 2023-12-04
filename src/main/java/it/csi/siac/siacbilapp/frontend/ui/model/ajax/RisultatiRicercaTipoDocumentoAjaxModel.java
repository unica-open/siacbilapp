/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;



import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.anagtipodoc.ElementoTipoDocumento;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 *
 */
public class RisultatiRicercaTipoDocumentoAjaxModel extends PagedDataTableAjaxModel<ElementoTipoDocumento> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4029533272809633835L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaTipoDocumentoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Tipo Documento FEL - AJAX");
	}
	 
}
