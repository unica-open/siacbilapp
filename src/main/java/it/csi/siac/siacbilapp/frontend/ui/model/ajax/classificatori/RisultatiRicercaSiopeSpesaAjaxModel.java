/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax.classificatori;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacbilser.model.SiopeSpesa;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable per il SIOPE di spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 16/12/2015
 *
 */
public class RisultatiRicercaSiopeSpesaAjaxModel extends GenericRisultatiRicercaAjaxModel<SiopeSpesa> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 864654160602839550L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaSiopeSpesaAjaxModel() {
		setTitolo("Risultati di ricerca SIOPE - AJAX");
	}
}
