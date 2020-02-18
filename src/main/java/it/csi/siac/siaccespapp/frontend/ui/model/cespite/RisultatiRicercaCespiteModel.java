/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.cespite;

import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCespite;
import it.csi.siac.siaccespser.model.Cespite;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class RisultatiRicercaCespiteModel extends GenericCespiteModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = -3378419133355619823L;


	private int uidDismissioneCollegata;
	
	/**
	 * Instantiates a new risultati ricerca tipo bene model.
	 */
	public RisultatiRicercaCespiteModel() {
		setTitolo("Risultati ricerca cespite");
	}
	
	
	/**
	 * @return the uidDismissioneCollegata
	 */
	public int getUidDismissioneCollegata() {
		return uidDismissioneCollegata;
	}




	/**
	 * @param uidDismissioneCollegata the uidDismissioneCollegata to set
	 */
	public void setUidDismissioneCollegata(int uidDismissioneCollegata) {
		this.uidDismissioneCollegata = uidDismissioneCollegata;
	}




	/**
	 * Componi categoria cespiti da uid.
	 *
	 * @return the categoria cespiti
	 */
	private Cespite componiCespiteDaUid() {
		Cespite cat = new Cespite();
		cat.setUid(getUidCespite());
		return cat;
	}


	/**
	 * Crea request elimina categoria cespiti.
	 *
	 * @return the elimina categoria cespiti
	 */
	public EliminaCespite creaRequestEliminaCespite() {
		EliminaCespite request = creaRequest(EliminaCespite.class);
		request.setCespite(componiCespiteDaUid());
		return request;
	}


}
