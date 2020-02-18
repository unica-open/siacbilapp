/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.RicercaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca della prima nota libera
 */
public class RicercaPrimaNotaLiberaINVModel extends RicercaPrimaNotaLiberaBaseModel {

	/*per la serializzazione */
	private static final long serialVersionUID = -5966701550178946098L;
	
	
	/** Costruttore vuoto di default */
	public RicercaPrimaNotaLiberaINVModel(){
		setTitolo("Ricerca Prima Nota Libera");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_INV;
	}
	
	@Override
	public Ambito getAmbitoListe() {
		return Ambito.AMBITO_FIN;
	}
	
	@Override
	public String getAmbitoSuffix(){
		return getAmbitoFIN().getSuffix();
	}


	
}
