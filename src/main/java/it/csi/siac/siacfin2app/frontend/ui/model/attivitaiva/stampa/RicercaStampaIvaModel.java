/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.stampa;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;

/**
 * Classe di model per la ricerca della stampa iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 19/01/2015
 *
 */
public class RicercaStampaIvaModel extends GenericStampaIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9102841609136343520L;
	
	private StampaIva stampaIva;
	private Boolean flagDocumentiPagati;
	private Boolean flagDocumentiIncassati;
	private RegistroIva registroIva;
	private String nomeFile;
	
	private List<TipoStampaIva> listaTipoStampaIva = new ArrayList<TipoStampaIva>();
	private List<TipoRegistroIva> listaTipoRegistroIva = new ArrayList<TipoRegistroIva>();
	private List<RegistroIva> listaRegistroIva = new ArrayList<RegistroIva>();
	
	/** Costruttore vuoto di default */
	public RicercaStampaIvaModel() {
		super();
		setTitolo("Ricerca stampe iva");
	}

	/**
	 * @return the stampaIva
	 */
	public StampaIva getStampaIva() {
		return stampaIva;
	}

	/**
	 * @param stampaIva the stampaIva to set
	 */
	public void setStampaIva(StampaIva stampaIva) {
		this.stampaIva = stampaIva;
	}

	/**
	 * @return the flagDocumentiPagati
	 */
	public Boolean getFlagDocumentiPagati() {
		return flagDocumentiPagati;
	}
	
	/**
	 * @return the flagDocumentiPagati, not null
	 */
	public Boolean getFlagDocumentiPagatiNotNull() {
		return flagDocumentiPagati == null ? Boolean.FALSE : flagDocumentiPagati;
	}

	/**
	 * @param flagDocumentiPagati the flagDocumentiPagati to set
	 */
	public void setFlagDocumentiPagati(Boolean flagDocumentiPagati) {
		this.flagDocumentiPagati = flagDocumentiPagati;
	}

	/**
	 * @return the flagDocumentiIncassati
	 */
	public Boolean getFlagDocumentiIncassati() {
		return flagDocumentiIncassati;
	}
	
	/**
	 * @return the flagDocumentiIncassati, not null
	 */
	public Boolean getFlagDocumentiIncassatiNotNull() {
		return flagDocumentiIncassati == null ? Boolean.FALSE : flagDocumentiIncassati;
	}

	/**
	 * @param flagDocumentiIncassati the flagDocumentiIncassati to set
	 */
	public void setFlagDocumentiIncassati(Boolean flagDocumentiIncassati) {
		this.flagDocumentiIncassati = flagDocumentiIncassati;
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
	 * @return the nomeFile
	 */
	public String getNomeFile() {
		return nomeFile;
	}

	/**
	 * @param nomeFile the nomeFile to set
	 */
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	/**
	 * @return the listaTipoStampaIva
	 */
	public List<TipoStampaIva> getListaTipoStampaIva() {
		return listaTipoStampaIva;
	}

	/**
	 * @param listaTipoStampaIva the listaTipoStampaIva to set
	 */
	public void setListaTipoStampaIva(List<TipoStampaIva> listaTipoStampaIva) {
		this.listaTipoStampaIva = listaTipoStampaIva != null ? listaTipoStampaIva : new ArrayList<TipoStampaIva>();
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
		this.listaTipoRegistroIva = listaTipoRegistroIva != null ? listaTipoRegistroIva : new ArrayList<TipoRegistroIva>();
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
		this.listaRegistroIva = listaRegistroIva != null ? listaRegistroIva : new ArrayList<RegistroIva>();
	}
	
	/**
	 * @return the documentiPagatiVisibile
	 */
	public boolean getDocumentiPagatiVisibile() {
		return getRegistroIva() != null && TipoRegistroIva.ACQUISTI_IVA_DIFFERITA.equals(getRegistroIva().getTipoRegistroIva());
	}
	
	/**
	 * @return the documentiIncassatiVisibile
	 */
	public boolean getDocumentiIncassatiVisibile() {
		return getRegistroIva() != null && TipoRegistroIva.VENDITE_IVA_DIFFERITA.equals(getRegistroIva().getTipoRegistroIva());
	}
	
	/**
	 * @return the registoIvaAbilitato
	 */
	public boolean getRegistroIvaAbilitato() {
		return getRegistroIva() != null && getRegistroIva().getGruppoAttivitaIva() != null
			&& getRegistroIva().getGruppoAttivitaIva().getUid() != 0 && getRegistroIva().getTipoRegistroIva() != null;
	}
	
	@Override
	public Boolean getPeriodoAbilitato() {
		return Boolean.valueOf(getTipoChiusura() != null);
	}
	
	@Override
	public String getAzioneIndietro() {
		return "";
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaStampaIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaStampaIva creaRequestRicercaSinteticaStampaIva() {
		RicercaSinteticaStampaIva request = creaRequest(RicercaSinteticaStampaIva.class);
		
		request.setNomeFile(getNomeFile());
		request.setParametriPaginazione(creaParametriPaginazione());
		
		// Popolamento della stampa
		getStampaIva().setFlagPagati(/*Boolean.TRUE.equals(getFlagDocumentiIncassatiNotNull()) ||*/ Boolean.TRUE.equals(getFlagDocumentiPagatiNotNull()));
		
		getStampaIva().setFlagIncassati(Boolean.TRUE.equals(getFlagDocumentiIncassatiNotNull()) );

		getStampaIva().getListaRegistroIva().add(getRegistroIva());

		request.setStampaIva(getStampaIva());
		
		return request;
	}

}
