/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.DettaglioMovimentiPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca del dettaglio dei movimenti della prima nota libera
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 *
 */
public class DettaglioMovimentiPrimaNotaLiberaINVModel extends  DettaglioMovimentiPrimaNotaLiberaBaseModel {
	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -4976915618633984275L;

	/** Costruttore vuoto di default */
	public DettaglioMovimentiPrimaNotaLiberaINVModel() {
		setTitolo("Risultati ricerca Prima Nota Libera - Dettaglio movimenti");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_INV;
	}

	@Override
	public String getAmbitoSuffix() {
		return "INV";
	}
}
