/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.causali;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.RicercaCausaleEPBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca della causale EP.
 * 
 * @author Marchino Alessandro
 * @author Simona Paggio
 * @version 1.0.0 - 31/03/2015
 * @version 1.1.0 - 06/10/2015 - adattato per GEN/GSA
 *
 */
public class RicercaCausaleEPFINModel extends RicercaCausaleEPBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -332114149548408690L;
	
	
	/** Costruttore vuoto di default */
	public RicercaCausaleEPFINModel() {
		setTitolo("Ricerca Causale");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
}
