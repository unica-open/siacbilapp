/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite;

/**
 * The Class InserisciSvalutazioneCespiteModel.
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/08/2018
 */
public class RicercaSvalutazioneCespiteModel extends BaseRicercaVariazioneCespiteModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4238127399042041307L;

	/**
	 * Instantiates a new inserisci tipo bene model.
	 */
	public RicercaSvalutazioneCespiteModel() {
		setTitolo("Inventario - Ricerca rivalutazione cespite");
	}

	@Override
	public String getBaseUrl() {
		return "ricercaSvalutazioneCespite";
	}

	@Override
	public String getFormTitle() {
		return "Ricerca svalutazioni cespite";
	}

	@Override
	public Boolean getFlagTipoVariazioneIncremento() {
		return Boolean.FALSE;
	}
	
	@Override
	public String getFormSubtitle() {
		return "Dati svalutazione";
	}
	
	@Override
	public String getTestoSelectTipoVariazione() {
		return "D -  Decremento";
	}

}
