/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.DettaglioMovimentiPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca del dettaglio dei movimenti della prima nota libera - GESTIONE GSA
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
public class DettaglioMovimentiPrimaNotaIntegrataManualeGSAModel extends DettaglioMovimentiPrimaNotaLiberaBaseModel {
	
	
	/**
	 * 	Per la serializzazione
	 */
	private static final long serialVersionUID = 686912385555779502L;

	/** Costruttore vuoto di default */
	public DettaglioMovimentiPrimaNotaIntegrataManualeGSAModel() {
		setTitolo("Risultati ricerca Prima Nota Integrata Manuale - GSA - Dettaglio movimenti");
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
