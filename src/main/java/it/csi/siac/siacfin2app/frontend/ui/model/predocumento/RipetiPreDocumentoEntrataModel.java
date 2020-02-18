/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;

/**
 * Classe di model per la ripetizione del PreDocumento di Entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/04/2017
 *
 */
public class RipetiPreDocumentoEntrataModel extends BaseInserimentoPreDocumentoEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8679360741899448946L;
	
	private boolean doLoadCausale;

	/** Costruttore vuoto di default */
	public RipetiPreDocumentoEntrataModel() {
		setTitolo("Ripeti Predisposizione di Incasso");
	}

	/**
	 * @return the doLoadCausale
	 */
	public boolean isDoLoadCausale() {
		return doLoadCausale;
	}

	/**
	 * @param doLoadCausale the doLoadCausale to set
	 */
	public void setDoLoadCausale(boolean doLoadCausale) {
		this.doLoadCausale = doLoadCausale;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPreDocumentoEntrata}.
	 * @return la request creata
	 */
	public RicercaDettaglioPreDocumentoEntrata creaRequestRicercaDettaglioPreDocumentoEntrata() {
		RicercaDettaglioPreDocumentoEntrata req = creaRequest(RicercaDettaglioPreDocumentoEntrata.class);
		req.setPreDocumentoEntrata(getPreDocumento());
		return req;
	}
}
