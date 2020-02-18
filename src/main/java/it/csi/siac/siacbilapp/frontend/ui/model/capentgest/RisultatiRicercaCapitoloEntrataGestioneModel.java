/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentgest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * Model per la visualizzazione dei risultati di ricerca per il Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * 
 */
public class RisultatiRicercaCapitoloEntrataGestioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7735713548791714513L;
	
	private CapitoloEntrataGestione capitoloEntrataGestione;

	private List<CapitoloEntrataGestione> listaCapitoloEntrataGestione = new ArrayList<CapitoloEntrataGestione>();
	
	// Property necessarie per pilotare la dataTable con il plugin json
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
	private ImportiCapitoloEG totaleImporti = new ImportiCapitoloEG();
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloEntrataGestioneModel() {
		super();
		setTitolo("Risultati di ricerca Capitolo Entrata Gestione");
	}

	/**
	 * @return the listaCapitoloEntrataGestione
	 */
	public List<CapitoloEntrataGestione> getListaCapitoloEntrataGestione() {
		return listaCapitoloEntrataGestione;
	}

	/**
	 * @param listaCapitoloEntrataGestione the listaCapitoloEntrataGestione to set
	 */
	public void setListaCapitoloEntrataGestione(List<CapitoloEntrataGestione> listaCapitoloEntrataGestione) {
		this.listaCapitoloEntrataGestione = listaCapitoloEntrataGestione;
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
	 * @return savedDisplayStart
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
	 * 
	 * @return capitoloEntrataGestione
	 */
	public CapitoloEntrataGestione getCapitoloEntrataGestione() {
		return capitoloEntrataGestione;
	}

	/**
	 * 
	 * @param capitoloEntrataGestione the capitoloEntrataGestione to set
	 */
	public void setCapitoloEntrataGestione(CapitoloEntrataGestione capitoloEntrataGestione) {
		this.capitoloEntrataGestione = capitoloEntrataGestione;
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
	public ImportiCapitoloEG getTotaleImporti() {
		return totaleImporti;
	}

	/**
	 * @param totaleImporti the totaleImporti to set
	 */
	public void setTotaleImporti(ImportiCapitoloEG totaleImporti) {
		this.totaleImporti = totaleImporti;
	}

	/* Requests */


	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @param uid l'uid del capitolo
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataGestione creaRequestRicercaDettaglioCapitoloEntrataGestione(int uid) {
		RicercaDettaglioCapitoloEntrataGestione request = creaRequest(RicercaDettaglioCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEGest(getRicercaDettaglioCapitoloEGest(uid));
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link EliminaCapitoloEntrataGestione} a partire dai dati.
	 * 
	 * @param bilancioDaEliminare il getBilancio() cui appartiene il capitolo da eliminare
	 * @param capitoloDaEliminare il capitolo da eliminare
	 * 
	 * @return la Rrequest creata
	 */
	public EliminaCapitoloEntrataGestione creaRequestEliminaCapitoloEntrataGestione(Bilancio bilancioDaEliminare, 
			CapitoloEntrataGestione capitoloDaEliminare) {
		EliminaCapitoloEntrataGestione request = new EliminaCapitoloEntrataGestione();
		request.setBilancio(bilancioDaEliminare);
		request.setCapitoloEntrataGest(capitoloDaEliminare);
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		return request;
	}

	/**
	 * Restituisce una Request di tipo {@link VerificaAnnullabilitaCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return 					la Request creata
	 */
	public VerificaAnnullabilitaCapitoloEntrataGestione creaRequestVerificaAnnullabilitaCapitoloEntrataGestione() {
		VerificaAnnullabilitaCapitoloEntrataGestione request = creaRequest(VerificaAnnullabilitaCapitoloEntrataGestione.class);
		request.setBilancio(getBilancio());
		request.setCapitolo(capitoloEntrataGestione);
		request.setEnte(getEnte());
		return request;
	}

	/**
	 * Restituisce una Request di tipo {@link AnnullaCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return 					la Request creata
	 */
	public AnnullaCapitoloEntrataGestione creaRequestAnnullaCapitoloEntrataGestione() {
		AnnullaCapitoloEntrataGestione request = creaRequest(AnnullaCapitoloEntrataGestione.class);
		request.setBilancio(getBilancio());
		request.setCapitoloEntrataGest(capitoloEntrataGestione);
		request.setEnte(getEnte());
		return request;
	}

	/* Metodi di utilita' */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Entrata Gestione.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEGest getRicercaDettaglioCapitoloEGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEGest utility = new RicercaDettaglioCapitoloEGest();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}


}
