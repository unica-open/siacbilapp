/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.causali;

import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.AggiornaCausaleEPBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe di model per l'aggiornamento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/04/2015
 *
 */
public class AggiornaCausaleEPGSAModel extends AggiornaCausaleEPBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 568572254744064120L;
	
	
	/** Costruttore vuoto di default */
	public AggiornaCausaleEPGSAModel() {
		setTitolo("Aggiorna Causale");
	}
	
	/**
	 * @return the classiDiConciliazione
	 */
	public List<ClasseDiConciliazione> getClassiDiConciliazione() {
		return Arrays.asList(ClasseDiConciliazione.values());
	}

	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

	@Override
	public String getBaseUrl() {
		return "aggiornaCausaleEPGSA";
	}
	
}
