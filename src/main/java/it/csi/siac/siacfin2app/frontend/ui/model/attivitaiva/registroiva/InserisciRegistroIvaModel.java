/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRegistroIva;

/**
 * Classe di model per l'inserimento del RegistroIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/05/2014
 *
 */
public class InserisciRegistroIvaModel extends GenericRegistroIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7647932828412755934L;

	/** Costruttore vuoto di default */
	public InserisciRegistroIvaModel() {
		setTitolo("Inserisci registro iva");
	}

	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link InserisceRegistroIva}.
	 * 
	 * @return la request creata
	 */
	public InserisceRegistroIva creaRequestInserisceRegistroIva() {
		InserisceRegistroIva request = new InserisceRegistroIva();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		// Creo il gruppo a mano
		request.setRegistroIva(creaRegistroIva());
		
		return request;
	}

}
