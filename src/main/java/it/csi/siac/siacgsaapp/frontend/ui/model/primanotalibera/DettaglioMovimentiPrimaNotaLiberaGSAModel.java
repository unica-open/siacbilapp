/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.DettaglioMovimentiPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca del dettaglio dei movimenti della prima nota libera - GESTIONE GSA
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 15/10/2015
 *
 */
public class DettaglioMovimentiPrimaNotaLiberaGSAModel extends DettaglioMovimentiPrimaNotaLiberaBaseModel {
	
	
	/**
	 * 	Per la serializzazione
	 */
	private static final long serialVersionUID = 686912385555779502L;

	/** Costruttore vuoto di default */
	public DettaglioMovimentiPrimaNotaLiberaGSAModel() {
		setTitolo("Risultati ricerca Prima Nota Libera - Dettaglio movimenti");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

	@Override
	public String getAmbitoSuffix() {
		return "GSA";
	}

}
