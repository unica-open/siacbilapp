/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.vincolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.vincolo.consultazione.ElementoVincoloConsultazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;

/**
 * Classe di model per la ricerca del Vincolo. Contiene una mappatura del form di ricerca.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 31/12/2013
 * 
 */
public class RicercaVincoloModel extends GenericVincoloModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5594106911780880133L;
	
	private String tipoCapitolo;
	
	// Per la ricerca di dettaglio
	private Integer uidVincolo;
	private ElementoVincoloConsultazione elementoVincoloConsultazione;
	
	/** Costruttore vuoto di default */
	public RicercaVincoloModel() {
		super();
		setTitolo("Ricerca Vincoli");
	}

	/**
	 * @return the tipoCapitolo
	 */
	public String getTipoCapitolo() {
		return tipoCapitolo;
	}

	/**
	 * @param tipoCapitolo the tipoCapitolo to set
	 */
	public void setTipoCapitolo(String tipoCapitolo) {
		this.tipoCapitolo = tipoCapitolo;
	}

	/**
	 * @return the uidVincolo
	 */
	public Integer getUidVincolo() {
		return uidVincolo;
	}

	/**
	 * @param uidVincolo the uidVincolo to set
	 */
	public void setUidVincolo(Integer uidVincolo) {
		this.uidVincolo = uidVincolo;
	}
	
	/**
	 * @return the elementoVincoloConsultazione
	 */
	public ElementoVincoloConsultazione getElementoVincoloConsultazione() {
		return elementoVincoloConsultazione;
	}

	/**
	 * @param elementoVincoloConsultazione the elementoVincoloConsultazione to set
	 */
	public void setElementoVincoloConsultazione(
			ElementoVincoloConsultazione elementoVincoloConsultazione) {
		this.elementoVincoloConsultazione = elementoVincoloConsultazione;
	}
	
	/* Requests */
	
	/**
	 * Crea una request per il servizio di Ricerca Vincolo.
	 * 
	 * @return la request creata
	 */
	public RicercaVincolo creaRequestRicercaVincolo() {
		RicercaVincolo request = new RicercaVincolo();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		
		// L'inserimento del capitolo è dovuto alla sola presenza dei numeri di capitolo / articolo / UEB
		// Essendo il numero del capitolo obbligatorio nel caso di presenza di uno qualsiasi dei tre dati, controllo quel valore
		if(getCapitolo().getNumeroCapitolo() != null) {
			request.setCapitolo(getCapitolo());
		}
		request.setTipiCapitolo(impostaTipiCapitolo());
		
		//SIAC-7192
//		request.set
		
		getVincolo().setEnte(getEnte());
		request.setVincolo(getVincolo());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		//task-52
		request.setRicercaCodiceLike(true);
				
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Ricerca Dettaglio Vincolo.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioVincolo creaRequestRicercaDettaglioVincolo() {
		RicercaDettaglioVincolo request = new RicercaDettaglioVincolo();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setChiaveVincolo(uidVincolo);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Imposta la lista dei tipi di atto per la ricerca del vincolo.
	 * 
	 * @return una lista dei tipi di capitolo corrispondenti alla selezione
	 */
	private List<TipoCapitolo> impostaTipiCapitolo() {
		String entrataUscita = StringUtils.trimToNull(tipoCapitolo);
		List<TipoCapitolo> result = new ArrayList<TipoCapitolo>();
		if(entrataUscita != null) {
			result = TipoCapitolo.getTipiCapitolo(entrataUscita, null);
		}
		return result;
	}
	
}
