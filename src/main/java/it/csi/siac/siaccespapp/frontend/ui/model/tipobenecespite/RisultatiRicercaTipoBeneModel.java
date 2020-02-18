/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite;

import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaTipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class RisultatiRicercaTipoBeneModel extends GenericTipoBeneModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = -182662157775360367L;
	
	private Integer annoAnnullamento;
	// SIAC-6376
	private boolean force;
	
	/**
	 * Instantiates a new risultati ricerca tipo bene model.
	 */
	public RisultatiRicercaTipoBeneModel() {
		setTitolo("Risultati ricerca tipo bene");
	}

	/**
	 * @return the annoAnnullamento
	 */
	public Integer getAnnoAnnullamento() {
		return this.annoAnnullamento;
	}

	/**
	 * @param annoAnnullamento the annoAnnullamento to set
	 */
	public void setAnnoAnnullamento(Integer annoAnnullamento) {
		this.annoAnnullamento = annoAnnullamento;
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
	private TipoBeneCespite componiTipoBeneCespiteDaUid() {
		TipoBeneCespite cat = new TipoBeneCespite();
		cat.setUid(getUidTipoBeneCespite());
		return cat;
	}


	/**
	 * Crea request elimina categoria cespiti.
	 *
	 * @return the elimina categoria cespiti
	 */
	public EliminaTipoBeneCespite creaRequestEliminaTipoBeneCespite() {
		EliminaTipoBeneCespite request = creaRequest(EliminaTipoBeneCespite.class);
		request.setTipoBeneCespite(componiTipoBeneCespiteDaUid());
		return request;
	}

	/**
	 * Crea request annulla tipo bene cespite.
	 *
	 * @return the annulla tipo bene cespite
	 */
	public AnnullaTipoBeneCespite creaRequestAnnullaTipoBeneCespite() {
		AnnullaTipoBeneCespite request = creaRequest(AnnullaTipoBeneCespite.class);
		request.setTipoBeneCespite(componiTipoBeneCespiteDaUid());
		request.setAnnoAnnullamento(getAnnoAnnullamento());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link VerificaAnnullabilitaTipoBeneCespite}
	 * @return la request creata
	 */
	public VerificaAnnullabilitaTipoBeneCespite creaRequestVerificaAnnullabilitaTipoBeneCespite() {
		VerificaAnnullabilitaTipoBeneCespite req = creaRequest(VerificaAnnullabilitaTipoBeneCespite.class);
		req.setTipoBeneCespite(componiTipoBeneCespiteDaUid());
		return req;
	}
}
