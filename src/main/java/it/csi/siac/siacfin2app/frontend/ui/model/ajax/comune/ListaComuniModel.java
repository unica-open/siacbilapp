/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.comune;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;

/**
 * Classe di model per la lista dei comuni.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/12/2014
 *
 */
public class ListaComuniModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2650652984086167015L;

	private String descrizione;
	private List<ComuneNascita> listaComuni = new ArrayList<ComuneNascita>();
	
	/** Costruttore vuoto di default */
	public ListaComuniModel() {
		super();
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return the listaComuni
	 */
	public List<ComuneNascita> getListaComuni() {
		return listaComuni;
	}

	/**
	 * @param listaComuni the listaComuni to set
	 */
	public void setListaComuni(List<ComuneNascita> listaComuni) {
		this.listaComuni = listaComuni != null ? listaComuni : new ArrayList<ComuneNascita>();
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link ListaComuni}.
	 * 
	 * @return la request creata
	 */
	public ListaComuni creaRequestListaComuni() {
		ListaComuni request = creaRequest(ListaComuni.class);
		
		request.setIdStato(BilConstants.CODICE_ITALIA.getConstant());
		request.setDescrizioneComune(descrizione);
		
		return request;
	}

}
