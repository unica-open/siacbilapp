/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscprev;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * Model per la visualizzazione dei risultati di ricerca per il Capitolo di Uscita Previsione.
 * 
 * @author AR
 * @author LG
 * @author Alessandro Marchino
 * 
 */
public class RisultatiRicercaCapitoloUscitaPrevisioneModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2415921098592102795L;
	
	private CapitoloUscitaPrevisione capitoloUscitaPrevisione;
	
	private List<CapitoloUscitaPrevisione> listaCapitoloUscitaPrevisione = new ArrayList<CapitoloUscitaPrevisione>();
	
	// Property necessarie per pilotare la dataTable con  il plugin json
	private int sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private String iDisplayStart;
	private String iDisplayLength;
	
	private int savedDisplayStart;
	
	private List<ElementoCapitolo> aaData = new ArrayList<ElementoCapitolo>();
	
	// Per le azioni da delegare all'esterno
	private int uidDaAggiornare;
	private int uidDaAnnullare;
	private int uidDaConsultare;
	private int uidDaEliminare;
	
	// Per la consultazione massiva
	private Integer paginaInizialeConsultazione;
	
	/*Totale Importi */
	private ImportiCapitoloUP totaleImporti = new ImportiCapitoloUP();
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloUscitaPrevisioneModel() {
		super();
		setTitolo("Risultati di ricerca Capitolo Spesa Previsione");
	}

	/**
	 * @return the listaCapitoloUscitaPrevisione
	 */
	public List<CapitoloUscitaPrevisione> getListaCapitoloUscitaPrevisione() {
		return listaCapitoloUscitaPrevisione;
	}

	/**
	 * @param listaCapitoloUscitaPrevisione the listaCapitoloUscitaPrevisione to set
	 */
	public void setListaCapitoloUscitaPrevisione(List<CapitoloUscitaPrevisione> listaCapitoloUscitaPrevisione) {
		this.listaCapitoloUscitaPrevisione = listaCapitoloUscitaPrevisione;
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
	 * @return the uidDaEliminare
	 */
	public int getUidDaEliminare() {
		return uidDaEliminare;
	}

	/**
	 * @param uidDaEliminare the uidDaEliminare to set
	 */
	public void setUidDaEliminare(int uidDaEliminare) {
		this.uidDaEliminare = uidDaEliminare;
	}
	
	/**
	 * 
	 * @return savedDisplayLength
	 */
	public int getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * 
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(int savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}

	/**
	 * 
	 * @return capitoloUscitaPrevisione
	 */
	public CapitoloUscitaPrevisione getCapitoloUscitaPrevisione() {
		return capitoloUscitaPrevisione;
	}

	/**
	 * 
	 * @param capitoloUscitaPrevisione the capitoloUscitaPrevisione to set
	 */
	public void setCapitoloUscitaPrevisione(
			CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		this.capitoloUscitaPrevisione = capitoloUscitaPrevisione;
	}
	
	/**
	 * @return the paginaInizialeConsultazione
	 */
	public Integer getPaginaInizialeConsultazione() {
		return paginaInizialeConsultazione;
	}

	/**
	 * @param paginaInizialeConsultazione the paginaInizialeConsultazione to set
	 */
	public void setPaginaInizialeConsultazione(Integer paginaInizialeConsultazione) {
		this.paginaInizialeConsultazione = paginaInizialeConsultazione;
	}

	/**
	 * @return the totaleImporti
	 */
	public ImportiCapitoloUP getTotaleImporti() {
		return totaleImporti;
	}

	/**
	 * @param totaleImporti the totaleImporti to set
	 */
	public void setTotaleImporti(ImportiCapitoloUP totaleImporti) {
		this.totaleImporti = totaleImporti;
	}
	
	/* Requests */


	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @param uid l'uid del capitolo
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaPrevisione creaRequestRicercaDettaglioCapitoloUscitaPrevisione(int uid) {
		RicercaDettaglioCapitoloUscitaPrevisione request = creaRequest(RicercaDettaglioCapitoloUscitaPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUPrev(getRicercaDettaglioCapitoloUPrev(uid));
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link EliminaCapitoloUscitaPrevisione} a partire dai dati.
	 * 
	 * @param bilancioDaEliminare il getBilancio() cui appartiene il capitolo da eliminare
	 * @param capitoloDaEliminare il capitolo da eliminare
	 * 
	 * @return la Rrequest creata
	 */
	public EliminaCapitoloUscitaPrevisione creaRequestEliminaCapitoloUscitaPrevisione(Bilancio bilancioDaEliminare, 
			CapitoloUscitaPrevisione capitoloDaEliminare) {
		EliminaCapitoloUscitaPrevisione request = new EliminaCapitoloUscitaPrevisione();
		request.setBilancio(bilancioDaEliminare);
		request.setCapitoloUscitaPrev(capitoloDaEliminare);
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		return request;
	}

	/**
	 * Restituisce una Request di tipo {@link VerificaAnnullabilitaCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return 					la Request creata
	 */
	public VerificaAnnullabilitaCapitoloUscitaPrevisione creaRequestVerificaAnnullabilitaCapitoloUscitaPrevisione() {
		VerificaAnnullabilitaCapitoloUscitaPrevisione request = creaRequest(VerificaAnnullabilitaCapitoloUscitaPrevisione.class);
		request.setBilancio(getBilancio());
		request.setCapitolo(capitoloUscitaPrevisione);
		request.setEnte(getEnte());
		return request;
	}

	/**
	 * Restituisce una Request di tipo {@link AnnullaCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return 					la Request creata
	 */
	public AnnullaCapitoloUscitaPrevisione creaRequestAnnullaCapitoloUscitaPrevisione() {
		AnnullaCapitoloUscitaPrevisione request = creaRequest(AnnullaCapitoloUscitaPrevisione.class);
		request.setBilancio(getBilancio());
		request.setCapitoloUscitaPrev(capitoloUscitaPrevisione);
		request.setEnte(getEnte());
		return request;
	}
	

	
	/* Metodi di utilita' */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita Previsione.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUPrev getRicercaDettaglioCapitoloUPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUPrev utility = new RicercaDettaglioCapitoloUPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
}
