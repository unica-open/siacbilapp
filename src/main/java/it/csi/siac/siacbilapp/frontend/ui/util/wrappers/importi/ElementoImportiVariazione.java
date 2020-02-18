/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.importi;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Classe di wrap per la gestione degli importi in una variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 04/11/2013
 *
 */
public class ElementoImportiVariazione implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7520071044888432625L;
	
	private BigDecimal totaleEntrataCompetenza;
	private BigDecimal totaleEntrataResiduo;
	private BigDecimal totaleEntrataCassa;
	private BigDecimal totaleEntrataFondoPluriennaleVincolato;
	
	private BigDecimal totaleSpesaCompetenza;
	private BigDecimal totaleSpesaResiduo;
	private BigDecimal totaleSpesaCassa;
	private BigDecimal totaleSpesaFondoPluriennaleVincolato;
	
	private BigDecimal differenzaCompetenza;
	private BigDecimal differenzaResiduo;
	private BigDecimal differenzaCassa;
	private BigDecimal differenzaFondoPluriennaleVincolato;
	
	/** Costruttore vuoto di default */
	public ElementoImportiVariazione() {
		super();
	}
	
	/**
	 * @return the totaleEntrataCompetenza
	 */
	public BigDecimal getTotaleEntrataCompetenza() {
		return totaleEntrataCompetenza;
	}

	/**
	 * @param totaleEntrataCompetenza the totaleEntrataCompetenza to set
	 */
	public void setTotaleEntrataCompetenza(BigDecimal totaleEntrataCompetenza) {
		this.totaleEntrataCompetenza = totaleEntrataCompetenza;
	}

	/**
	 * @return the totaleEntrataResiduo
	 */
	public BigDecimal getTotaleEntrataResiduo() {
		return totaleEntrataResiduo;
	}

	/**
	 * @param totaleEntrataResiduo the totaleEntrataResiduo to set
	 */
	public void setTotaleEntrataResiduo(BigDecimal totaleEntrataResiduo) {
		this.totaleEntrataResiduo = totaleEntrataResiduo;
	}

	/**
	 * @return the totaleEntrataCassa
	 */
	public BigDecimal getTotaleEntrataCassa() {
		return totaleEntrataCassa;
	}

	/**
	 * @param totaleEntrataCassa the totaleEntrataCassa to set
	 */
	public void setTotaleEntrataCassa(BigDecimal totaleEntrataCassa) {
		this.totaleEntrataCassa = totaleEntrataCassa;
	}

	/**
	 * @return the totaleEntrataFondoPluriennaleVincolato
	 */
	public BigDecimal getTotaleEntrataFondoPluriennaleVincolato() {
		return totaleEntrataFondoPluriennaleVincolato;
	}

	/**
	 * @param totaleEntrataFondoPluriennaleVincolato the totaleEntrataFondoPluriennaleVincolato to set
	 */
	public void setTotaleEntrataFondoPluriennaleVincolato(
			BigDecimal totaleEntrataFondoPluriennaleVincolato) {
		this.totaleEntrataFondoPluriennaleVincolato = totaleEntrataFondoPluriennaleVincolato;
	}

	/**
	 * @return the totaleSpesaCompetenza
	 */
	public BigDecimal getTotaleSpesaCompetenza() {
		return totaleSpesaCompetenza;
	}

	/**
	 * @param totaleSpesaCompetenza the totaleSpesaCompetenza to set
	 */
	public void setTotaleSpesaCompetenza(BigDecimal totaleSpesaCompetenza) {
		this.totaleSpesaCompetenza = totaleSpesaCompetenza;
	}

	/**
	 * @return the totaleSpesaResiduo
	 */
	public BigDecimal getTotaleSpesaResiduo() {
		return totaleSpesaResiduo;
	}

	/**
	 * @param totaleSpesaResiduo the totaleSpesaResiduo to set
	 */
	public void setTotaleSpesaResiduo(BigDecimal totaleSpesaResiduo) {
		this.totaleSpesaResiduo = totaleSpesaResiduo;
	}

	/**
	 * @return the totaleSpesaCassa
	 */
	public BigDecimal getTotaleSpesaCassa() {
		return totaleSpesaCassa;
	}

	/**
	 * @param totaleSpesaCassa the totaleSpesaCassa to set
	 */
	public void setTotaleSpesaCassa(BigDecimal totaleSpesaCassa) {
		this.totaleSpesaCassa = totaleSpesaCassa;
	}

	/**
	 * @return the totaleSpesaFondoPluriennaleVincolato
	 */
	public BigDecimal getTotaleSpesaFondoPluriennaleVincolato() {
		return totaleSpesaFondoPluriennaleVincolato;
	}

	/**
	 * @param totaleSpesaFondoPluriennaleVincolato the totaleSpesaFondoPluriennaleVincolato to set
	 */
	public void setTotaleSpesaFondoPluriennaleVincolato(
			BigDecimal totaleSpesaFondoPluriennaleVincolato) {
		this.totaleSpesaFondoPluriennaleVincolato = totaleSpesaFondoPluriennaleVincolato;
	}

	/**
	 * @return the differenzaCompetenza
	 */
	public BigDecimal getDifferenzaCompetenza() {
		return differenzaCompetenza;
	}

	/**
	 * @param differenzaCompetenza the differenzaCompetenza to set
	 */
	public void setDifferenzaCompetenza(BigDecimal differenzaCompetenza) {
		this.differenzaCompetenza = differenzaCompetenza;
	}

	/**
	 * @return the differenzaResiduo
	 */
	public BigDecimal getDifferenzaResiduo() {
		return differenzaResiduo;
	}

	/**
	 * @param differenzaResiduo the differenzaResiduo to set
	 */
	public void setDifferenzaResiduo(BigDecimal differenzaResiduo) {
		this.differenzaResiduo = differenzaResiduo;
	}

	/**
	 * @return the differenzaCassa
	 */
	public BigDecimal getDifferenzaCassa() {
		return differenzaCassa;
	}

	/**
	 * @param differenzaCassa the differenzaCassa to set
	 */
	public void setDifferenzaCassa(BigDecimal differenzaCassa) {
		this.differenzaCassa = differenzaCassa;
	}

	/**
	 * @return the differenzaFondoPluriennaleVincolato
	 */
	public BigDecimal getDifferenzaFondoPluriennaleVincolato() {
		return differenzaFondoPluriennaleVincolato;
	}

	/**
	 * @param differenzaFondoPluriennaleVincolato the differenzaFondoPluriennaleVincolato to set
	 */
	public void setDifferenzaFondoPluriennaleVincolato(
			BigDecimal differenzaFondoPluriennaleVincolato) {
		this.differenzaFondoPluriennaleVincolato = differenzaFondoPluriennaleVincolato;
	}
	
}
