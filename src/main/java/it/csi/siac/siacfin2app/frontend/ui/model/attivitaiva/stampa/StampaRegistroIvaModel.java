/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.stampa;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistriIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di model per la stampa del registro iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/07/2014
 *
 */
public class StampaRegistroIvaModel extends GenericStampaIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3794008524755799066L;
	
	private TipoRegistroIva tipoRegistroIva;
	private Boolean flagDocumentiPagati = Boolean.FALSE;
	private Boolean flagDocumentiIncassati = Boolean.FALSE;
	private RegistroIva registroIva;
	
	private List<TipoRegistroIva> listaTipoRegistroIva = new ArrayList<TipoRegistroIva>();
	private List<RegistroIva> listaRegistroIva = new ArrayList<RegistroIva>();
	
	private boolean flagOnlyCheckRegistroIva;

	
	/** Costruttore vuoto di default */
	public StampaRegistroIvaModel() {
		super();
		setTitolo("Stampa registro iva");
		setTemplateConferma(ErroreFin.CONFERMA_STAMPA_REGISTRO_IVA.getErrore("___").getTesto());
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
	 * @return the flagDocumentiPagati
	 */
	public Boolean getFlagDocumentiPagati() {
		return flagDocumentiPagati;
	}
	
	/**
	 * @return the flagDocumentiPagati
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
	 * @return the flagDocumentiIncassati
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

	/**
	 * @return the flagOnlyCheckRegistroIva
	 */
	public boolean isFlagOnlyCheckRegistroIva() {
		return flagOnlyCheckRegistroIva;
	}

	/**
	 * @param flagOnlyCheckRegistroIva the flagOnlyCheckRegistroIva to set
	 */
	public void setFlagOnlyCheckRegistroIva(boolean flagOnlyCheckRegistroIva) {
		this.flagOnlyCheckRegistroIva = flagOnlyCheckRegistroIva;
	}

	/**
	 * @return the documentiPagatiVisibile
	 */
	public Boolean getDocumentiPagatiVisibile() {
		return Boolean.valueOf(TipoRegistroIva.ACQUISTI_IVA_DIFFERITA.equals(getTipoRegistroIva()));
	}
	
	/**
	 * @return the documentiIncassatiVisibile
	 */
	public Boolean getDocumentiIncassatiVisibile() {
		return Boolean.valueOf(TipoRegistroIva.VENDITE_IVA_DIFFERITA.equals(getTipoRegistroIva()));
	}
	
	/**
	 * @return the registoIvaAbilitato
	 */
	public Boolean getRegistroIvaAbilitato() {
		return Boolean.valueOf(getGruppoAttivitaIva() != null && getGruppoAttivitaIva().getUid() != 0 && getTipoRegistroIva() != null);
	}
	
	@Override
	public String getAzioneIndietro() {
		return "stampaRegistroIva";
	}

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link StampaRegistroIva}.
	 * 
	 * @return la request creata
	 */
	public StampaRegistroIva creaRequestStampaRegistroIva() {
		StampaRegistroIva request = creaRequest(StampaRegistroIva.class);
		
		request.setBilancio(getBilancio());
		request.setDocumentiIncassati(getFlagDocumentiIncassatiNotNull());
		request.setDocumentiPagati(getFlagDocumentiPagatiNotNull());
		request.setEnte(getEnte());
		request.setPeriodo(getPeriodo());
		request.setRegistroIva(getRegistroIva());
		request.setTipoStampa(getTipoStampa());
		
		// Imposto il gruppo
		getRegistroIva().setGruppoAttivitaIva(getGruppoAttivitaIva());
		// Imposto il tipo di chiusura
		getGruppoAttivitaIva().setTipoChiusura(getTipoChiusura());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link StampaRegistriIva}.
	 * @param listaRegistriIva la lista dei registri da stampare
	 * @param onlyCheck se sia necessario effettuare solo i controlli
	 * @return la request creata
	 */
	public StampaRegistriIva creaRequestStampaRegistriIva(List<RegistroIva> listaRegistriIva, Boolean onlyCheck){
		StampaRegistriIva request = creaRequest(StampaRegistriIva.class);
		
		request.setBilancio(getBilancio());
		request.setDocumentiIncassati(getFlagDocumentiIncassatiNotNull());
		request.setDocumentiPagati(getFlagDocumentiPagatiNotNull());
		request.setEnte(getEnte());
		request.setPeriodo(getPeriodo()); 
		request.setRegistriIva(listaRegistriIva);
		request.setTipoStampa(getTipoStampa());
		request.setFlagOnlyCheckRegistroIva(onlyCheck);
		
		for (RegistroIva ri : listaRegistriIva) {
			ri.setGruppoAttivitaIva(getGruppoAttivitaIva());
		}
		// Imposto il tipo di chiusura
		getGruppoAttivitaIva().setTipoChiusura(getTipoChiusura());
		
		return request;
	}


}
