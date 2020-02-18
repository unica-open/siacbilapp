/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.causale;

import java.util.Date;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleSpesa;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;

/**
 * Model per la visualizzazione dei risultati di ricerca per la Causale di Spesa.
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 - 06/02/2014
 * 
 */
public class RisultatiRicercaCausaleSpesaModel extends GenericCausaleSpesaModel {

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
	
	private String riepilogoRicerca;
	

	/** Costruttore vuoto di default */
	public RisultatiRicercaCausaleSpesaModel() {
		super();
		setTitolo("Risultati ricerca Causale Spesa");
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
	


	/* Requests */

	/**
	 * @return the riepilogoRicerca
	 */
	public String getRiepilogoRicerca() {
		return riepilogoRicerca;
	}

	/**
	 * @param riepilogoRicerca the riepilogoRicerca to set
	 */
	public void setRiepilogoRicerca(String riepilogoRicerca) {
		this.riepilogoRicerca = riepilogoRicerca;
	}

	/**
	 * Crea una request per il servizio di Annulla CausaleSpesa.
	 * 
	 * @return la request creata
	 */
	public AnnullaCausaleSpesa creaRequestAnnullaCausaleSpesa() {
		AnnullaCausaleSpesa request = new AnnullaCausaleSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		CausaleSpesa causaleDaAnnullare = new CausaleSpesa();
		
		causaleDaAnnullare.setUid(uidDaAnnullare);
		causaleDaAnnullare.setDataScadenza(getCausale().getDataScadenza());
		
		request.setCausaleSpesa(causaleDaAnnullare);
		return request;
	}
	
}
