/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.categoriacespiti;

import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespiti;

/**
 * The Class GenericTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class RicercaCategoriaCespitiModel extends GenericCategoriaCespitiModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4438808752791737126L;

	/**
	 * costruttore
	 */
	public RicercaCategoriaCespitiModel() {
		setTitolo("Ricerca categoria cespiti");
	}
	
	/**
	 * Crea request ricerca sintetica categoria cespiti.
	 *
	 * @return the ricerca sintetica categoria cespiti
	 */
	public RicercaSinteticaCategoriaCespiti creaRequestRicercaSinteticaCategoriaCespiti() {
		RicercaSinteticaCategoriaCespiti request = creaRequest(RicercaSinteticaCategoriaCespiti.class);
		request.setCategoriaCespiti(getCategoriaCespiti());
		request.getCategoriaCespiti().setAmbito(Ambito.AMBITO_FIN);
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}

}
