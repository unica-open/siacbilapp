/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfelapp.frontend.ui.model.fatturaelettronica;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfelapp.frontend.ui.util.wrapper.ElementoFatturaFEL;

/**
 * Classe di model per i risultati della ricerca della fattura elettronica, gestione dell'AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/06/2015
 */
public class RisultatiRicercaFatturaElettronicaAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoFatturaFEL> {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = -6051383247960350936L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaFatturaElettronicaAjaxModel() {
		setTitolo("Risultati ricerca FEL - AJAX");
	}

}
