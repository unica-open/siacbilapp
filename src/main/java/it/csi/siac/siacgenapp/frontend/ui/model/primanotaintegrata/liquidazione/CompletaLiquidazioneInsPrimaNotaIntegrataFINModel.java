/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.liquidazione;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.liquidazione.CompletaLiquidazioneInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento della prima nota integrata sulla liquidazione.
 */
public class CompletaLiquidazioneInsPrimaNotaIntegrataFINModel extends CompletaLiquidazioneInsPrimaNotaIntegrataBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5166240681408158786L;
	
	/** Costruttore vuoto di default */
	public CompletaLiquidazioneInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}


