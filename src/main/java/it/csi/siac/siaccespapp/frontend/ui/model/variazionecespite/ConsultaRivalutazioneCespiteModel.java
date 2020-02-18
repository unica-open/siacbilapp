/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite;

/**
 * Model per la consultazione della rivalutazione del cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/08/2018
 *
 */
public class ConsultaRivalutazioneCespiteModel extends BaseConsultaVariazioneCespiteModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7822396481001508162L;

	/** Costruttore vuoto di default */
	public ConsultaRivalutazioneCespiteModel() {
		setTitolo("Inventario - Consulta Rivalutazione Cespite");
	}
	
	@Override
	public String getTipoVariazione() {
		return "I - incremento";
	}

	@Override
	public String getFormTitle() {
		return "Consulta rivalutazione";
	}

	@Override
	public String getBoxTitle() {
		return "Dati rivalutazione";
	}
	
}
