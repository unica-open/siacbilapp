/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.categoriacespiti;

import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCategoriaCespiti;
import it.csi.siac.siaccespser.model.CategoriaCespiti;

/**
 * The Class GenericTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class InserisciCategoriaCespitiModel extends GenericCategoriaCespitiModel {

	/**Per la serializzazione*/
	private static final long serialVersionUID = -7685831791877707751L;

	/** Costruttore vuoto di default */
	public InserisciCategoriaCespitiModel() {
		setTitolo("Inserimento categoria cespite");
	}

	/**
	 * Crea request inserisci categoria cespiti.
	 *
	 * @return the inserisci categoria cespiti
	 */
	public InserisciCategoriaCespiti creaRequestInserisciCategoriaCespiti() {
		InserisciCategoriaCespiti req = creaRequest(InserisciCategoriaCespiti.class);
		req.setCategoriaCespiti(obtainCategoriaCespitePerInserimento());
		return req;
	}
	
	/**
	 * Metodo di utilita Per ottenere la categoria cespiti inserita
	 *
	 * @return the uid categoria cespiti inserita
	 */
	public int getUidCategoriaCespitiInserita() {
		return getCategoriaCespiti() != null ? getCategoriaCespiti().getUid() : 0; 
	}
	
	private CategoriaCespiti obtainCategoriaCespitePerInserimento(){
		CategoriaCespiti catDaInserire = getCategoriaCespiti();
		catDaInserire.setAmbito(Ambito.AMBITO_FIN);
		return catDaInserire;
	}

}
