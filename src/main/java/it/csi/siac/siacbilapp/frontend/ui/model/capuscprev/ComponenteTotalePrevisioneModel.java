/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscprev;

import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaPrevisioneModel;

public class ComponenteTotalePrevisioneModel extends CapitoloUscitaPrevisioneModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6498661454470917171L;
	private int uid;
	private String descrizione;
	private BigDecimal importoEntrata = BigDecimal.ZERO;
	private BigDecimal importoSpesa = BigDecimal.ZERO;
	private BigDecimal importoDifferenza = BigDecimal.ZERO;
	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
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
	 * @return the importoEntrata
	 */
	public BigDecimal getImportoEntrata() {
		return importoEntrata;
	}
	/**
	 * @param importoEntrata the importoEntrata to set
	 */
	public void setImportoEntrata(BigDecimal importoEntrata) {
		this.importoEntrata = importoEntrata;
	}
	/**
	 * @return the importoSpesa
	 */
	public BigDecimal getImportoSpesa() {
		return importoSpesa;
	}
	/**
	 * @param importoSpesa the importoSpesa to set
	 */
	public void setImportoSpesa(BigDecimal importoSpesa) {
		this.importoSpesa = importoSpesa;
	}
	/**
	 * @return the importoDifferenza
	 */
	public BigDecimal getImportoDifferenza() {
		return importoDifferenza;
	}
	/**
	 * @param importoDifferenza the importoDifferenza to set
	 */
	public void setImportoDifferenza(BigDecimal importoDifferenza) {
		this.importoDifferenza = importoDifferenza;
	}
	
}
