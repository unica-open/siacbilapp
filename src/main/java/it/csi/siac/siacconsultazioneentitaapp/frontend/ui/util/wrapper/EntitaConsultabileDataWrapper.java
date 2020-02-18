/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper;

import java.io.Serializable;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter.EntitaConsultabileDataAdapter;

/**
 * Wrapper di {@link EntitaConsultabileDataAdapter} per il frontend.
 * 
 * @author Domenico
 *
 */
public class EntitaConsultabileDataWrapper implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6676208105817395986L;
	
	private String name;
	private boolean summable;
	
	/** Costruttore vuoto di default */
	public EntitaConsultabileDataWrapper() {
		this(null, false);
	}
	/**
	 * Costruttore di wrap
	 * @param name il nome
	 * @param summable se sia sommabile
	 */
	public EntitaConsultabileDataWrapper(String name, boolean summable) {
		this.name = name;
		this.summable = summable;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the summable
	 */
	public boolean isSummable() {
		return this.summable;
	}
	/**
	 * @param summable the summable to set
	 */
	public void setSummable(boolean summable) {
		this.summable = summable;
	}
	
}
