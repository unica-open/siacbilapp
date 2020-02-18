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
public class RisultatiRicercaSvalutazioneCespiteModel extends BaseRisultatiRicercaVariazioneCespiteModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7150320890446875447L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaSvalutazioneCespiteModel() {
		setTitolo("Inventario - Risultati Ricerca svalutazioni Cespite");
	}
	
	@Override
	public String getFormTitle() {
		return "Risultati ricerca svalutazioni cespite";
	}

	@Override
	public String getBaseUrl() {
		return "risultatiRicercaSvalutazioneCespite";
	}
	
	@Override
	public String getTestoSelectTipoVariazione() {
		return "D -  Decremento";
	}

	@Override
	protected Boolean getFlagTipoVariazioneIncremento() {
		return Boolean.FALSE;
	}

}
