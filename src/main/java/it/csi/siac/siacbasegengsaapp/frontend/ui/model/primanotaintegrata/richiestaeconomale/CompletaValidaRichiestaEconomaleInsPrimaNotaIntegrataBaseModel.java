/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.richiestaeconomale;

/**
 * Completamento e validazione della prima nota integrata sulla richiesta economale, base.
 * 
 * @author Simona Paggio 
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 */
public abstract class CompletaValidaRichiestaEconomaleInsPrimaNotaIntegrataBaseModel extends GestioneRichiestaEconomalePrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -3757449916658899522L;

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
		return "completaValidaRichiestaEconomaleInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
