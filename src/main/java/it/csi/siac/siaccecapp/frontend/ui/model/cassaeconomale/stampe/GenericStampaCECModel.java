/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.stampe;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.CassaEconomaleBaseModel;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoStampa;


/**
 * Classe di model generica per le stampa della CEC.
 * 
 * @author Paggio Simona,Nazha Ahmad
 * @version 1.0.0 - 10/02/2015
 * @version 1.0.1 - 01/04/2015
 */

public class GenericStampaCECModel extends CassaEconomaleBaseModel {

	/* per serializzazione */
	private static final long serialVersionUID = -1851080829775459775L;
	
	private TipoStampa tipoStampa;
	private List<TipoStampa> listaTipoStampa = new ArrayList<TipoStampa>();
	
	//aggiunti il 01_04_2015 sono stati aggiunti su questa classe di model per evitare di replicare il codice*N per tutte le stampe 

	private List<CassaEconomale> listaCasseEconomali=new ArrayList<CassaEconomale>();
	private Boolean unicaCassa = Boolean.TRUE;

	/** Costruttore vuoto di default */
	public GenericStampaCECModel() {
		setTitolo("Stampe cassa");
	}
	/**
	 * @return the tipoStampa
	 */
	public TipoStampa getTipoStampa() {
		return this.tipoStampa;
	}

	/**
	 * @param tipoStampa the tipoStampa to set
	 */
	public void setTipoStampa(TipoStampa tipoStampa) {
		this.tipoStampa = tipoStampa;
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
	 * @return the urlStampaGiornaleCassa
	 */
	public String getUrlStampaGiornaleCassa() {
		return "cassaEconomaleStampe_giornaleCassa.do";
	}
	/**
	 * @return the urlStampaRendiconto
	 */
	public String getUrlStampaRendiconto() {
		return "cassaEconomaleStampe_rendiconto.do";
	}
	
	/**
	 * @return the listaCasseEconomali
	 */
	public List<CassaEconomale> getListaCasseEconomali() {
		return listaCasseEconomali;
	}
	/**
	 * @param listaCasseEconomali the listaCasseEconomali to set
	 */
	public void setListaCasseEconomali(List<CassaEconomale> listaCasseEconomali) {
		this.listaCasseEconomali = listaCasseEconomali;
	}
	/**
	 * @return the unicaCassa
	 */
	public Boolean getUnicaCassa() {
		return unicaCassa;
	}
	/**
	 * @param unicaCassa the unicaCassa to set
	 */
	public void setUnicaCassa(Boolean unicaCassa) {
		this.unicaCassa = unicaCassa;
	}



}
