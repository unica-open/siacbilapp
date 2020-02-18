/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper.EntitaConsultabileDataWrapper;
import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;

/**
 * Classe di model per la ricerca di intestazioni della tabella dell'entita consultabile
 * @author Elisa Chiari
 * @version 1.0.0 - 24/03/2016
 *
 */
public class RicercaIntestazioniCampiTabellaEntitaConsultabileAjaxModel extends GenericBilancioModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -729279515962848300L;
	
	private TipoEntitaConsultabile tipoEntitaConsultabile;
	private List<EntitaConsultabileDataWrapper> listaIntestazioniCampiTabella = new ArrayList<EntitaConsultabileDataWrapper>();
	
	/** Costruttore */
	public RicercaIntestazioniCampiTabellaEntitaConsultabileAjaxModel() {
		super();
		setTitolo("Ricerca intestazioni tabella entita consultabile - AJAX");
	}

	/**
	 * @return the tipoEntuitaconsultabile
	 */
	public TipoEntitaConsultabile getTipoEntitaConsultabile() {
		return tipoEntitaConsultabile;
	}

	/**
	 * @param tipoEntitaConsultabile the tipoEntitaConsultabile to set
	 */
	public void setTipoEntitaConsultabile(TipoEntitaConsultabile tipoEntitaConsultabile) {
		this.tipoEntitaConsultabile = tipoEntitaConsultabile;
	}
	
	/**
	 * Crea una request per la ricerca delle intestazioni dei campi della tabella
	 */
	public void creaRequestRicercaIntestazioniCampiTabella(){
		//TODO cruscottino
		
	}

	/**
	 * @return the listaIntestazioniCampiTabella
	 */
	public List<EntitaConsultabileDataWrapper> getListaIntestazioniCampiTabella() {
		return listaIntestazioniCampiTabella;
	}

	/**
	 * @param listaIntestazioniCampiTabella the listaIntestazioniCampiTabella to set
	 */
	public void setListaIntestazioniCampiTabella(List<EntitaConsultabileDataWrapper> listaIntestazioniCampiTabella) {
		this.listaIntestazioniCampiTabella = listaIntestazioniCampiTabella;
	}
	
}
