/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite;

/**
 * Classe di base per i risultati di ricerca della variazione del cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/08/2018
 *
 */
public class RisultatiRicercaRivalutazioneCespiteModel extends BaseRisultatiRicercaVariazioneCespiteModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7150320890446875447L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRivalutazioneCespiteModel() {
		setTitolo("Inventario - Risultati Ricerca rivalutazioni Cespite");
	}

	@Override
	public String getFormTitle() {
		return "Risultati ricerca rivalutazioni cespite";
	}
	
	@Override
	public String getBaseUrl() {
		return "risultatiRicercaRivalutazioneCespite";
	}
	
	@Override
	public String getTestoSelectTipoVariazione() {
		return "I -  Incremento";
	}

	@Override
	protected Boolean getFlagTipoVariazioneIncremento() {
		return Boolean.TRUE;
	}

}
