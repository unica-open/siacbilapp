/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.stampa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoChiusura;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoStampa;

/**
 * Classe di model generica per le stampa dell'iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/07/2014
 *
 */
public abstract class GenericStampaIvaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2913331277748746802L;
	
	private GruppoAttivitaIva gruppoAttivitaIva;
	private TipoChiusura tipoChiusura;
	private Periodo periodo;
	private TipoStampa tipoStampa;
	
	private BigDecimal ivaACredito = BigDecimal.ZERO;
	
	private List<GruppoAttivitaIva> listaGruppoAttivitaIva = new ArrayList<GruppoAttivitaIva>();
	private List<TipoChiusura> listaTipoChiusura = new ArrayList<TipoChiusura>();
	private List<Periodo> listaPeriodo = new ArrayList<Periodo>();
	private List<TipoStampa> listaTipoStampa = new ArrayList<TipoStampa>();
	
	private String templateConferma;
	
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
	 * @return the periodo
	 */
	public Periodo getPeriodo() {
		return periodo;
	}

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	/**
	 * @return the tipoStampa
	 */
	public TipoStampa getTipoStampa() {
		return tipoStampa;
	}

	/**
	 * @param tipoStampa the tipoStampa to set
	 */
	public void setTipoStampa(TipoStampa tipoStampa) {
		this.tipoStampa = tipoStampa;
	}
	
	/**
	 * @return the ivaACredito
	 */
	public BigDecimal getIvaACredito() {
		return ivaACredito;
	}

	/**
	 * @param ivaACredito the ivaACredito to set
	 */
	public void setIvaACredito(BigDecimal ivaACredito) {
		this.ivaACredito = ivaACredito;
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
	 * @return the listaTipoChiusura
	 */
	public List<TipoChiusura> getListaTipoChiusura() {
		return listaTipoChiusura;
	}

	/**
	 * @param listaTipoChiusura the listaTipoChiusura to set
	 */
	public void setListaTipoChiusura(List<TipoChiusura> listaTipoChiusura) {
		this.listaTipoChiusura = listaTipoChiusura;
	}

	/**
	 * @return the listaPeriodo
	 */
	public List<Periodo> getListaPeriodo() {
		return listaPeriodo;
	}

	/**
	 * @param listaPeriodo the listaPeriodo to set
	 */
	public void setListaPeriodo(List<Periodo> listaPeriodo) {
		this.listaPeriodo = listaPeriodo;
	}

	/**
	 * @return the listaTipoStampa
	 */
	public List<TipoStampa> getListaTipoStampa() {
		return listaTipoStampa;
	}

	/**
	 * @param listaTipoStampa the listaTipoStampa to set
	 */
	public void setListaTipoStampa(List<TipoStampa> listaTipoStampa) {
		this.listaTipoStampa = listaTipoStampa;
	}
	
	/**
	 * @return the templateConferma
	 */
	public String getTemplateConferma() {
		return templateConferma;
	}

	/**
	 * @param templateConferma the templateConferma to set
	 */
	public void setTemplateConferma(String templateConferma) {
		this.templateConferma = templateConferma;
	}

	/**
	 * @return the periodoAbilitato
	 */
	public Boolean getPeriodoAbilitato() {
		return getGruppoAttivitaIva() != null && getGruppoAttivitaIva().getUid() != 0;
	}
	
	/**
	 * @return the azioneIndietro
	 */
	public abstract String getAzioneIndietro();
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaGruppoAttivitaIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaGruppoAttivitaIva creaRequestRicercaGruppoAttivitaIva() {
		RicercaGruppoAttivitaIva request = creaRequest(RicercaGruppoAttivitaIva.class);
		
		GruppoAttivitaIva gai = new GruppoAttivitaIva();
		gai.setEnte(getEnte());
		request.setGruppoAttivitaIva(gai);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaGruppoAttivitaIva} a partire dal gruppo fornito.
	 * 
	 * @param gruppo il gruppo da cercare
	 * 
	 * @return la request creata
	 */
	public RicercaGruppoAttivitaIva creaRequestRicercaGruppoAttivitaIva(GruppoAttivitaIva gruppo) {
		RicercaGruppoAttivitaIva request = creaRequest(RicercaGruppoAttivitaIva.class);
		request.setGruppoAttivitaIva(gruppo);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaRegistroIva}.
	 * 
	 * @param gruppo il gruppo tramite cui effettuare la ricerca
	 * @param tipo   il tipo tramite cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaRegistroIva creaRequestRicercaRegistroIva(GruppoAttivitaIva gruppo, TipoRegistroIva tipo) {
		RicercaRegistroIva request = creaRequest(RicercaRegistroIva.class);
		
		RegistroIva ri = new RegistroIva();
		ri.setEnte(getEnte());
		ri.setGruppoAttivitaIva(gruppo);
		ri.setTipoRegistroIva(tipo);
		request.setRegistroIva(ri);
		
		return request;
	}
	
}
