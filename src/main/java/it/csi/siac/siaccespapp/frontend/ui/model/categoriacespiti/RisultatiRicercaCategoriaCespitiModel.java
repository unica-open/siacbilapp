/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.categoriacespiti;

import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaCategoriaCespiti;
import it.csi.siac.siaccespser.model.CategoriaCespiti;

/**
 * The Class GenericTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class RisultatiRicercaCategoriaCespitiModel extends GenericCategoriaCespitiModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8711770091204035728L;
	
	// SIAC-6377
	private boolean force;
	
	/**
	 * Instantiates a new risultati ricerca categoria cespiti model.
	 */
	public RisultatiRicercaCategoriaCespitiModel() {
		setTitolo("Risultati ricerca categoria cespiti");
	}
	
	/**
	 * @return the force
	 */
	public boolean isForce() {
		return this.force;
	}

	/**
	 * @param force the force to set
	 */
	public void setForce(boolean force) {
		this.force = force;
	}

	/**
	 * Componi categoria cespiti da uid.
	 *
	 * @return the categoria cespiti
	 */
	private CategoriaCespiti componiCategoriaCespitiDaUid() {
		CategoriaCespiti cat = new CategoriaCespiti();
		cat.setUid(getUidCategoriaCespiti());
		return cat;
	}


	/**
	 * Crea request elimina categoria cespiti.
	 *
	 * @return the elimina categoria cespiti
	 */
	public EliminaCategoriaCespiti creaRequestEliminaCategoriaCespiti() {
		EliminaCategoriaCespiti request = creaRequest(EliminaCategoriaCespiti.class);
		request.setCategoriaCespiti(componiCategoriaCespitiDaUid());
		return request;
	}

	/**
	 * Crea request annulla categoria cespiti.
	 *
	 * @return the annulla categoria cespiti
	 */
	public AnnullaCategoriaCespiti creaRequestAnnullaCategoriaCespiti() {
		AnnullaCategoriaCespiti request = creaRequest(AnnullaCategoriaCespiti.class);
		request.setCategoriaCespiti(componiCategoriaCespitiDaUid());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link VerificaAnnullabilitaCategoriaCespiti}
	 * @return la request creata
	 */
	public VerificaAnnullabilitaCategoriaCespiti creaRequestVerificaAnnullabilitaCategoriaCespiti() {
		VerificaAnnullabilitaCategoriaCespiti req = creaRequest(VerificaAnnullabilitaCategoriaCespiti.class);
		req.setCategoriaCespiti(componiCategoriaCespitiDaUid());
		return req;
	}
	
}
