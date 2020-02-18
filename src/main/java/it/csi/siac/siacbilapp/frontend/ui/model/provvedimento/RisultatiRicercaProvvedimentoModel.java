/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.provvedimento;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccommonapp.model.GenericModel;

/**
 * Classe di model per i risultati di ricerca per il provvedimento. Contiene una mappatura dei campi del form dei risultati.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/09/2013
 *
 */
public class RisultatiRicercaProvvedimentoModel extends GenericModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5574846708985685450L;
	
	private Integer uidDaAggiornare;
	private Integer uidDaConsultare;
	
	private List<AttoAmministrativo> listaProvvedimento = new ArrayList<AttoAmministrativo>();
	
	// Per il dataTable
	private Integer iDisplayStart;
	private Integer posizioneIniziale;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaProvvedimentoModel() {
		super();
		setTitolo("Risultati di ricerca provvedimento");
	}
	
	/**
	 * @return the uidDaAggiornare
	 */
	public Integer getUidDaAggiornare() {
		return uidDaAggiornare;
	}

	/**
	 * @param uidDaAggiornare the uidDaAggiornare to set
	 */
	public void setUidDaAggiornare(Integer uidDaAggiornare) {
		this.uidDaAggiornare = uidDaAggiornare;
	}

	/**
	 * @return the uidDaConsultare
	 */
	public Integer getUidDaConsultare() {
		return uidDaConsultare;
	}

	/**
	 * @param uidDaConsultare the uidDaConsultare to set
	 */
	public void setUidDaConsultare(Integer uidDaConsultare) {
		this.uidDaConsultare = uidDaConsultare;
	}

	/**
	 * @return the listaProvvedimento
	 */
	public List<AttoAmministrativo> getListaProvvedimento() {
		return listaProvvedimento;
	}

	/**
	 * @param listaProvvedimento the listaProvvedimento to set
	 */
	public void setListaProvvedimento(List<AttoAmministrativo> listaProvvedimento) {
		this.listaProvvedimento = listaProvvedimento;
	}

	/**
	 * @return the iDisplayStart
	 */
	public Integer getiDisplayStart() {
		return iDisplayStart;
	}

	/**
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(Integer iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	/**
	 * @return the posizioneIniziale
	 */
	public Integer getPosizioneIniziale() {
		return posizioneIniziale;
	}

	/**
	 * @param posizioneIniziale the posizioneIniziale to set
	 */
	public void setPosizioneIniziale(Integer posizioneIniziale) {
		this.posizioneIniziale = posizioneIniziale;
	}
	
}
