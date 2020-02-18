/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite;

import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnagraficaDismissioneCespite;

/**
 * The Class InserisciDismissioneCespiteModel.
 * @author elisa
 * @version 1.0.0 - 09-08-2018
 */
public class InserisciDismissioneCespiteModel extends BaseInserisciAggiornaDismissioneCespiteModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1964160202891601532L;
	
	private int uidCespiteCollegamentoAutomatico;
	
	/**
	 * Instantiates a new inserisci tipo bene model.
	 */
	public InserisciDismissioneCespiteModel() {
		setTitolo("dismissione");
	}
	
	
	/**
	 * @return the uidCespite
	 */
	public int getUidCespiteCollegamentoAutomatico() {
		return uidCespiteCollegamentoAutomatico;
	}


	/**
	 * @param uidCespite the uidCespite to set
	 */
	public void setUidCespiteCollegamentoAutomatico(int uidCespite) {
		this.uidCespiteCollegamentoAutomatico = uidCespite;
	}


	/**
	 * Gets the base url.
	 *
	 * @return the base url
	 */
	public String getBaseUrl() {
		return "inserisciDismissioneCespite";
	}

	/**
	 * Crea request inserisci tipo bene cespite.
	 *
	 * @return the inserisci tipo bene cespite
	 */
	public InserisciAnagraficaDismissioneCespite creaRequestInserisciAnagraficaDismissioneCespite() {
		InserisciAnagraficaDismissioneCespite req = creaRequest(InserisciAnagraficaDismissioneCespite.class);
		req.setDismissioneCespite(getDismissioneCespite());
		return req;
	}
	
}
