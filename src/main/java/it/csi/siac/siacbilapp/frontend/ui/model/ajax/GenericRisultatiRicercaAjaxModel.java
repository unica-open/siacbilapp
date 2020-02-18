/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;

/**
 * Classe di model generica per le interazioni AJAX per i risultati di ricerca
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/01/2014
 *
 * @param <S> i dati ottenuti per l'AJAX, implementanti {@link Serializable}
 */
public abstract class GenericRisultatiRicercaAjaxModel<S extends Serializable> extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1907325698021102792L;
	
	private String sEcho;
	private int iTotalRecords;
	private int iTotalDisplayRecords;
	private int iDisplayStart;
	private int iDisplayLength;
	private List<S> aaData = new ArrayList<S>();
	private boolean forceRefresh;
	private Map<String, Object> moreData = new HashMap<String, Object>();
	
	/**
	 * @return the sEcho
	 */
	public String getsEcho() {
		return sEcho;
	}
	
	/**
	 * @param sEcho the sEcho to set
	 */
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
	
	/**
	 * @return the iTotalRecords
	 */
	public int getiTotalRecords() {
		return iTotalRecords;
	}
	
	/**
	 * @param iTotalRecords the iTotalRecords to set
	 */
	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	
	/**
	 * @return the iTotalDisplayRecords
	 */
	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	
	/**
	 * @param iTotalDisplayRecords the iTotalDisplayRecords to set
	 */
	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	
	/**
	 * @return the iDisplayStart
	 */
	public int getiDisplayStart() {
		return iDisplayStart;
	}
	
	/**
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}
	
	/**
	 * @return the iDisplayLength
	 */
	public int getiDisplayLength() {
		return iDisplayLength;
	}
	
	/**
	 * @param iDisplayLength the iDisplayLength to set
	 */
	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}
	
	/**
	 * @return the aaData
	 */
	public List<S> getAaData() {
		return aaData;
	}
	
	/**
	 * @param aaData the aaData to set
	 */
	public void setAaData(List<S> aaData) {
		this.aaData = aaData != null ? aaData : new ArrayList<S>();
	}

	/**
	 * @return the forceRefresh
	 */
	public boolean isForceRefresh() {
		return forceRefresh;
	}

	/**
	 * @param forceRefresh the forceRefresh to set
	 */
	public void setForceRefresh(boolean forceRefresh) {
		this.forceRefresh = forceRefresh;
	}

	/**
	 * @return the moreData
	 */
	public Map<String, Object> getMoreData() {
		return moreData;
	}

	/**
	 * @param moreData the moreData to set
	 */
	public void setMoreData(Map<String, Object> moreData) {
		this.moreData = moreData != null ? moreData : new HashMap<String, Object>();
	}

	/**
	 * Aggiunge dati ulteriori all'interno della mappa
	 * @param key chiave associata al dato aggiunto
	 * @param value dato aggiunto
	 */
	public void addMoreData(String key, Object value) {
		moreData.put(key, value);
	}
	
}
