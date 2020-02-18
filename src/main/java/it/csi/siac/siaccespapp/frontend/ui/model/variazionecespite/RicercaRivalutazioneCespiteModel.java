/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite;

/**
 * The Class InserisciRivalutazioneCespiteModel.
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/08/2018
 */
public class RicercaRivalutazioneCespiteModel extends BaseRicercaVariazioneCespiteModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4238127399042041307L;

	/**
	 * Instantiates a new inserisci tipo bene model.
	 */
	public RicercaRivalutazioneCespiteModel() {
		setTitolo("Inventario - Ricerca rivalutazione cespite");
	}

	@Override
	public String getBaseUrl() {
		return "ricercaRivalutazioneCespite";
	}

	@Override
	public String getFormTitle() {
		return "Ricerca rivalutazioni cespite";
	}

	@Override
	public Boolean getFlagTipoVariazioneIncremento() {
		return Boolean.TRUE;
	}
	
	@Override
	public String getFormSubtitle() {
		return "Dati rivalutazione";
	}
	
	@Override
	public String getTestoSelectTipoVariazione() {
		return "I -  Incremento";
	}

}
