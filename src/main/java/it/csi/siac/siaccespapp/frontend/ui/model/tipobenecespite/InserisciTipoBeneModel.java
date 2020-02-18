/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite;

import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciTipoBeneCespite;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class InserisciTipoBeneModel extends GenericTipoBeneModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = 2415246619135080077L;
	
	/**
	 * Instantiates a new inserisci tipo bene model.
	 */
	public InserisciTipoBeneModel() {
		setTitolo("anagrafica tipo bene");
	}
	
	/**
	 * Crea request inserisci tipo bene cespite.
	 *
	 * @return the inserisci tipo bene cespite
	 */
	public InserisciTipoBeneCespite creaRequestInserisciTipoBeneCespite() {
		InserisciTipoBeneCespite req = creaRequest(InserisciTipoBeneCespite.class);
		req.setTipoBeneCespite(getTipoBeneCespite());
		return req;
	}
	
	/**
	 * Gets the uid tipo bene cespite inserito.
	 *
	 * @return the uid tipo bene cespite inserito
	 */
	public int getUidTipoBeneCespiteInserito() {
		return getTipoBeneCespite() != null ? getTipoBeneCespite().getUid() : 0; 
	}

}
