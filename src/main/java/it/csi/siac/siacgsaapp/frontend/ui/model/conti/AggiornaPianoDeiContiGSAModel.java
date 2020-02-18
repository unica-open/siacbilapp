/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.conti;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti.AggiornaPianoDeiContiModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'aggiornamento del piano dei conti.
 * 
 * @author Valentina Triolo
 * @version 1.0.0
 *
 */
public class AggiornaPianoDeiContiGSAModel extends AggiornaPianoDeiContiModel{

	private static final long serialVersionUID = 1475325577751418329L;
	
	/** Costruttore vuoto di default */
	public AggiornaPianoDeiContiGSAModel(){
		setTitolo("Aggiorna Piano Dei Conti GSA");
		setAmbito(Ambito.AMBITO_GSA);
	}


}
