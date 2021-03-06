/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.RicercaRegistrazioneMovFinBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca della registrazione Movimento Fin per GEN.
 */
public class RicercaRegistrazioneMovFinFINModel extends RicercaRegistrazioneMovFinBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8806707493634574540L;
	
	
	/** Costruttore vuoto di default */
	public RicercaRegistrazioneMovFinFINModel(){
		setTitolo("Ricerca registro");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

	@Override
	public String getBaseUrl() {
		return "ricercaRegistrazioneMovFinFIN";
	}
	
}
