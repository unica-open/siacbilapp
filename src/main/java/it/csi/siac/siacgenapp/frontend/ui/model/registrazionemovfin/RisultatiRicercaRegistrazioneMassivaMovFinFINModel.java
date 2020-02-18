/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Model per la visualizzazione dei risultati di ricerca massiva per la RegistrazioneMovFin (ambito GEN).
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 24/11/2016
 * 
 */
public class RisultatiRicercaRegistrazioneMassivaMovFinFINModel extends RisultatiRicercaRegistrazioneMovFinBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2604532833113997467L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistrazioneMassivaMovFinFINModel() {
		super();
		setTitolo("Risultati ricerca registrazione massiva prime note");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
