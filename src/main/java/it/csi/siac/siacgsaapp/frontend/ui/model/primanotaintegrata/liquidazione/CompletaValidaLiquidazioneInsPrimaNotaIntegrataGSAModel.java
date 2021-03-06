/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.liquidazione;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.liquidazione.CompletaValidaLiquidazioneInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento e validazione della prima nota integrata sulla liquidazione.
 */
public class CompletaValidaLiquidazioneInsPrimaNotaIntegrataGSAModel extends CompletaValidaLiquidazioneInsPrimaNotaIntegrataBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5166240681408158786L;
	
	/** Costruttore vuoto di default */
	public CompletaValidaLiquidazioneInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}


