/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscgest;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * Model per la visualizzazione dei risultati di ricerca per il Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * 
 */
public class RisultatiRicercaCapitoloUscitaGestioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6263077854467943899L;
	
	private CapitoloUscitaGestione capitoloUscitaGestione;

	private List<CapitoloUscitaGestione> listaCapitoloUscitaGestione = new ArrayList<CapitoloUscitaGestione>();
	
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
	private ImportiCapitoloUG totaleImporti = new ImportiCapitoloUG();
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloUscitaGestioneModel() {
		super();
		setTitolo("Risultati di ricerca Capitolo Spesa Gestione");
	}

	/**
	 * @return the listaCapitoloUscitaGestione
	 */
	public List<CapitoloUscitaGestione> getListaCapitoloUscitaGestione() {
		return listaCapitoloUscitaGestione;
	}

	/**
	 * @param listaCapitoloUscitaGestione the listaCapitoloUscitaGestione to set
	 */
	public void setListaCapitoloUscitaGestione(List<CapitoloUscitaGestione> listaCapitoloUscitaGestione) {
		this.listaCapitoloUscitaGestione = listaCapitoloUscitaGestione;
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
	 * 
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(int savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}

	/**
	 * 
	 * @return capitoloUscitaGestione
	 */
	public CapitoloUscitaGestione getCapitoloUscitaGestione() {
		return capitoloUscitaGestione;
	}

	/**
	 * 
	 * @param capitoloUscitaGestione the capitoloUscitaGestione to set
	 */
	public void setCapitoloUscitaGestione(CapitoloUscitaGestione capitoloUscitaGestione) {
		this.capitoloUscitaGestione = capitoloUscitaGestione;
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
	public ImportiCapitoloUG getTotaleImporti() {
		return totaleImporti;
	}

	/**
	 * @param totaleImporti the totaleImporti to set
	 */
	public void setTotaleImporti(ImportiCapitoloUG totaleImporti) {
		this.totaleImporti = totaleImporti;
	}
	/* Requests */



	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @param uid l'uid del capitolo
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaGestione creaRequestRicercaDettaglioCapitoloUscitaGestione(int uid) {
		RicercaDettaglioCapitoloUscitaGestione request = creaRequest(RicercaDettaglioCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUGest(getRicercaDettaglioCapitoloUGest(uid));
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link EliminaCapitoloUscitaGestione} a partire dai dati.
	 * 
	 * @param bilancioDaEliminare il bilancio cui appartiene il capitolo da eliminare
	 * @param capitoloDaEliminare il capitolo da eliminare
	 * 
	 * @return la Rrequest creata
	 */
	public EliminaCapitoloUscitaGestione creaRequestEliminaCapitoloUscitaGestione(Bilancio bilancioDaEliminare, CapitoloUscitaGestione capitoloDaEliminare) {
		EliminaCapitoloUscitaGestione request = creaRequest(EliminaCapitoloUscitaGestione.class);
		request.setBilancio(bilancioDaEliminare);
		request.setCapitoloUscitaGest(capitoloDaEliminare);
		request.setEnte(getEnte());
		return request;
	}

	/**
	 * Restituisce una Request di tipo {@link VerificaAnnullabilitaCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return 					la Request creata
	 */
	public VerificaAnnullabilitaCapitoloUscitaGestione creaRequestVerificaAnnullabilitaCapitoloUscitaGestione() {
		VerificaAnnullabilitaCapitoloUscitaGestione request = creaRequest(VerificaAnnullabilitaCapitoloUscitaGestione.class);
		request.setBilancio(getBilancio());
		request.setCapitolo(capitoloUscitaGestione);
		request.setEnte(getEnte());
		return request;
	}

	/**
	 * Restituisce una Request di tipo {@link AnnullaCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return 					la Request creata
	 */
	public AnnullaCapitoloUscitaGestione creaRequestAnnullaCapitoloUscitaGestione() {
		AnnullaCapitoloUscitaGestione request = creaRequest(AnnullaCapitoloUscitaGestione.class);
		request.setBilancio(getBilancio());
		request.setCapitoloUscitaGest(capitoloUscitaGestione);
		request.setEnte(getEnte());
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita Gestione.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUGest getRicercaDettaglioCapitoloUGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUGest utility = new RicercaDettaglioCapitoloUGest();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}

	
}
