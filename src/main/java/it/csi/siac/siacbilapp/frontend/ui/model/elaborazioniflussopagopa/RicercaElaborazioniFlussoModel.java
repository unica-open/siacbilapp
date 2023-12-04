/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.elaborazioniflussopagopa;

import java.util.Date;

import it.csi.siac.pagopa.frontend.webservice.msg.RicercaElaborazioni;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;

/**
 * Classe di model per la ricerca del elaborazione flusso. Contiene una mappatura dei campi della ricerca.
 * 
 * @author Vincenzo Gambino
 * @version 1.0.0 08/07/2020
 *
 */
public class RicercaElaborazioniFlussoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5408731638066392689L;
	
	/** Costruttore vuoto di default */
	public RicercaElaborazioniFlussoModel() {
		super();
		setTitolo("Ricerca PagoPA");
	}
	

	private Integer numeroProvvisorio;
	private String flusso;
	private Date dataInizioEmissione;
	private Date dataFineEmissione;
	private Date dataInizioElaborazioneFlusso;
	private Date dataFineElaborazioneFlusso;
	// SIAC-8046 CM 09/03/2021 Inizio
	private String esitoElaborazioneFlusso;
	// SIAC-8046 CM 09/03/2021 Fine
	
	
	
	/* Request */
	/**
	 * @return the numeroProvvisorio
	 */
	public Integer getNumeroProvvisorio() {
		return numeroProvvisorio;
	}





	/**
	 * @return the flusso
	 */
	public String getFlusso() {
		return flusso;
	}





	/**
	 * @return the dataInizioEmissione
	 */
	public Date getDataInizioEmissione() {
		return dataInizioEmissione;
	}





	/**
	 * @return the dataFineEmissione
	 */
	public Date getDataFineEmissione() {
		return dataFineEmissione;
	}





	/**
	 * @return the dataInizioElaborazioneFlusso
	 */
	public Date getDataInizioElaborazioneFlusso() {
		return dataInizioElaborazioneFlusso;
	}





	/**
	 * @return the dataFineElaborazioneFlusso
	 */
	public Date getDataFineElaborazioneFlusso() {
		return dataFineElaborazioneFlusso;
	}





	/**
	 * @param numeroProvvisorio the numeroProvvisorio to set
	 */
	public void setNumeroProvvisorio(Integer numeroProvvisorio) {
		this.numeroProvvisorio = numeroProvvisorio;
	}





	/**
	 * @param flusso the flusso to set
	 */
	public void setFlusso(String flusso) {
		this.flusso = flusso;
	}





	/**
	 * @param dataInizioEmissione the dataInizioEmissione to set
	 */
	public void setDataInizioEmissione(Date dataInizioEmissione) {
		this.dataInizioEmissione = dataInizioEmissione;
	}





	/**
	 * @param dataFineEmissione the dataFineEmissione to set
	 */
	public void setDataFineEmissione(Date dataFineEmissione) {
		this.dataFineEmissione = dataFineEmissione;
	}





	/**
	 * @param dataInizioElaborazioneFlusso the dataInizioElaborazioneFlusso to set
	 */
	public void setDataInizioElaborazioneFlusso(Date dataInizioElaborazioneFlusso) {
		this.dataInizioElaborazioneFlusso = dataInizioElaborazioneFlusso;
	}





	/**
	 * @param dataFineElaborazioneFlusso the dataFineElaborazioneFlusso to set
	 */
	public void setDataFineElaborazioneFlusso(Date dataFineElaborazioneFlusso) {
		this.dataFineElaborazioneFlusso = dataFineElaborazioneFlusso;
	}



	/**
	 * @return the esitoElaborazioneFlusso
	 */
	public String getEsitoElaborazioneFlusso() {
		return esitoElaborazioneFlusso;
	}





	/**
	 * @param esitoElaborazioneFlusso the esitoElaborazioneFlusso to set
	 */
	public void setEsitoElaborazioneFlusso(String esitoElaborazioneFlusso) {
		this.esitoElaborazioneFlusso = esitoElaborazioneFlusso;
	}
	
	
	
	


	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento} a partire dal model.
	 * 
	 * @return la request creata
	 */
	public RicercaElaborazioni creaRequestRicercaElaborazioniFlusso() {
		RicercaElaborazioni request = creaRequest(RicercaElaborazioni.class);
		
		
		//request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}
	
	/* Utilities */
	
	
	
	
	
	
	
}
