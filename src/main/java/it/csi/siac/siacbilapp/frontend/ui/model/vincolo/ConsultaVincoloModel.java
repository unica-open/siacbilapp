/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.vincolo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;

/**
 * Classe di model per la consultazione del Vincolo. Contiene una mappatura del form di consultazione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 02/01/2014
 * 
 */
public class ConsultaVincoloModel extends GenericVincoloModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6001840461586092716L;
	
	private String stato;
	
	private Integer uidDaConsultare;
	
	/** Costruttore vuoto di default */
	public ConsultaVincoloModel() {
		super();
		setTitolo("Consulta Vincoli");
	}

	/**
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}

	/**
	 * @return the uidDaConsultare
	 */
	public Integer getUidDaConsultare() {
		return uidDaConsultare;
	}

	/**
	 * @param uidDaConsultare the uidDaConsultare to set
	 */
	public void setUidDaConsultare(Integer uidDaConsultare) {
		this.uidDaConsultare = uidDaConsultare;
	}
	
	/**
	 * @return the genereVincolo
	 */
	public String getGenereVincolo() {
		if(getVincolo() == null || getVincolo().getGenereVincolo() == null) {
			return "";
		}
		return getVincolo().getGenereVincolo().getCodice() + " - " + getVincolo().getGenereVincolo().getDescrizione();
	}

	/* Requests */
	
	/**
	 * Crea una request per il servizio di Ricerca Dettaglio Vincolo.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioVincolo creaRequestRicercaDettaglioVincolo() {
		RicercaDettaglioVincolo request = new RicercaDettaglioVincolo();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setChiaveVincolo(uidDaConsultare);
		request.setBilancio(getBilancio());
		
		return request;
	}
		
	/* Metodi di utilita' */

	@Override
	public void impostaDati(RicercaDettaglioVincoloResponse response, List<Capitolo<?, ?>> listaEntrata, List<Capitolo<?, ?>> listaUscita) {
		super.impostaDati(response, listaEntrata, listaUscita);
		// Imposto il dato rimanente
		stato = StringUtils.capitalize(getVincolo().getStatoOperativo().name().toLowerCase());
	}
	
}
