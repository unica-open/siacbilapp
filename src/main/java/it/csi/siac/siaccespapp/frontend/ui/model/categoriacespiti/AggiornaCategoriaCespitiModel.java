/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.categoriacespiti;

import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCategoriaCespiti;

/**
 * The Class GenericTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class AggiornaCategoriaCespitiModel extends GenericCategoriaCespitiModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8235776096798324205L;

	/** Costruttore vuoto di default */
	public AggiornaCategoriaCespitiModel() {
		setTitolo("Aggiorna categoria cespite");
	}
	/**
	 * Crea request aggiorna categoria cespiti.
	 *
	 * @return the aggiorna categoria cespiti
	 */
	public AggiornaCategoriaCespiti creaRequestAggiornaCategoriaCespiti() {
		AggiornaCategoriaCespiti req = creaRequest(AggiornaCategoriaCespiti.class);
		req.setCategoriaCespiti(getCategoriaCespiti());
		return req;
	}
	
}
