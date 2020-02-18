/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.conti;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti.InserisciFiglioPianoDeiContiModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'inserimento del figlio del piano dei conti.
 * 
 * @author Valentina Triolo
 * @version 1.0.0
 *
 */
public class InserisciFiglioPianoDeiContiGSAModel extends InserisciFiglioPianoDeiContiModel{

	private static final long serialVersionUID = 1565888666025892640L;
	
	
	
	/** Costruttore vuoto di default */
	public InserisciFiglioPianoDeiContiGSAModel(){
		setTitolo("Inserisci Figlio Piano Dei Conti GSA");
		setAmbito(Ambito.AMBITO_GSA);
	}
	
}
