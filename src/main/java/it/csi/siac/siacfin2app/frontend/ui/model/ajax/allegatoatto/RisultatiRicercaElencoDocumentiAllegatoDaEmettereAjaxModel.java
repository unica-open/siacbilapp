/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoDaEmettere;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 10/09/2014
 *
 */
public class RisultatiRicercaElencoDocumentiAllegatoDaEmettereAjaxModel extends PagedDataTableAjaxModel<ElementoElencoDocumentiAllegatoDaEmettere> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3341073866091955070L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaElencoDocumentiAllegatoDaEmettereAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Elenco Documento Allegato - AJAX");
	}
		
}
