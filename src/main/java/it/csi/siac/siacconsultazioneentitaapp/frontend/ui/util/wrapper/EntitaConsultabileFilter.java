/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper;

import java.io.Serializable;

/**
 * Filtro per le entit&agrave; consultabili
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/06/2018
 *
 */
public class EntitaConsultabileFilter implements Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2748207374830249575L;
	
	private String codice;
	private String descrizione;
	private String checked;
	
	
	/** Costruttore vuoto di default */
	public EntitaConsultabileFilter() {
		this(null, null, null);
	}

	/**
	 * Costruttore di wrap per i campi
	 * @param codice il codice
	 * @param descrizione la descrizione
	 * @param checked il checked
	 */
	public EntitaConsultabileFilter(String codice, String descrizione, String checked) {
		this.codice = codice;
		this.descrizione = descrizione;
		this.checked = checked;
	}
	/**
	 * @return the codice
	 */
	public String getCodice() {
		return this.codice;
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
		return this.descrizione;
	}
	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return the checked
	 */
	public String getChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(String checked) {
		this.checked = checked;
	}
	
	
	
}
