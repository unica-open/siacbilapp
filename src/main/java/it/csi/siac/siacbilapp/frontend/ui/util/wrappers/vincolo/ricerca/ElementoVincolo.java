/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.vincolo.ricerca;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.StatoOperativo;

/**
 * Classe di wrap per il Vincolo.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 02/01/2013
 *
 */
public class ElementoVincolo implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7955007686452616649L;
	
	private Integer uid;
	private String codice;
	private String descrizione;
	private String bilancio;
	private String trasferimentiVincolati;
	private Integer numeroCapitoliEntrata;
	private Integer numeroCapitoliUscita;
	private StatoOperativo statoOperativo;
	
	private String azioni;
	
	/** Costruttore vuoto di default */
	public ElementoVincolo() {
		super();
	}

	@Override
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
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
	 * @return the bilancio
	 */
	public String getBilancio() {
		return bilancio;
	}

	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(String bilancio) {
		this.bilancio = bilancio;
	}

	/**
	 * @return the trasferimentiVincolati
	 */
	public String getTrasferimentiVincolati() {
		return trasferimentiVincolati;
	}

	/**
	 * @param trasferimentiVincolati the trasferimentiVincolati to set
	 */
	public void setTrasferimentiVincolati(String trasferimentiVincolati) {
		this.trasferimentiVincolati = trasferimentiVincolati;
	}

	/**
	 * @return the numeroCapitoliEntrata
	 */
	public Integer getNumeroCapitoliEntrata() {
		return numeroCapitoliEntrata;
	}

	/**
	 * @param numeroCapitoliEntrata the numeroCapitoliEntrata to set
	 */
	public void setNumeroCapitoliEntrata(Integer numeroCapitoliEntrata) {
		this.numeroCapitoliEntrata = numeroCapitoliEntrata;
	}

	/**
	 * @return the numeroCapitoliUscita
	 */
	public Integer getNumeroCapitoliUscita() {
		return numeroCapitoliUscita;
	}

	/**
	 * @param numeroCapitoliUscita the numeroCapitoliUscita to set
	 */
	public void setNumeroCapitoliUscita(Integer numeroCapitoliUscita) {
		this.numeroCapitoliUscita = numeroCapitoliUscita;
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	/**
	 * @return the statoOperativo
	 */
	public StatoOperativo getStatoOperativo() {
		return statoOperativo;
	}

	/**
	 * @param statoOperativo the statoOperativo to set
	 */
	public void setStatoOperativo(StatoOperativo statoOperativo) {
		this.statoOperativo = statoOperativo;
	}
	
	/**
	 * Controlla se il vincolo &eacute; in stato valido.
	 * 
	 * @return <code>true</code> se lo stato operativo del vincolo &eacute; pari a VALIDO; <code>false</code> altrimenti
	 */
	public boolean isValido() {
		return StatoOperativo.VALIDO.equals(statoOperativo);
	}
	
}
