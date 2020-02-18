/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.tabelle;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siaccecser.model.OperazioneCassa;

/**
 * Classe di model per i risultati della ricerca delle operazioni cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/12/2014
 *
 */
public class RisultatiRicercaOperazioneCassaAjaxModel extends GenericRisultatiRicercaAjaxModel<OperazioneCassa> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 601196310588521334L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaOperazioneCassaAjaxModel() {
		setTitolo("Risultati ricerca operazione cassa - AJAX");
	}

}
