/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.gruppoattivita;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

/**
 * Classe di model per l'inserimento del GruppoAttivitaIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/05/2014
 *
 */
public class RicercaGruppoAttivitaIvaModel extends GenericGruppoAttivitaIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7647932828412755934L;
	
	/** Costruttore vuoto di default */
	public RicercaGruppoAttivitaIvaModel() {
		setTitolo("Ricerca gruppo attività iva");
	}

	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaGruppoAttivitaIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaGruppoAttivitaIva creaRequestRicercaSinteticaGruppoAttivitaIva() {
		RicercaSinteticaGruppoAttivitaIva request = new RicercaSinteticaGruppoAttivitaIva();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		// Popolo l'anno
		request.setAnno(getAnnoEsercizioInt());
		// Creo il gruppo a mano
		request.setGruppoAttivitaIva(creaGruppoAttivitaIva());
		
		return request;
	}

	@Override
	protected GruppoAttivitaIva creaGruppoAttivitaIva() {
		GruppoAttivitaIva gai = getGruppoAttivitaIva();
		gai.setTipoAttivita(getTipoAttivita());
		gai.setEnte(getEnte());
		return gai;
	}
	
	
	
}
