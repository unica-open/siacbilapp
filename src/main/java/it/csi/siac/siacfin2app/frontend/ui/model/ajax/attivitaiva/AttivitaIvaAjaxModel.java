/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.attivitaiva;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;

/**
 * Classe di model per la ricerca AJAX dell'AttivitaIva
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 05/06/2014
 *
 */
public class AttivitaIvaAjaxModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8481758298090419317L;
	
	private AttivitaIva attivitaIva;
	private List<AttivitaIva> listaAttivitaIva = new ArrayList<AttivitaIva>();
	
	/** Costruttore vuoto di default */
	public AttivitaIvaAjaxModel() {
		super();
		setTitolo("Ajax Model");
	}

	/**
	 * @return the attivitaIva
	 */
	public AttivitaIva getAttivitaIva() {
		return attivitaIva;
	}

	/**
	 * @param attivitaIva the attivitaIva to set
	 */
	public void setAttivitaIva(AttivitaIva attivitaIva) {
		this.attivitaIva = attivitaIva;
	}

	/**
	 * @return the listaAttivitaIva
	 */
	public List<AttivitaIva> getListaAttivitaIva() {
		return listaAttivitaIva;
	}

	/**
	 * @param listaAttivitaIva the listaAttivitaIva to set
	 */
	public void setListaAttivitaIva(List<AttivitaIva> listaAttivitaIva) {
		this.listaAttivitaIva = listaAttivitaIva;
	}
	
	/* **** Request **** */
	/**
	 * Crea una request per il servizio di {@link RicercaAttivitaIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaAttivitaIva creaRequestRicercaAttivitaIva() {
		RicercaAttivitaIva request = new RicercaAttivitaIva();
		
		request.setDataOra(now());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setAttivitaIva(getAttivitaIva());
		
		return request;
	}
	
}
