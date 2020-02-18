/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.vincolo;

import java.util.Date;

import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.Vincolo;

/**
 * Classe di model per l'inserimento del Vincolo. Contiene una mappatura del form di inserimento.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 30/12/2013
 * 
 */
public class InserisciVincoloModel extends GenericVincoloModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4581590705945522752L;
	
	private Integer idVincolo;
	
	private Boolean bilancioPrevisioneAbilitato = Boolean.TRUE;
	private Boolean bilancioGestioneAbilitato = Boolean.TRUE;
	
	/** Costruttore vuoto di default */
	public InserisciVincoloModel() {
		super();
		setTitolo("Inserimento Vincolo");
	}
		
	/**
	 * @return the idVincolo
	 */
	public Integer getIdVincolo() {
		return idVincolo;
	}

	/**
	 * @param idVincolo the idVincolo to set
	 */
	public void setIdVincolo(Integer idVincolo) {
		this.idVincolo = idVincolo;
	}
	
	/**
	 * @return the bilancioPrevisioneAbilitato
	 */
	public Boolean getBilancioPrevisioneAbilitato() {
		return bilancioPrevisioneAbilitato;
	}

	/**
	 * @param bilancioPrevisioneAbilitato the bilancioPrevisioneAbilitato to set
	 */
	public void setBilancioPrevisioneAbilitato(Boolean bilancioPrevisioneAbilitato) {
		this.bilancioPrevisioneAbilitato = bilancioPrevisioneAbilitato;
	}

	/**
	 * @return the bilancioGestioneAbilitato
	 */
	public Boolean getBilancioGestioneAbilitato() {
		return bilancioGestioneAbilitato;
	}

	/**
	 * @param bilancioGestioneAbilitato the bilancioGestioneAbilitato to set
	 */
	public void setBilancioGestioneAbilitato(Boolean bilancioGestioneAbilitato) {
		this.bilancioGestioneAbilitato = bilancioGestioneAbilitato;
	}
	
	/* Requests */

	/**
	 * Crea una request per il servizio di Ricerca del Vincolo.
	 * 
	 * @return la request creata
	 */
	public RicercaVincolo creaRequestRicercaVincolo() {
		RicercaVincolo request = new RicercaVincolo();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setVincolo(impostaVincolo());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Inserisce Anagrafica Vincolo.
	 * 
	 * @return la request creata
	 */
	public InserisceAnagraficaVincolo creaRequestInserisceAnagraficaVincolo() {
		InserisceAnagraficaVincolo request = new InserisceAnagraficaVincolo();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		getVincolo().setEnte(getEnte());
		getVincolo().setBilancio(getBilancio());
		getVincolo().setStatoOperativo(StatoOperativo.VALIDO);
		
		request.setVincolo(getVincolo());
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Imposta il vincolo per la ricerca.
	 * 
	 * @return il vincolo creato
	 */
	private Vincolo impostaVincolo() {
		Vincolo vincoloDaImpostare = new Vincolo();
		
		vincoloDaImpostare.setTipoVincoloCapitoli(getVincolo().getTipoVincoloCapitoli());
		vincoloDaImpostare.setBilancio(getBilancio());
		vincoloDaImpostare.setCodice(getVincolo().getCodice());
		vincoloDaImpostare.setStatoOperativo(StatoOperativo.VALIDO);
		vincoloDaImpostare.setEnte(getEnte());
		
		return vincoloDaImpostare;
	}
	
}
