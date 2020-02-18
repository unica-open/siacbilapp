/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite;

import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespite;
import it.csi.siac.siaccespser.model.DismissioneCespite;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class RisultatiRicercaDismissioneCespiteModel extends GenericDismissioneCespiteModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = 8184476923761020833L;


	/**
	 * Instantiates a new risultati ricerca tipo bene model.
	 */
	public RisultatiRicercaDismissioneCespiteModel() {
		setTitolo("Risultati ricerca dismissione");
	}
	
	
	/**
	 * Componi categoria cespiti da uid.
	 *
	 * @return the categoria cespiti
	 */
	private DismissioneCespite componiCespiteDaUid() {
		DismissioneCespite cat = new DismissioneCespite();
		cat.setUid(getUidDismissioneCespite());
		return cat;
	}


	/**
	 * Crea request elimina categoria cespiti.
	 *
	 * @return the elimina categoria cespiti
	 */
	public EliminaDismissioneCespite creaRequestEliminaDismissioneCespite() {
		EliminaDismissioneCespite request = creaRequest(EliminaDismissioneCespite.class);
		request.setDismissioneCespite(componiCespiteDaUid());
		return request;
	}


	/**
	 * Crea request effettua scritture dismissione cespite.
	 *
	 * @return the effettua scritture dismissione cespite
	 */
	public InserisciPrimeNoteDismissioneCespite creaRequestEffettuaScrittureDismissioneCespite() {
		InserisciPrimeNoteDismissioneCespite req = creaRequest(InserisciPrimeNoteDismissioneCespite.class);
		DismissioneCespite dismissione = new DismissioneCespite();
		dismissione.setUid(getUidDismissioneCespite());
		req.setDismissioneCespite(dismissione);
		
		return req;
	}


}
