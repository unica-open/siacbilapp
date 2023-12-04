/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.richiestaeconomale;

import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.richiestaeconomale.ElementoRichiestaEconomale;

/**
 * Classe di model per i risultati della ricerca modulare delle richieste economali, gestione dell'AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/03/2017
 *
 */
public class RisultatiRicercaModulareRichiestaEconomaleAjaxModel extends PagedDataTableAjaxModel<ElementoRichiestaEconomale> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8155066811501708329L;
	
	private BigDecimal totale = BigDecimal.ZERO;

	/** Costruttore vuoto di default */
	public RisultatiRicercaModulareRichiestaEconomaleAjaxModel() {
		setTitolo("Risultati ricerca richiesta economale - AJAX");
	}

	/**
	 * @return the totale
	 */
	public BigDecimal getTotale() {
		return totale;
	}

	/**
	 * @param totale the totale to set
	 */
	public void setTotale(BigDecimal totale) {
		this.totale = totale != null ? totale : BigDecimal.ZERO;
	}

}
