/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.conti;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti.RicercaPianoDeiContiModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca del piano dei conti.
 * 
 * @author Valentina Triolo
 * @version 1.0.0
 *
 */
public class RicercaPianoDeiContiFINModel extends RicercaPianoDeiContiModel{

	private static final long serialVersionUID = 1565888666025892640L;
	
	
	/** Costruttore vuoto di default */
	public RicercaPianoDeiContiFINModel(){
		setTitolo("Ricerca Piano Dei Conti FIN");
		setAmbito(Ambito.AMBITO_FIN);
	}
	

}
