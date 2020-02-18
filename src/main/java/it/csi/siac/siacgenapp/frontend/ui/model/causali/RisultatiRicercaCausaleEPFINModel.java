/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.causali;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.RisultatiRicercaCausaleEPBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per i risultati di ricerca della causale EP.
 * 
 * @author Marchino Alessandro
 * @author Simona Paggio
 * @version 1.0.0 - 31/03/2015
 * @version 1.1.0 - 06/10/2015 - adattato per GEN/GSA
 *
 */
public class RisultatiRicercaCausaleEPFINModel extends RisultatiRicercaCausaleEPBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7966012718455192201L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCausaleEPFINModel() {
		setTitolo("Risultati ricerca Causali");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
}
