/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;

/**
 * Classe di Model la ricerca dell'Elenco.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 10/09/2014
 *
 */
public class RicercaElencoDocumentiAllegatoAjaxModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1153777257681224473L;
	
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	
	/** Costruttore vuoto di default */
	public RicercaElencoDocumentiAllegatoAjaxModel() {
		super();
		setTitolo("Ricerca Elemento Documento Allegato Iva - AJAX");
	}

	/**
	 * @return the elencoDocumentiAllegato
	 */
	public ElencoDocumentiAllegato getElencoDocumentiAllegato() {
		return elencoDocumentiAllegato;
	}

	/**
	 * @param elencoDocumentiAllegato the elencoDocumentiAllegato to set
	 */
	public void setElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		this.elencoDocumentiAllegato = elencoDocumentiAllegato;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaElenco}.
	 * 
	 * @return la request creata
	 */
	public RicercaElenco creaRequestRicercaElenco() {
		RicercaElenco request = creaRequest(RicercaElenco.class);
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		// Popolo l'elenco
		getElencoDocumentiAllegato().setEnte(getEnte());
		request.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		
		List<StatoOperativoElencoDocumenti> list = new ArrayList<StatoOperativoElencoDocumenti>();
		if(getElencoDocumentiAllegato().getStatoOperativoElencoDocumenti() != null) {
			list.add(getElencoDocumentiAllegato().getStatoOperativoElencoDocumenti());
		}
		request.setStatiOperativiElencoDocumenti(list);
		
		return request;
	}
	
}
