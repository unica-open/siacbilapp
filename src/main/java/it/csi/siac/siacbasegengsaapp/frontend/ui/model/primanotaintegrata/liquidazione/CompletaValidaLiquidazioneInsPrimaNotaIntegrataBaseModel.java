/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.liquidazione;



/**
 * Completamento e validazione della prima nota integrata sulla liquidazione.
 */
public abstract class CompletaValidaLiquidazioneInsPrimaNotaIntegrataBaseModel extends GestioneLiquidazionePrimaNotaIntegrataBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5166240681408158786L;
	
	/** Costruttore vuoto di default */
	public CompletaValidaLiquidazioneInsPrimaNotaIntegrataBaseModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	@Override
	public boolean isAggiornamento() {
		return false;
	}
	
	@Override
	public boolean isValidazione() {
		return true;
	}

	@Override
	public boolean isFromRegistrazione() {
		return true;
	}

	@Override
	public String getBaseUrl() {
		return "completaValidaLiquidazioneInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}



}


