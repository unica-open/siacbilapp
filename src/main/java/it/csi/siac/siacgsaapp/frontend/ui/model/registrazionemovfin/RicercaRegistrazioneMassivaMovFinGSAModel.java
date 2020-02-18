/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.RicercaRegistrazioneMovFinBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca massiva della registrazione Movimento Fin per GSA.
 */
public class RicercaRegistrazioneMassivaMovFinGSAModel extends RicercaRegistrazioneMovFinBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4173827400126429553L;

	/** Costruttore vuoto di default */
	public RicercaRegistrazioneMassivaMovFinGSAModel(){
		setTitolo("Registrazione massiva prime note GSA");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
	@Override
	public String getBaseUrl() {
		return "ricercaRegistrazioneMassivaMovFinGSA";
	}
}
