/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite;

import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaTipoBeneCespite;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class AggiornaTipoBeneModel extends GenericTipoBeneModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = 1141282659213557260L;	
	
	/**
	 * Costruttore vuoto
	 */
	public AggiornaTipoBeneModel() {
		setTitolo("aggiorna tipo bene");
	}

	/**
	 * Crea request aggiorna tipo bene cespite.
	 *
	 * @return the aggiorna tipo bene cespite
	 */
	public AggiornaTipoBeneCespite creaRequestAggiornaTipoBeneCespite() {
		AggiornaTipoBeneCespite req = creaRequest(AggiornaTipoBeneCespite.class);
		req.setTipoBeneCespite(getTipoBeneCespite());
		return req;
	}

	
	/**
	 * Gets the uid old causale decremento.
	 *
	 * @return the uid old causale decremento
	 */
	public String getUidOldCausaleDecremento() {
		return getTipoBeneCespite() != null && idEntitaPresente(getTipoBeneCespite().getCausaleDecremento())? ("" + getTipoBeneCespite().getCausaleDecremento().getUid()) : "";
	}
	
	/**
	 * Gets the uid old causale incremento.
	 *
	 * @return the uid old causale incremento
	 */
	public String getUidOldCausaleIncremento() {
		return getTipoBeneCespite() != null && idEntitaPresente(getTipoBeneCespite().getCausaleIncremento())? ("" + getTipoBeneCespite().getCausaleIncremento().getUid()) : "";
	}
	
	/**
	 * Gets the uid old causale ammortamento.
	 *
	 * @return the uid old causale ammortamento
	 */
	public String getUidOldCausaleAmmortamento() {
		return getTipoBeneCespite() != null && idEntitaPresente(getTipoBeneCespite().getCausaleAmmortamento())? ("" + getTipoBeneCespite().getCausaleAmmortamento().getUid()) : "";
	}
}
