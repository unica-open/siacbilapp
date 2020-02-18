/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentgest;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;

/**
 * Model per la visualizzazione dei risultati di ricerca massiva per il Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 23/09/2013
 * 
 */
public class RisultatiRicercaMassivaCapitoloEntrataGestioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4720878548366477141L;
	// Property necessarie per pilotare la dataTable con il plugin json
	private int sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private String iDisplayStart;
	private String iDisplayLength;
	
	private int savedDisplayStart;
	
	private List<ElementoCapitolo> aaData = new ArrayList<ElementoCapitolo>();
	
	// Per le azioni da delegare all'esterno
	// Aggiornamento
	private Integer annoCapitoloDaAggiornare;
	private Integer numeroCapitoloDaAggiornare;
	private Integer numeroArticoloDaAggiornare;
	// COnsultazione
	private Integer annoCapitoloDaConsultare;
	private Integer numeroCapitoloDaConsultare;
	private Integer numeroArticoloDaConsultare;

	/*Totale Importi */
	private ImportiCapitoloEG totaleImporti = new ImportiCapitoloEG();
	
	/** Costruttore vuoto di default */
	 public RisultatiRicercaMassivaCapitoloEntrataGestioneModel() {
		super();
		setTitolo("Risultati di ricerca Capitolo Entrata Gestione (Massivo)");
	 }
	 
	 /* Getter e Setter */

	/**
	 * @return the sEcho
	 */
	public int getsEcho() {
		return sEcho;
	}

	/**
	 * @param sEcho the sEcho to set
	 */
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	/**
	 * @return the iTotalRecords
	 */
	public String getiTotalRecords() {
		return iTotalRecords;
	}

	/**
	 * @param iTotalRecords the iTotalRecords to set
	 */
	public void setiTotalRecords(String iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	/**
	 * @return the iTotalDisplayRecords
	 */
	public String getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	/**
	 * @param iTotalDisplayRecords the iTotalDisplayRecords to set
	 */
	public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	/**
	 * @return the iDisplayStart
	 */
	public String getiDisplayStart() {
		return iDisplayStart;
	}

	/**
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(String iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	/**
	 * @return the iDisplayLength
	 */
	public String getiDisplayLength() {
		return iDisplayLength;
	}

	/**
	 * @param iDisplayLength the iDisplayLength to set
	 */
	public void setiDisplayLength(String iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	/**
	 * @return the savedDisplayStart
	 */
	public int getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(int savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}

	/**
	 * @return the aaData
	 */
	public List<ElementoCapitolo> getAaData() {
		return aaData;
	}

	/**
	 * @param aaData the aaData to set
	 */
	public void setAaData(List<ElementoCapitolo> aaData) {
		this.aaData = aaData;
	}

	/**
	 * @return the annoCapitoloDaAggiornare
	 */
	public Integer getAnnoCapitoloDaAggiornare() {
		return annoCapitoloDaAggiornare;
	}

	/**
	 * @param annoCapitoloDaAggiornare the annoCapitoloDaAggiornare to set
	 */
	public void setAnnoCapitoloDaAggiornare(Integer annoCapitoloDaAggiornare) {
		this.annoCapitoloDaAggiornare = annoCapitoloDaAggiornare;
	}

	/**
	 * @return the numeroCapitoloDaAggiornare
	 */
	public Integer getNumeroCapitoloDaAggiornare() {
		return numeroCapitoloDaAggiornare;
	}

	/**
	 * @param numeroCapitoloDaAggiornare the numeroCapitoloDaAggiornare to set
	 */
	public void setNumeroCapitoloDaAggiornare(Integer numeroCapitoloDaAggiornare) {
		this.numeroCapitoloDaAggiornare = numeroCapitoloDaAggiornare;
	}

	/**
	 * @return the numeroArticoloDaAggiornare
	 */
	public Integer getNumeroArticoloDaAggiornare() {
		return numeroArticoloDaAggiornare;
	}

	/**
	 * @param numeroArticoloDaAggiornare the numeroArticoloDaAggiornare to set
	 */
	public void setNumeroArticoloDaAggiornare(Integer numeroArticoloDaAggiornare) {
		this.numeroArticoloDaAggiornare = numeroArticoloDaAggiornare;
	}

	/**
	 * @return the annoCapitoloDaConsultare
	 */
	public Integer getAnnoCapitoloDaConsultare() {
		return annoCapitoloDaConsultare;
	}

	/**
	 * @param annoCapitoloDaConsultare the annoCapitoloDaConsultare to set
	 */
	public void setAnnoCapitoloDaConsultare(Integer annoCapitoloDaConsultare) {
		this.annoCapitoloDaConsultare = annoCapitoloDaConsultare;
	}

	/**
	 * @return the numeroCapitoloDaConsultare
	 */
	public Integer getNumeroCapitoloDaConsultare() {
		return numeroCapitoloDaConsultare;
	}

	/**
	 * @param numeroCapitoloDaConsultare the numeroCapitoloDaConsultare to set
	 */
	public void setNumeroCapitoloDaConsultare(Integer numeroCapitoloDaConsultare) {
		this.numeroCapitoloDaConsultare = numeroCapitoloDaConsultare;
	}

	/**
	 * @return the numeroArticoloDaConsultare
	 */
	public Integer getNumeroArticoloDaConsultare() {
		return numeroArticoloDaConsultare;
	}

	/**
	 * @param numeroArticoloDaConsultare the numeroArticoloDaConsultare to set
	 */
	public void setNumeroArticoloDaConsultare(Integer numeroArticoloDaConsultare) {
		this.numeroArticoloDaConsultare = numeroArticoloDaConsultare;
	}

	/**
	 * @return the totaleImporti
	 */
	public ImportiCapitoloEG getTotaleImporti() {
		return totaleImporti;
	}

	/**
	 * @param totaleImporti the totaleImporti to set
	 */
	public void setTotaleImporti(ImportiCapitoloEG totaleImporti) {
		this.totaleImporti = totaleImporti;
	}
	
	
}
