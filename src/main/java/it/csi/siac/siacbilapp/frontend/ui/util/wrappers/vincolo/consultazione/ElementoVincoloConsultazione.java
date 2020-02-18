/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.vincolo.consultazione;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.vincolo.ElementoCapitoloVincolo;
import it.csi.siac.siacbilser.model.Vincolo;

/**
 * Classe di wrap per il Vincolo per l'uso nella consultazione del capitolo.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 08/01/2013
 *
 */
public class ElementoVincoloConsultazione implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4835483875380046624L;

	private Vincolo vincolo;
	
	private String stato;
	private String tipoBilancio;
	
	private Integer uidDaConsultare;
	
	private List<ElementoCapitoloVincolo> listaCapitoliEntrata;
	private List<ElementoCapitoloVincolo> listaCapitoliUscita;
	
	private BigDecimal totaleStanziamentoEntrata0;
	private BigDecimal totaleStanziamentoEntrata1;
	private BigDecimal totaleStanziamentoEntrata2;
	
	private BigDecimal totaleStanziamentoUscita0;
	private BigDecimal totaleStanziamentoUscita1;
	private BigDecimal totaleStanziamentoUscita2;
	
	/** Costruttore vuoto di default */
	public ElementoVincoloConsultazione() {
		super();
	}
	
	@Override
	public int getUid() {
		return getVincolo().getUid();
	}

	/**
	 * @return the vincolo
	 */
	public Vincolo getVincolo() {
		return vincolo;
	}

	/**
	 * @param vincolo the vincolo to set
	 */
	public void setVincolo(Vincolo vincolo) {
		this.vincolo = vincolo;
	}

	/**
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}

	/**
	 * @return the tipoBilancio
	 */
	public String getTipoBilancio() {
		return tipoBilancio;
	}

	/**
	 * @param tipoBilancio the tipoBilancio to set
	 */
	public void setTipoBilancio(String tipoBilancio) {
		this.tipoBilancio = tipoBilancio;
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
	 * @return the listaCapitoliEntrata
	 */
	public List<ElementoCapitoloVincolo> getListaCapitoliEntrata() {
		return listaCapitoliEntrata;
	}

	/**
	 * @param listaCapitoliEntrata the listaCapitoliEntrata to set
	 */
	public void setListaCapitoliEntrata(
			List<ElementoCapitoloVincolo> listaCapitoliEntrata) {
		this.listaCapitoliEntrata = listaCapitoliEntrata;
	}

	/**
	 * @return the listaCapitoliUscita
	 */
	public List<ElementoCapitoloVincolo> getListaCapitoliUscita() {
		return listaCapitoliUscita;
	}

	/**
	 * @param listaCapitoliUscita the listaCapitoliUscita to set
	 */
	public void setListaCapitoliUscita(
			List<ElementoCapitoloVincolo> listaCapitoliUscita) {
		this.listaCapitoliUscita = listaCapitoliUscita;
	}

	/**
	 * @return the totaleStanziamentoEntrata0
	 */
	public BigDecimal getTotaleStanziamentoEntrata0() {
		return totaleStanziamentoEntrata0;
	}

	/**
	 * @param totaleStanziamentoEntrata0 the totaleStanziamentoEntrata0 to set
	 */
	public void setTotaleStanziamentoEntrata0(BigDecimal totaleStanziamentoEntrata0) {
		this.totaleStanziamentoEntrata0 = totaleStanziamentoEntrata0;
	}

	/**
	 * @return the totaleStanziamentoEntrata1
	 */
	public BigDecimal getTotaleStanziamentoEntrata1() {
		return totaleStanziamentoEntrata1;
	}

	/**
	 * @param totaleStanziamentoEntrata1 the totaleStanziamentoEntrata1 to set
	 */
	public void setTotaleStanziamentoEntrata1(BigDecimal totaleStanziamentoEntrata1) {
		this.totaleStanziamentoEntrata1 = totaleStanziamentoEntrata1;
	}

	/**
	 * @return the totaleStanziamentoEntrata2
	 */
	public BigDecimal getTotaleStanziamentoEntrata2() {
		return totaleStanziamentoEntrata2;
	}

	/**
	 * @param totaleStanziamentoEntrata2 the totaleStanziamentoEntrata2 to set
	 */
	public void setTotaleStanziamentoEntrata2(BigDecimal totaleStanziamentoEntrata2) {
		this.totaleStanziamentoEntrata2 = totaleStanziamentoEntrata2;
	}

	/**
	 * @return the totaleStanziamentoUscita0
	 */
	public BigDecimal getTotaleStanziamentoUscita0() {
		return totaleStanziamentoUscita0;
	}

	/**
	 * @param totaleStanziamentoUscita0 the totaleStanziamentoUscita0 to set
	 */
	public void setTotaleStanziamentoUscita0(BigDecimal totaleStanziamentoUscita0) {
		this.totaleStanziamentoUscita0 = totaleStanziamentoUscita0;
	}

	/**
	 * @return the totaleStanziamentoUscita1
	 */
	public BigDecimal getTotaleStanziamentoUscita1() {
		return totaleStanziamentoUscita1;
	}

	/**
	 * @param totaleStanziamentoUscita1 the totaleStanziamentoUscita1 to set
	 */
	public void setTotaleStanziamentoUscita1(BigDecimal totaleStanziamentoUscita1) {
		this.totaleStanziamentoUscita1 = totaleStanziamentoUscita1;
	}

	/**
	 * @return the totaleStanziamentoUscita2
	 */
	public BigDecimal getTotaleStanziamentoUscita2() {
		return totaleStanziamentoUscita2;
	}

	/**
	 * @param totaleStanziamentoUscita2 the totaleStanziamentoUscita2 to set
	 */
	public void setTotaleStanziamentoUscita2(BigDecimal totaleStanziamentoUscita2) {
		this.totaleStanziamentoUscita2 = totaleStanziamentoUscita2;
	}
	
}
