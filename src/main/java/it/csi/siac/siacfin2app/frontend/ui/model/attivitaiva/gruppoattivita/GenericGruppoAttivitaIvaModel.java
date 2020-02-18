/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.gruppoattivita;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.model.TipoAttivita;
import it.csi.siac.siacfin2ser.model.TipoChiusura;

/**
 * Classe di model generica per il GruppoAttivitaIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/05/2014
 *
 */
public class GenericGruppoAttivitaIvaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7921433043382389550L;
	
	private GruppoAttivitaIva gruppoAttivitaIva;
	private TipoChiusura tipoChiusura;
	private ProRataEChiusuraGruppoIva proRataEChiusuraGruppoIva;
	private TipoAttivita tipoAttivita;
	
	private List<TipoChiusura> listaTipoChiusura = new ArrayList<TipoChiusura>();
	private List<TipoAttivita> listaTipoAttivita = new ArrayList<TipoAttivita>();
	private List<AttivitaIva> listaAttivitaIva = new ArrayList<AttivitaIva>();
	
	private Integer uidGruppoAttivitaIva;
	
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
	 * @return the tipoChiusura
	 */
	public TipoChiusura getTipoChiusura() {
		return tipoChiusura;
	}

	/**
	 * @param tipoChiusura the tipoChiusura to set
	 */
	public void setTipoChiusura(TipoChiusura tipoChiusura) {
		this.tipoChiusura = tipoChiusura;
	}

	/**
	 * @return the proRataEChiusuraGruppoIva
	 */
	public ProRataEChiusuraGruppoIva getProRataEChiusuraGruppoIva() {
		return proRataEChiusuraGruppoIva;
	}

	/**
	 * @param proRataEChiusuraGruppoIva the proRataEChiusuraGruppoIva to set
	 */
	public void setProRataEChiusuraGruppoIva(
			ProRataEChiusuraGruppoIva proRataEChiusuraGruppoIva) {
		this.proRataEChiusuraGruppoIva = proRataEChiusuraGruppoIva;
	}

	/**
	 * @return the tipoAttivita
	 */
	public TipoAttivita getTipoAttivita() {
		return tipoAttivita;
	}

	/**
	 * @param tipoAttivita the tipoAttivita to set
	 */
	public void setTipoAttivita(TipoAttivita tipoAttivita) {
		this.tipoAttivita = tipoAttivita;
	}

	/**
	 * @return the listaTipoChiusura
	 */
	public List<TipoChiusura> getListaTipoChiusura() {
		return listaTipoChiusura;
	}

	/**
	 * @param listaTipoChiusura the listaTipoChiusura to set
	 */
	public void setListaTipoChiusura(List<TipoChiusura> listaTipoChiusura) {
		this.listaTipoChiusura = listaTipoChiusura != null ? listaTipoChiusura : new ArrayList<TipoChiusura>();
	}

	/**
	 * @return the listaTipoAttivita
	 */
	public List<TipoAttivita> getListaTipoAttivita() {
		return listaTipoAttivita;
	}

	/**
	 * @param listaTipoAttivita the listaTipoAttivita to set
	 */
	public void setListaTipoAttivita(List<TipoAttivita> listaTipoAttivita) {
		this.listaTipoAttivita = listaTipoAttivita != null ? listaTipoAttivita : new ArrayList<TipoAttivita>();
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
		this.listaAttivitaIva = listaAttivitaIva != null ? listaAttivitaIva : new ArrayList<AttivitaIva>();
	}

	/**
	 * @return the uidGruppoAttivitaIva
	 */
	public Integer getUidGruppoAttivitaIva() {
		return uidGruppoAttivitaIva;
	}

	/**
	 * @param uidGruppoAttivitaIva the uidGruppoAttivitaIva to set
	 */
	public void setUidGruppoAttivitaIva(Integer uidGruppoAttivitaIva) {
		this.uidGruppoAttivitaIva = uidGruppoAttivitaIva;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaAttivitaIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaAttivitaIva creaRequestRicercaAttivitaIva() {
		RicercaAttivitaIva request = creaRequest(RicercaAttivitaIva.class);
		
		request.setEnte(getEnte());
		
		AttivitaIva attivitaIva = new AttivitaIva();
		request.setAttivitaIva(attivitaIva);
		
		return request;
	}
	
	/* **** Metodi di utilita' **** */

	/**
	 * Imposta i dati relativi al Gruppo Attivita Iva.
	 * 
	 * @param gruppo il gruppo da cui ottenere i dati
	 */
	public void impostaDati(GruppoAttivitaIva gruppo) {
		setGruppoAttivitaIva(gruppo);
		setTipoChiusura(gruppo.getTipoChiusura());
		setTipoAttivita(gruppo.getTipoAttivita());
		
		// Prendo il primo proRata
		if(!gruppo.getListaProRataEChiusuraGruppoIva().isEmpty()) {
			setProRataEChiusuraGruppoIva(gruppo.getListaProRataEChiusuraGruppoIva().get(0));
		}
		// Imposto l'uid per il reindirizzamento all'aggiornamento
		setUidGruppoAttivitaIva(gruppo.getUid());
	}
	
	/**
	 * Crea un GruppoAttivitaIva a partire da un dato uid.
	 * 
	 * @param uid l'uid a partire da cui creare il gruppo
	 * 
	 * @return il Gruppo creato
	 */
	protected GruppoAttivitaIva creaGruppoAttivitaIva(Integer uid) {
		GruppoAttivitaIva gai = new GruppoAttivitaIva();
		gai.setUid(uid);
		// Generalmente e' ridondante. Ma non fa particolarmente male
		gai.setEnte(getEnte());
		gai.setAnnualizzazione(getAnnoEsercizioInt());
		
		return gai;
	}
	
	/**
	 * Crea un GruppoAttivitaIva.
	 * 
	 * @return il Gruppo creato
	 */
	protected GruppoAttivitaIva creaGruppoAttivitaIva() {
		GruppoAttivitaIva gai = getGruppoAttivitaIva();
		
		gai.setTipoChiusura(getTipoChiusura());
		gai.setTipoAttivita(getTipoAttivita());
		gai.setEnte(getEnte());
		
		// Imposto i proRata nella lista
		gai.getListaProRataEChiusuraGruppoIva().add(creaProRataEChiusuraGruppoIva());
		gai.setAnnualizzazione(getAnnoEsercizioInt());
		
		return gai;
	}

	/**
	 * Crea un ProRataEChiusuraGruppoIva.
	 * 
	 * @return il Gruppo creato
	 */
	private ProRataEChiusuraGruppoIva creaProRataEChiusuraGruppoIva() {
		ProRataEChiusuraGruppoIva precgi = getProRataEChiusuraGruppoIva();
		
		precgi.setAnnoEsercizio(getAnnoEsercizioInt());
		precgi.setEnte(getEnte());
		
		return precgi;
	}
	
}
