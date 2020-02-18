/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.causali;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.AggiornaCausaleEPBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'aggiornamento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/04/2015
 *
 */
public class AggiornaCausaleEPFINModel extends AggiornaCausaleEPBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 568572254744064120L;
	
	/** Costruttore vuoto di default */
	public AggiornaCausaleEPFINModel() {
		setTitolo("Aggiorna Causale");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

	@Override
	public String getBaseUrl() {
		return "aggiornaCausaleEPFIN";
	}
	
	/**
	 * @return the ambitoFIN
	 */
	public Ambito getAmbitoFIN() {
		return Ambito.AMBITO_FIN;
	}
}
