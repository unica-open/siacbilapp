/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.AggiornaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'aggiornamento della causale EP.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 06/05/2015
 *
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 */
public class AggiornaPrimaNotaLiberaFINModel extends AggiornaPrimaNotaLiberaBaseModel {
	
	/**
	 * Per la serializzazione 
	 */
	private static final long serialVersionUID = 7546069440877297964L;

	/** Costruttore vuoto di default */
	public AggiornaPrimaNotaLiberaFINModel(){
		setTitolo("Aggiorna Prima Nota Libera");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
