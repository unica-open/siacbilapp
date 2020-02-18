/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroIva;

/**
 * Classe di model per l'aggiornamento del RegistroIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/05/2014
 *
 */
public class AggiornaRegistroIvaModel extends AggiornaRegistroIvaBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5916854908189509644L;

	/** Costruttore vuoto di default */
	public AggiornaRegistroIvaModel() {
		setTitolo("Aggiorna registro iva");
	}

	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link AggiornaRegistroIva}.
	 * 
	 * @return la request creata
	 */
	public AggiornaRegistroIva creaRequestAggiornaRegistroIva() {
		AggiornaRegistroIva request = new AggiornaRegistroIva();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		// Creo il registro a mano
		request.setRegistroIva(creaRegistroIva());
		
		return request;
	}

}
