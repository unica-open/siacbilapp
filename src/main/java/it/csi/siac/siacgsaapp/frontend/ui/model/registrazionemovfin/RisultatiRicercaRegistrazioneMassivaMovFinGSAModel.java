/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Model per la visualizzazione dei risultati di ricerca per la RegistrazioneMovFin (ambito GSA).
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/10/2015
 * 
 */
public class RisultatiRicercaRegistrazioneMassivaMovFinGSAModel extends RisultatiRicercaRegistrazioneMovFinBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3236316695125638560L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistrazioneMassivaMovFinGSAModel() {
		super();
		setTitolo("Risultati ricerca registrazione massiva prime note GSA");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
