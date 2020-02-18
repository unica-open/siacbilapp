/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.GestioneRateiERiscontiPrimaNotaIntegrataDocumentoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Ricerca della prima nota integrata. Modulo GEN.
 * 
 * @author paggio
 * @author Marchino Alessandro
 *
 * @version 1.0.0 12/05/2015
 * @version 1.0.1 13/05/2015
 * @version 1.1.0 08/10/2015 - gestione GEN/GSA
 */
public class GestioneRateiERiscontiPrimaNotaIntegrataDocumentoGSAModel extends GestioneRateiERiscontiPrimaNotaIntegrataDocumentoBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7204916520208464831L;


	/** Costruttore vuoto di default */
	public GestioneRateiERiscontiPrimaNotaIntegrataDocumentoGSAModel() {
		setTitolo("ratei e risconti");
	}

	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
	
}
