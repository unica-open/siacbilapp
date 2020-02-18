/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.provvisoriocassa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;

/**
 * Model per la visualizzazione dei risultati di ricerca per il Provvisorio di cassa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 11/03/2016
 * 
 */
public class RisultatiRicercaSinteticaProvvisorioDiCassaModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -9142120563868951189L;
	
	private Integer savedDisplayStart;
	private Integer uidDaAssociare;
	private List<Integer> listaUidProvvisorio = new ArrayList<Integer>();
	private BigDecimal totaleProvvisoriSelezionati;
	private String nomeAzioneSuccessiva;

	/** Costruttore vuoto di default */
	public RisultatiRicercaSinteticaProvvisorioDiCassaModel() {
		super();
		setTitolo("Risultati ricerca Provvisorio di Cassa");
	}

	/**
	 * @return the uidDaAssociare
	 */
	public Integer getUidDaAssociare() {
		return uidDaAssociare;
	}

	/**
	 * @param uidDaAssociare the uidDaAssociare to set
	 */
	public void setUidDaAssociare(Integer uidDaAssociare) {
		this.uidDaAssociare = uidDaAssociare;
	}

	/**
	 * @return the savedDisplayStart
	 */
	public Integer getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(Integer savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}

	/**
	 * @return the listauidProvvisorio
	 */
	public List<Integer> getListaUidProvvisorio() {
		return listaUidProvvisorio;
	}

	/**
	 * @param listaUidProvvisorio the listauidProvvisorio to set
	 */
	public void setListaUidProvvisorio(List<Integer> listaUidProvvisorio) {
		this.listaUidProvvisorio = listaUidProvvisorio;
	}

	/**
	 * @return the totaleProvvisoriSelezionati
	 */
	public BigDecimal getTotaleProvvisoriSelezionati() {
		return totaleProvvisoriSelezionati;
	}

	/**
	 * @param totaleProvvisoriSelezionati the totaleProvvisoriSelezionati to set
	 */
	public void setTotaleProvvisoriSelezionati(
			BigDecimal totaleProvvisoriSelezionati) {
		this.totaleProvvisoriSelezionati = totaleProvvisoriSelezionati;
	}

	/**
	 * @return the nomeAzioneSuccessiva
	 */
	public String getNomeAzioneSuccessiva() {
		return nomeAzioneSuccessiva;
	}

	/**
	 * @param nomeAzioneSuccessiva the nomeAzioneSuccessiva to set
	 */
	public void setNomeAzioneSuccessiva(String nomeAzioneSuccessiva) {
		this.nomeAzioneSuccessiva = nomeAzioneSuccessiva;
	}
	
	
	
}
