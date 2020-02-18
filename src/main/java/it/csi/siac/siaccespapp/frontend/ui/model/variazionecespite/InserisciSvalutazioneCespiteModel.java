/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite;

/**
 * The Class InserisciSvalutazioneCespiteModel.
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/08/2018
 */
public class InserisciSvalutazioneCespiteModel extends BaseInserisciVariazioneCespiteModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4238127399042041307L;

	/** Costruttore vuoto di default */
	public InserisciSvalutazioneCespiteModel() {
		setTitolo("Inventario - Svalutazione cespite");
	}

	@Override
	public String getBaseUrl() {
		return "inserisciSvalutazioneCespite";
	}

	@Override
	public String getFormTitle() {
		return "Inserisci svalutazione cespite";
	}

	@Override
	public String getWizardStep3() {
		return "svalutazioni scheda cespite";
	}

	@Override
	public Boolean getFlagTipoVariazioneIncremento() {
		return Boolean.FALSE;
	}

	@Override
	public String getTableTitle() {
		return "Svalutazioni";
	}

	@Override
	public String getButtonNewTitle() {
		return "Inserisci nuova svalutazione";
	}

	@Override
	public String getIntestazioneInserimentoNuovaVariazione() {
		return "Dati inserimento svalutazione";
	}

	@Override
	public String getIntestazioneAggiornamentoVariazione() {
		return "Dati aggiornamento svalutazione";
	}
	
	@Override
	public String getTestoSelectTipoVariazione() {
		return "D -  Decremento";
	}

}
