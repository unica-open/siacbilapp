/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.causali;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.RisultatiRicercaCausaleEPBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per i risultati di ricerca della causale EP.
 * 
 * @author Simona Paggio
 * @version 1.1.0 - 06/10/2015 - adattato per GEN/GSA
 *
 */
public class RisultatiRicercaCausaleEPGSAModel extends RisultatiRicercaCausaleEPBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 174093760574097021L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCausaleEPGSAModel() {
		setTitolo("Risultati ricerca Causali");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
}
