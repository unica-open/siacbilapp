/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.RisultatiRicercaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca della prima nota libera
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 5/5/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 *
 */
public class RisultatiRicercaPrimaNotaLiberaFINModel extends RisultatiRicercaPrimaNotaLiberaBaseModel {
	

	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -8853479390854228236L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaLiberaFINModel() {
		setTitolo("Risultati ricerca Prima Nota Libera");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
	@Override
	public String getAmbitoSuffix() {
		return "FIN";
	}
}
