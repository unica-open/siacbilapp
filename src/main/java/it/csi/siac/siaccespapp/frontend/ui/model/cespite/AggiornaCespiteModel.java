/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.cespite;

import java.math.BigDecimal;

import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCespite;

/**
 * The Class AggiornaCespiteModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class AggiornaCespiteModel extends GenericCespiteModel {

	/** PEr la serializzazione*/

	private static final long serialVersionUID = 1681476969431678375L;
	
	private Integer uidPrimaNota;
	
	private BigDecimal importoMassimoCespite;

	/**
	 * Costruttore vuoto
	 */
	public AggiornaCespiteModel() {
		setTitolo("aggiorna cespite");
	}

	
	/**
	 * @return the uidPrimaNota
	 */
	public Integer getUidPrimaNota() {
		return uidPrimaNota;
	}



	/**
	 * @param uidPrimaNota the uidPrimaNota to set
	 */
	public void setUidPrimaNota(Integer uidPrimaNota) {
		this.uidPrimaNota = uidPrimaNota;
	}

	/**
	 * @return the importoMassimoCespite
	 */
	public BigDecimal getImportoMassimoCespite() {
		return importoMassimoCespite;
	}


	/**
	 * @param importoMassimoCespite the importoMassimoCespite to set
	 */
	public void setImportoMassimoCespite(BigDecimal importoMassimoCespite) {
		this.importoMassimoCespite = importoMassimoCespite;
	}


	/**
	 * Crea request aggiorna tipo bene cespite.
	 *
	 * @return the aggiorna tipo bene cespite
	 */
	public AggiornaCespite creaRequestAggiornaCespite() {
		AggiornaCespite req = creaRequest(AggiornaCespite.class);
		req.setCespite(getCespite());
		return req;
	}

}
