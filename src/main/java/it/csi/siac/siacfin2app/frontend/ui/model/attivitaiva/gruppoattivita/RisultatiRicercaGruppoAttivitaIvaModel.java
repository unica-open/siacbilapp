/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.gruppoattivita;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.attivitaiva.ElementoAnnualizzazioneGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAnnualizzataGruppoAttivitaIva;

/**
 * Model per la visualizzazione dei risultati di ricerca per il GruppoAttivitaIva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/05/2014
 * 
 */
public class RisultatiRicercaGruppoAttivitaIvaModel extends GenericGruppoAttivitaIvaModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -359924089804244041L;

	private int savedDisplayStart;
	
	// Per le azioni da delegare all'esterno
	private Integer uidDaAggiornare;
	private Integer uidDaEliminare;
	
	private List<ElementoAnnualizzazioneGruppoAttivitaIva> listaElementoAnnualizzazioneGruppoAttivitaIva = new ArrayList<ElementoAnnualizzazioneGruppoAttivitaIva>();

	/** Costruttore vuoto di default */
	public RisultatiRicercaGruppoAttivitaIvaModel() {
		super();
		setTitolo("Risultati ricerca Gruppo Attività Iva");
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
	public Integer getUidDaAggiornare() {
		return uidDaAggiornare;
	}

	/**
	 * @param uidDaAggiornare the uidDaAggiornare to set
	 */
	public void setUidDaAggiornare(Integer uidDaAggiornare) {
		this.uidDaAggiornare = uidDaAggiornare;
	}

	/**
	 * @return the uidDaEliminare
	 */
	public Integer getUidDaEliminare() {
		return uidDaEliminare;
	}

	/**
	 * @param uidDaEliminare the uidDaEliminare to set
	 */
	public void setUidDaEliminare(Integer uidDaEliminare) {
		this.uidDaEliminare = uidDaEliminare;
	}
	
	/**
	 * @return the listaElementoAnnualizzazioneGruppoAttivitaIva
	 */
	public List<ElementoAnnualizzazioneGruppoAttivitaIva> getListaElementoAnnualizzazioneGruppoAttivitaIva() {
		return listaElementoAnnualizzazioneGruppoAttivitaIva;
	}

	/**
	 * @param listaElementoAnnualizzazioneGruppoAttivitaIva the listaElementoAnnualizzazioneGruppoAttivitaIva to set
	 */
	public void setListaElementoAnnualizzazioneGruppoAttivitaIva(List<ElementoAnnualizzazioneGruppoAttivitaIva> listaElementoAnnualizzazioneGruppoAttivitaIva) {
		this.listaElementoAnnualizzazioneGruppoAttivitaIva = listaElementoAnnualizzazioneGruppoAttivitaIva != null ? listaElementoAnnualizzazioneGruppoAttivitaIva : new ArrayList<ElementoAnnualizzazioneGruppoAttivitaIva>();
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link EliminaGruppoAttivitaIva}.
	 * 
	 * @return la request creata
	 */
	public EliminaGruppoAttivitaIva creaRequestEliminaGruppoAttivitaIva() {
		EliminaGruppoAttivitaIva request = creaRequest(EliminaGruppoAttivitaIva.class);
		
		// Creo il gruppo dato l'uid
		request.setGruppoAttivitaIva(creaGruppoAttivitaIva(getUidDaEliminare()));
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioAnnualizzataGruppoAttivitaIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioAnnualizzataGruppoAttivitaIva creaRequestRicercaDettaglioAnnualizzataGruppoAttivitaIva() {
		RicercaDettaglioAnnualizzataGruppoAttivitaIva request = creaRequest(RicercaDettaglioAnnualizzataGruppoAttivitaIva.class);
		
		request.setGruppoAttivitaIva(getGruppoAttivitaIva());
		
		return request;
	}

}
