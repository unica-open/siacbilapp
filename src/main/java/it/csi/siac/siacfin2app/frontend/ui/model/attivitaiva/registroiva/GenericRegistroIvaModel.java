/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

/**
 * Classe di model generica per il RegistroIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/05/2014
 *
 */
public class GenericRegistroIvaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8463527240509059716L;
	
	private RegistroIva registroIva;
	private GruppoAttivitaIva gruppoAttivitaIva;
	private TipoRegistroIva tipoRegistroIva;
	
	private List<GruppoAttivitaIva> listaGruppoAttivitaIva = new ArrayList<GruppoAttivitaIva>();
	private List<TipoRegistroIva> listaTipoRegistroIva = new ArrayList<TipoRegistroIva>();
	
	private Integer uidRegistroIva;

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
	 * @return the gruppoAttivitaIva
	 */
	public GruppoAttivitaIva getGruppoAttivitaIva() {
		return gruppoAttivitaIva;
	}

	/**
	 * @param gruppoAttivitaIva the gruppoAttivitaIva to set
	 */
	public void setGruppoAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva) {
		this.gruppoAttivitaIva = gruppoAttivitaIva;
	}

	/**
	 * @return the tipoRegistroIva
	 */
	public TipoRegistroIva getTipoRegistroIva() {
		return tipoRegistroIva;
	}

	/**
	 * @param tipoRegistroIva the tipoRegistroIva to set
	 */
	public void setTipoRegistroIva(TipoRegistroIva tipoRegistroIva) {
		this.tipoRegistroIva = tipoRegistroIva;
	}

	/**
	 * @return the listaGruppoAttivitaIva
	 */
	public List<GruppoAttivitaIva> getListaGruppoAttivitaIva() {
		return listaGruppoAttivitaIva;
	}

	/**
	 * @param listaGruppoAttivitaIva the listaGruppoAttivitaIva to set
	 */
	public void setListaGruppoAttivitaIva(
			List<GruppoAttivitaIva> listaGruppoAttivitaIva) {
		this.listaGruppoAttivitaIva = listaGruppoAttivitaIva;
	}

	/**
	 * @return the listaTipoRegistroIva
	 */
	public List<TipoRegistroIva> getListaTipoRegistroIva() {
		return listaTipoRegistroIva;
	}

	/**
	 * @param listaTipoRegistroIva the listaTipoRegistroIva to set
	 */
	public void setListaTipoRegistroIva(List<TipoRegistroIva> listaTipoRegistroIva) {
		this.listaTipoRegistroIva = listaTipoRegistroIva;
	}

	/**
	 * @return the uidRegistroIva
	 */
	public Integer getUidRegistroIva() {
		return uidRegistroIva;
	}

	/**
	 * @param uidRegistroIva the uidRegistroIva to set
	 */
	public void setUidRegistroIva(Integer uidRegistroIva) {
		this.uidRegistroIva = uidRegistroIva;
	}
	
	/* **** Request **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaGruppoAttivitaIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaGruppoAttivitaIva creaRequestRicercaGruppoAttivitaIva() {
		RicercaGruppoAttivitaIva request = new RicercaGruppoAttivitaIva();
		
		request.setDataOra(now());
		request.setGruppoAttivitaIva(creaGruppoAttivitaIva());
		request.setRichiedente(getRichiedente());
		
		return request;
	}
	
	/* **** utilita' **** */
	
	/**
	 * Imposta i dati del registro iva.
	 * 
	 * @param registro il registro da cui popolare il model
	 */
	public void impostaDati(RegistroIva registro) {
		setRegistroIva(registro);
		setGruppoAttivitaIva(registro.getGruppoAttivitaIva());
		setTipoRegistroIva(registro.getTipoRegistroIva());
		
		setUidRegistroIva(registro.getUid());
	}
	
	/**
	 * Crea un Gruppo Attivita Iva per la ricerca.
	 * 
	 * @return il gruppo creato
	 */
	private GruppoAttivitaIva creaGruppoAttivitaIva() {
		GruppoAttivitaIva gai = new GruppoAttivitaIva();
		gai.setEnte(getEnte());
		return gai;
	}
	
	/**
	 * Crea un RegistroIva.
	 * 
	 * @return il Registro creato
	 */
	protected RegistroIva creaRegistroIva() {
		RegistroIva ri = getRegistroIva();
		
		ri.setGruppoAttivitaIva(getGruppoAttivitaIva());
		ri.setTipoRegistroIva(getTipoRegistroIva());
		ri.setEnte(getEnte());
		
		return ri;
	}
	
	/**
	 * Crea un RegistroIva a partire dall'uid dello stesso.
	 * 
	 * @param uid l'uid tramite cui creare il registro
	 * 
	 * @return il registro corrispondente all'uid
	 */
	protected RegistroIva creaRegistroIva(Integer uid) {
		RegistroIva ri = new RegistroIva();
		
		ri.setUid(uid);
		ri.setEnte(getEnte());
		
		return ri;
	}
	
}
