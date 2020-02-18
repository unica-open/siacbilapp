/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.progetto;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgetto;
import it.csi.siac.siacbilser.model.Progetto;

/**
 * Model per la visualizzazione dei risultati di ricerca per il Progetto.
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 - 06/02/2014
 * 
 */
public class RisultatiRicercaProgettoModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5713758744308131312L;
	
	// Property necessarie per pilotare la dataTable con  il plugin json
	private int sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private String iDisplayStart;
	private String iDisplayLength;
	
	private int savedDisplayStart;
	
	// Per le azioni da delegare all'esterno
	private int uidDaAggiornare;
	private int uidDaAnnullare;
	private int uidDaConsultare;
	private int uidDaRiattivare;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaProgettoModel() {
		super();
		setTitolo("Risultati di ricerca Progetto");
	}

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
	 * @return the uidDaAggiornare
	 */
	public int getUidDaAggiornare() {
		return uidDaAggiornare;
	}

	/**
	 * @param uidDaAggiornare the uidDaAggiornare to set
	 */
	public void setUidDaAggiornare(int uidDaAggiornare) {
		this.uidDaAggiornare = uidDaAggiornare;
	}

	/**
	 * @return the uidDaAnnullare
	 */
	public int getUidDaAnnullare() {
		return uidDaAnnullare;
	}

	/**
	 * @param uidDaAnnullare the uidDaAnnullare to set
	 */
	public void setUidDaAnnullare(int uidDaAnnullare) {
		this.uidDaAnnullare = uidDaAnnullare;
	}

	/**
	 * @return the uidDaConsultare
	 */
	public int getUidDaConsultare() {
		return uidDaConsultare;
	}

	/**
	 * @param uidDaConsultare the uidDaConsultare to set
	 */
	public void setUidDaConsultare(int uidDaConsultare) {
		this.uidDaConsultare = uidDaConsultare;
	}
	
	/**
	 * @return the uidDaRiattivare
	 */
	public int getUidDaRiattivare() {
		return uidDaRiattivare;
	}

	/**
	 * @param uidDaRiattivare the uidDaRiattivare to set
	 */
	public void setUidDaRiattivare(int uidDaRiattivare) {
		this.uidDaRiattivare = uidDaRiattivare;
	}
	
	/* Requests */
	
	/**
	 * Crea una request per il servizio di Annulla Progetto.
	 * 
	 * @return la request creata
	 */
	public AnnullaProgetto creaRequestAnnullaProgetto() {
		AnnullaProgetto request = creaRequest(AnnullaProgetto.class);
		
		Progetto progetto = new Progetto();
		progetto.setUid(uidDaAnnullare);
		progetto.setEnte(getEnte());
		
		request.setProgetto(progetto);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Riattiva Progetto.
	 * 
	 * @return la request creata
	 */
	public RiattivaProgetto creaRequestRiattivaProgetto() {
		RiattivaProgetto request = creaRequest(RiattivaProgetto.class);
		
		Progetto progetto = new Progetto();
		progetto.setUid(uidDaRiattivare);
		progetto.setEnte(getEnte());
		
		request.setProgetto(progetto);
		
		return request;
	}
	
	
}
