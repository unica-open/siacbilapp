/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite;

import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaAnagraficaDismissioneCespite;

/**
 * The Class AggiornaAnagraficaDismissioneCespiteModel.
 * @author elisa
 * @version 1.0.0 - 09-08-2018
 */
public class AggiornaDismissioneCespiteModel extends BaseInserisciAggiornaDismissioneCespiteModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = -3307085305936710021L;

	/**
	 * Costruttore vuoto
	 */
	public AggiornaDismissioneCespiteModel() {
		setTitolo("dismissione");
	}
	
	/**
	 * Gets the base url.
	 *
	 * @return the base url
	 */
	public String getBaseUrl() {
		return "aggiornaDismissioneCespite";
	}

	/**
	 * Crea request aggiorna tipo bene cespite.
	 *
	 * @return the aggiorna tipo bene cespite
	 */
	public AggiornaAnagraficaDismissioneCespite creaRequestAggiornaAnagraficaDismissioneCespite() {
		AggiornaAnagraficaDismissioneCespite req = creaRequest(AggiornaAnagraficaDismissioneCespite.class);
		req.setDismissioneCespite(getDismissioneCespite());
		return req;
	}
	
}
