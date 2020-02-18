/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.causali;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.InserisciCausaleEPBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'inserimento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/03/2015
 *
 */
public class InserisciCausaleEPFINModel extends InserisciCausaleEPBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8266888891757997525L;

		
	/** Costruttore vuoto di default */
	public InserisciCausaleEPFINModel() {
		setTitolo("Inserisci Causale");
		
	}
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	@Override
	public String getBaseUrl() {
		return "inserisciCausaleEPFIN";
	}
	
	/**
	 * @return the ambitoFIN
	 */
	public Ambito getAmbitoFIN() {
		return Ambito.AMBITO_FIN;
	}

	
	
	
}
