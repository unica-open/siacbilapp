/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.richiestaeconomale;

import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.richiestaeconomale.ElementoRichiestaEconomale;

/**
 * Classe di model per i risultati della richiesta delle richieste economali, gestione dell'AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/01/2015
 *
 */
public class RisultatiRicercaRichiestaEconomaleAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoRichiestaEconomale> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6793464465202548880L;
	
	private BigDecimal totale = BigDecimal.ZERO;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRichiestaEconomaleAjaxModel() {
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
