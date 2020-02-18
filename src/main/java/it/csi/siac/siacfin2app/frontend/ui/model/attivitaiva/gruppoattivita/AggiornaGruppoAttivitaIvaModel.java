/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.gruppoattivita;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaEProrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

/**
 * Classe di model per l'aggiornamento del GruppoAttivitaIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/05/2014
 *
 */
public class AggiornaGruppoAttivitaIvaModel extends GenericGruppoAttivitaIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7647932828412755934L;

	/** Costruttore vuoto di default */
	public AggiornaGruppoAttivitaIvaModel() {
		setTitolo("Aggiorna gruppo attività iva");
	}

	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link AggiornaGruppoAttivitaIvaEProrata}.
	 * 
	 * @return la request creata
	 */
	public AggiornaGruppoAttivitaIvaEProrata creaRequestAggiornaGruppoAttivitaIvaEProrata() {
		AggiornaGruppoAttivitaIvaEProrata request = new AggiornaGruppoAttivitaIvaEProrata();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		// Creo il gruppo a mano
		request.setGruppoAttivitaIva(creaGruppoAttivitaIva());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaGruppoAttivitaIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioGruppoAttivitaIva creaRequestRicercaDettaglioGruppoAttivitaIva() {
		RicercaDettaglioGruppoAttivitaIva request = new RicercaDettaglioGruppoAttivitaIva();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		
		request.setGruppoAttivitaIva(creaGruppoAttivitaIva(getUidGruppoAttivitaIva(), getAnnoEsercizioInt()));
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaGruppoAttivitaIva} per l'anno precedente all'attuale
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioGruppoAttivitaIva creaRequestRicercaDettaglioGruppoAttivitaIvaPrecedente() {
		RicercaDettaglioGruppoAttivitaIva request = new RicercaDettaglioGruppoAttivitaIva();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		
		request.setGruppoAttivitaIva(creaGruppoAttivitaIva(getUidGruppoAttivitaIva(), getAnnoEsercizioInt() - 1));
		
		return request;
	}
	
	@Override
	protected GruppoAttivitaIva creaGruppoAttivitaIva() {
		GruppoAttivitaIva gruppoAttivitaIva = super.creaGruppoAttivitaIva();
		gruppoAttivitaIva.setUid(getUidGruppoAttivitaIva());
		return gruppoAttivitaIva;
	}
	
	/**
	 * Crea un GruppoAttivitaIva a partire da un dato uid.
	 * 
	 * @param uid l'uid a partire da cui creare il gruppo
	 * @param annualizzazione l'annualizzazione
	 * 
	 * @return il Gruppo creato
	 */
	protected GruppoAttivitaIva creaGruppoAttivitaIva(Integer uid, Integer annualizzazione) {
		GruppoAttivitaIva gai = new GruppoAttivitaIva();
		gai.setUid(uid);
		// Generalmente e' ridondante. Ma non fa particolarmente male
		gai.setEnte(getEnte());
		gai.setAnnualizzazione(annualizzazione);
		
		return gai;
	}

}
