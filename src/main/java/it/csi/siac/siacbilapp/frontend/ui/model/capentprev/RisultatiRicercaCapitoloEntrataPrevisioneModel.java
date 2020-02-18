/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentprev;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * Model per la visualizzazione dei risultati di ricerca per il Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * 
 */
public class RisultatiRicercaCapitoloEntrataPrevisioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8675359642254632588L;
	
	private CapitoloEntrataPrevisione capitoloEntrataPrevisione;

	private List<CapitoloEntrataPrevisione> listaCapitoloEntrataPrevisione = new ArrayList<CapitoloEntrataPrevisione>();
	
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

	private Integer paginaInizialeConsultazione;
	
	
	/*Totale Importi */
	private ImportiCapitoloEP totaleImporti = new ImportiCapitoloEP();
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloEntrataPrevisioneModel() {
		super();
		setTitolo("Risultati di ricerca Capitolo Entrata Previsione");
	}

	/**
	 * @return the listaCapitoloUscitaPrevisione
	 */
	public List<CapitoloEntrataPrevisione> getListaCapitoloEntrataPrevisione() {
		return listaCapitoloEntrataPrevisione;
	}

	/**
	 * @param listaCapitoloEntrataPrevisione the listaCapitoloUscitaPrevisione to set
	 */
	public void setListaCapitoloEntrataPrevisione(List<CapitoloEntrataPrevisione> listaCapitoloEntrataPrevisione) {
		this.listaCapitoloEntrataPrevisione = listaCapitoloEntrataPrevisione;
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
	public ImportiCapitoloEP getTotaleImporti() {
		return totaleImporti;
	}

	/**
	 * @param totaleImporti the totaleImporti to set
	 */
	public void setTotaleImporti(ImportiCapitoloEP totaleImporti) {
		this.totaleImporti = totaleImporti;
	}
	/* Requests */



	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @param uid l'uid del capitolo
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataPrevisione creaRequestRicercaDettaglioCapitoloEntrataPrevisione(int uid) {
		RicercaDettaglioCapitoloEntrataPrevisione request = creaRequest(RicercaDettaglioCapitoloEntrataPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEPrev(getRicercaDettaglioCapitoloEPrev(uid));
		return request;
	}

	/**
	 * 
	 * @return savedDisplayStart
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
	 * @return capitoloEntrataPrevisione
	 */ 
	public CapitoloEntrataPrevisione getCapitoloEntrataPrevisione() {
		return capitoloEntrataPrevisione;
	}

	/**
	 * 
	 * @param capitoloEntrataPrevisione the capitoloEntrataPrevisione to set
	 */
	public void setCapitoloEntrataPrevisione(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		this.capitoloEntrataPrevisione = capitoloEntrataPrevisione;
	}
	/**
	 * Restituisce una Request di tipo {@link EliminaCapitoloEntrataPrevisione} a partire dai dati.
	 * 
	 * @param bilancioDaEliminare il getBilancio() cui appartiene il capitolo da eliminare
	 * @param capitoloDaEliminare il capitolo da eliminare
	 * 
	 * @return la Rrequest creata
	 */
	public EliminaCapitoloEntrataPrevisione creaRequestEliminaCapitoloEntrataPrevisione(Bilancio bilancioDaEliminare, CapitoloEntrataPrevisione capitoloDaEliminare) {
		EliminaCapitoloEntrataPrevisione request = creaRequest(EliminaCapitoloEntrataPrevisione.class);
		request.setBilancio(bilancioDaEliminare);
		request.setCapitoloEntrataPrev(capitoloDaEliminare);
		request.setEnte(getEnte());
		return request;
	}

	/**
	 * Restituisce una Request di tipo {@link VerificaAnnullabilitaCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return 					la Request creata
	 */
	public VerificaAnnullabilitaCapitoloEntrataPrevisione creaRequestVerificaAnnullabilitaCapitoloEntrataPrevisione() {
		VerificaAnnullabilitaCapitoloEntrataPrevisione request = creaRequest(VerificaAnnullabilitaCapitoloEntrataPrevisione.class);
		request.setBilancio(getBilancio());
		request.setCapitolo(capitoloEntrataPrevisione);
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		return request;
	}

	/**
	 * Restituisce una Request di tipo {@link AnnullaCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return 					la Request creata
	 */
	public AnnullaCapitoloEntrataPrevisione creaRequestAnnullaCapitoloEntrataPrevisione() {
		AnnullaCapitoloEntrataPrevisione request = creaRequest(AnnullaCapitoloEntrataPrevisione.class);
		request.setBilancio(getBilancio());
		request.setCapitoloEntrataPrev(capitoloEntrataPrevisione);
		request.setEnte(getEnte());
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Entrata Previsione.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEPrev getRicercaDettaglioCapitoloEPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEPrev utility = new RicercaDettaglioCapitoloEPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}

}
