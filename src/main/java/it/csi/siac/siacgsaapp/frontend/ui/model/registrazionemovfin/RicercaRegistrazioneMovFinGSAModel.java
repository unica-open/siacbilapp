/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.RicercaRegistrazioneMovFinBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca della registrazione Movimento Fin per GSA.
 */
public class RicercaRegistrazioneMovFinGSAModel extends RicercaRegistrazioneMovFinBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6350818284630988844L;

	/** Costruttore vuoto di default */
	public RicercaRegistrazioneMovFinGSAModel(){
		setTitolo("Ricerca registro");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
	@Override
	public String getBaseUrl() {
		return "ricercaRegistrazioneMovFinGSA";
	}
}
