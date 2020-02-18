/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.liquidazione;



/**
 * Completamento della prima nota integrata sulla liquidazione.
 */
public abstract class CompletaLiquidazioneInsPrimaNotaIntegrataBaseModel extends GestioneLiquidazionePrimaNotaIntegrataBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5166240681408158786L;
	
	/** Costruttore vuoto di default */
	public CompletaLiquidazioneInsPrimaNotaIntegrataBaseModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	@Override
	public boolean isAggiornamento() {
		return false;
	}
	
	@Override
	public boolean isValidazione() {
		return false;
	}

	@Override
	public boolean isFromRegistrazione() {
		return true;
	}

	@Override
	public String getBaseUrl() {
		return "completaLiquidazioneInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

	
}


