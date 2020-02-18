/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.importi;

import java.math.BigDecimal;

/**
 * Wrapper per gli importi in dare e avere
 * @author Marchino Alessandro
 *
 */
public class ImportiDareAvere {

	private int numeroScrittureDare = 0;
	private int numeroScrittureAvere = 0;
	private BigDecimal totaleDare = BigDecimal.ZERO;
	private BigDecimal totaleAvere = BigDecimal.ZERO;
	
	/**
	 * @return the numeroScrittureDare
	 */
	public int getNumeroScrittureDare() {
		return this.numeroScrittureDare;
	}

	/**
	 * @return the numeroScrittureAvere
	 */
	public int getNumeroScrittureAvere() {
		return this.numeroScrittureAvere;
	}

	/**
	 * @return the totaleDare
	 */
	public BigDecimal getTotaleDare() {
		return this.totaleDare;
	}

	/**
	 * @return the totaleAvere
	 */
	public BigDecimal getTotaleAvere() {
		return this.totaleAvere;
	}
	
	/**
	 * Incrementa il numero di scritture in dare
	 */
	public void incrementNumeroScrittureDare() {
		numeroScrittureDare++;
	}
	/**
	 * Incrementa il numero di scritture in avere
	 */
	public void incrementNumeroScrittureAvere() {
		numeroScrittureAvere++;
	}
	/**
	 * Aggiunge un importo in dare
	 * @param augend l'importo da aggiungere
	 */
	public void addTotaleDare(BigDecimal augend) {
		totaleDare = totaleDare.add(augend);
	}
	/**
	 * Aggiunge un importo in avere
	 * @param augend l'importo da aggiungere
	 */
	public void addTotaleAvere(BigDecimal augend) {
		totaleAvere = totaleAvere.add(augend);
	}

}
