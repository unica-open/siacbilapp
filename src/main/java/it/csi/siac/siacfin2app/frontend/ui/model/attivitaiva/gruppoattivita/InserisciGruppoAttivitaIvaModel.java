/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.gruppoattivita;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaEProrata;

/**
 * Classe di model per l'inserimento del GruppoAttivitaIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/05/2014
 *
 */
public class InserisciGruppoAttivitaIvaModel extends GenericGruppoAttivitaIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7647932828412755934L;

	/** Costruttore vuoto di default */
	public InserisciGruppoAttivitaIvaModel() {
		setTitolo("Inserisci gruppo attività iva");
	}

	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link InserisceGruppoAttivitaIvaEProrata}.
	 * 
	 * @return la request creata
	 */
	public InserisceGruppoAttivitaIvaEProrata creaRequestInserisceGruppoAttivitaIvaEProrata() {
		InserisceGruppoAttivitaIvaEProrata request = new InserisceGruppoAttivitaIvaEProrata();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		// Creo il gruppo a mano
		request.setGruppoAttivitaIva(creaGruppoAttivitaIva());
		
		return request;
	}

}
