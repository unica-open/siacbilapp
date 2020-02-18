/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.tipoonere;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaTipoOnere;

/**
 * Classe di model per la ricerca del Tipo Onere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/nov/2014
 *
 */
public class RicercaTipoOnereModel extends GenericTipoOnereModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9159872632192252800L;
	
	private Boolean corsoDiValidita;
	
	/** Costruttore vuoto di default */
	public RicercaTipoOnereModel() {
		setTitolo("Ricerca onere");
	}

	/**
	 * @return the corsoDiValidita
	 */
	public Boolean getCorsoDiValidita() {
		return corsoDiValidita;
	}

	/**
	 * @param corsoDiValidita the corsoDiValidita to set
	 */
	public void setCorsoDiValidita(Boolean corsoDiValidita) {
		this.corsoDiValidita = corsoDiValidita;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaTipoOnere}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaTipoOnere creaRequestRicercaSinteticaTipoOnere() {
		RicercaSinteticaTipoOnere request = creaRequest(RicercaSinteticaTipoOnere.class);
		
		request.setCorsoDiValidita(getCorsoDiValidita());
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setTipoOnere(getTipoOnere());
		
		return request;
	}
	
}
