/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 10/09/2014
 *
 */
public class RisultatiRicercaElencoDocumentiAllegatoAjaxModel extends GenericRisultatiRicercaAjaxModel<ElencoDocumentiAllegato> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3341073866091955070L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaElencoDocumentiAllegatoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Elenco Documento Allegato - AJAX");
	}
		
}
