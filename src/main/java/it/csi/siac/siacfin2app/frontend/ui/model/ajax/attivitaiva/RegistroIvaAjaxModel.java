/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.attivitaiva;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * Classe di model per la ricerca AJAX del RegistroIva
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 12/06/2014
 *
 */
public class RegistroIvaAjaxModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2282618328272494720L;
	
	private RegistroIva registroIva;
	private List<RegistroIva> listaRegistroIva = new ArrayList<RegistroIva>();
	
	/** Costruttore vuoto di default */
	public RegistroIvaAjaxModel() {
		super();
		setTitolo("Ajax Model");
	}

	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

	/**
	 * @param registroIva the registroIva to set
	 */
	public void setRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}

	/**
	 * @return the listaRegistroIva
	 */
	public List<RegistroIva> getListaRegistroIva() {
		return listaRegistroIva;
	}

	/**
	 * @param listaRegistroIva the listaRegistroIva to set
	 */
	public void setListaRegistroIva(List<RegistroIva> listaRegistroIva) {
		this.listaRegistroIva = listaRegistroIva;
	}

	/* **** Request **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaRegistroIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaRegistroIva creaRequestRicercaRegistroIva() {
		RicercaRegistroIva request = creaRequest(RicercaRegistroIva.class);
		
		RegistroIva ri = getRegistroIva();
		ri.setEnte(getEnte());
		request.setRegistroIva(ri);
		
		return request;
	}
	
}
