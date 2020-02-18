/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaDatiFinanziariPrimaNotaIntegrataBaseAjaxModel;

/**
 * Classe di AjaxModel per la gestione dei risultati di ricerca dei dati finanziari nella consultazione della prima nota integrata
 * @author Elisa Chiari
 * @version 1.0.0 - 24/10/2016
 *
 */
public class RisultatiRicercaDatiFinanziariPrimaNotaIntegrataFINAjaxModel extends RisultatiRicercaDatiFinanziariPrimaNotaIntegrataBaseAjaxModel {

	/**Per la serializzazione */
	private static final long serialVersionUID = -4338384842277716807L;

	/**
	 * Costruttore vuoto di default
	 */
	public RisultatiRicercaDatiFinanziariPrimaNotaIntegrataFINAjaxModel(){
		super();
		setTitolo("Risultati ricerca dati finanziari - AJAX");
	}
	
	
}
