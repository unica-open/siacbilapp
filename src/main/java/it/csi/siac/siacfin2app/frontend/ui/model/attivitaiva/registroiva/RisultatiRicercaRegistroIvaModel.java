/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.AllineaProtocolloRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.BloccaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SbloccaRegistroIva;

/**
 * Model per la visualizzazione dei risultati di ricerca per il GruppoAttivitaIva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/05/2014
 * 
 */
public class RisultatiRicercaRegistroIvaModel extends GenericRegistroIvaModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -359924089804244041L;

	private int savedDisplayStart;
	
	// Per le azioni da delegare all'esterno
	private Integer uidDaAggiornare;
	private Integer uidDaEliminare;
	//CR-3791
	private Integer uidRegistroSelezionato;
		
	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistroIvaModel() {
		super();
		setTitolo("Risultati ricerca Registro Iva");
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
	 * @return the uidRegistroSelezionato
	 */
	public Integer getUidRegistroSelezionato() {
		return uidRegistroSelezionato;
	}

	/**
	 * @param uidRegistroSelezionato the uidRegistroSelezionato to set
	 */
	public void setUidRegistroSelezionato(Integer uidRegistroSelezionato) {
		this.uidRegistroSelezionato = uidRegistroSelezionato;
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link EliminaRegistroIva}.
	 * 
	 * @return la request creata
	 */
	public EliminaRegistroIva creaRequestEliminaRegistroIva() {
		EliminaRegistroIva request = new EliminaRegistroIva();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		// Creo il gruppo dato l'uid
		request.setRegistroIva(creaRegistroIva(getUidDaEliminare()));
		
		return request;
	}
	
	//CR-3791
		/**
		 * Crea una request per il servizio di {@link BloccaRegistroIva}.
		 * @return la request creata
		 */
		public BloccaRegistroIva creaRequestBloccaRegistroIva(){
			BloccaRegistroIva request = creaRequest(BloccaRegistroIva.class);
			request.setRegistroIva(creaRegistroIva(getUidRegistroSelezionato()));
			return request;
		}

		/** 
		 * Crea una request per il servizio di {@link SbloccaRegistroIva}.
		 * @return la request creata
		 */
		public SbloccaRegistroIva creaRequestSbloccaRegistroIva() {
			SbloccaRegistroIva request = creaRequest(SbloccaRegistroIva.class);
			request.setRegistroIva(creaRegistroIva(getUidRegistroSelezionato()));
			return request;
		}
		/** 
		 * Crea una request per il servizio di {@link AllineaProtocolloRegistroIva}.
		 * @return la request creata
		 */
		public AllineaProtocolloRegistroIva creaRequestAllineaProtocolloRegistroIva() {
			AllineaProtocolloRegistroIva request = creaRequest(AllineaProtocolloRegistroIva.class);
			request.setRegistroIva(creaRegistroIva(getUidRegistroSelezionato()));
			request.setBilancio(getBilancio());
			return request;
		}
}
