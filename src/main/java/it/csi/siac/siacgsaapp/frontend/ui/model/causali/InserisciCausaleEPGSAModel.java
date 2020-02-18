/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.causali;

import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.InserisciCausaleEPBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe di model per l'inserimento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/03/2015
 *
 */
public class InserisciCausaleEPGSAModel extends InserisciCausaleEPBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8266888891757997525L;
	
	

	/** Costruttore vuoto di default */
	public InserisciCausaleEPGSAModel() {
		setTitolo("Inserisci Causale");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
	@Override
	public String getBaseUrl() {
		return "inserisciCausaleEPGSA";
	}

	/**
	 * @return the classiDiConciliazione
	 */
	public List<ClasseDiConciliazione> getClassiDiConciliazione() {
		return Arrays.asList(ClasseDiConciliazione.values());
	}

}
