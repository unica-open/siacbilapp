/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioRegistroIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * Classe di model per l'aggiornamento del RegistroIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/05/2014
 *
 */
public class AggiornaRegistroIvaBaseModel extends GenericRegistroIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5916854908189509644L;

	/** Costruttore vuoto di default */
	public AggiornaRegistroIvaBaseModel() {
		setTitolo("Aggiorna registro iva");
	}

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioRegistroIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioRegistroIva creaRequestRicercaDettaglioRegistroIva() {
		RicercaDettaglioRegistroIva request = new RicercaDettaglioRegistroIva();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		// Creo il registro
		request.setRegistroIva(creaRegistroIva(getUidRegistroIva()));
		return request;
	}
	
	@Override
	protected RegistroIva creaRegistroIva() {
		RegistroIva registroIva = super.creaRegistroIva();
		registroIva.setUid(getUidRegistroIva());
		return registroIva;
	}

}
