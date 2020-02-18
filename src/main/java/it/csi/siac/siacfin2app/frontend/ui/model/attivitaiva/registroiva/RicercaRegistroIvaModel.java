/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaRegistroIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * Classe di model per la ricerca del RegistroIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/06/2014
 *
 */
public class RicercaRegistroIvaModel extends GenericRegistroIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3418279328646538433L;

	/** Costruttore vuoto di default */
	public RicercaRegistroIvaModel() {
		setTitolo("Ricerca registro iva");
	}

	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaRegistroIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaRegistroIva creaRequestRicercaSinteticaRegistroIva() {
		RicercaSinteticaRegistroIva request = new RicercaSinteticaRegistroIva();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		// Creo il gruppo a mano
		request.setRegistroIva(creaRegistroIva());
		// Creo i parametri di paginazione
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	@Override
	protected RegistroIva creaRegistroIva() {
		RegistroIva registroIva = getRegistroIva();
		
		registroIva.setGruppoAttivitaIva(impostaEntitaFacoltativa(getGruppoAttivitaIva()));
		registroIva.setTipoRegistroIva(getTipoRegistroIva());
		registroIva.setEnte(getEnte());
		
		return registroIva;
	}

}
