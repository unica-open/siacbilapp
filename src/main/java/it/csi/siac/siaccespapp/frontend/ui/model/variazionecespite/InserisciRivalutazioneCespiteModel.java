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
public class InserisciRivalutazioneCespiteModel extends BaseInserisciVariazioneCespiteModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4238127399042041307L;

	/**
	 * Instantiates a new inserisci tipo bene model.
	 */
	public InserisciRivalutazioneCespiteModel() {
		setTitolo("Inventario - Rivalutazione cespite");
	}

	@Override
	public String getBaseUrl() {
		return "inserisciRivalutazioneCespite";
	}

	@Override
	public String getFormTitle() {
		return "Inserisci rivalutazione cespite";
	}

	@Override
	public String getWizardStep3() {
		return "rivalutazioni scheda cespite";
	}

	@Override
	public Boolean getFlagTipoVariazioneIncremento() {
		return Boolean.TRUE;
	}

	@Override
	public String getTableTitle() {
		return "Rivalutazioni";
	}

	@Override
	public String getButtonNewTitle() {
		return "Inserisci nuova rivalutazione";
	}

	@Override
	public String getIntestazioneInserimentoNuovaVariazione() {
		return "Dati inserimento rivalutazione";
	}

	@Override
	public String getIntestazioneAggiornamentoVariazione() {
		return "Dati aggiornamento rivalutazione";
	}
	@Override
	public String getTestoSelectTipoVariazione() {
		return "I -  Incremento";
	}

}
