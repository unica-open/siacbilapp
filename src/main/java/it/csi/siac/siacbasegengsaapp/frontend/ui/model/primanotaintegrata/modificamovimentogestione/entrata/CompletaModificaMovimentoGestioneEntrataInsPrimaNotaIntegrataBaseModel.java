/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.entrata;

/**
 * Completamento della prima nota integrata sulla modifica del movimento di gestione di entrata, base.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 18/11/2015
 */
public abstract class CompletaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataBaseModel extends GestioneModificaMovimentoGestioneEntrataPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 3660539884767391626L;

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
		return "completaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
