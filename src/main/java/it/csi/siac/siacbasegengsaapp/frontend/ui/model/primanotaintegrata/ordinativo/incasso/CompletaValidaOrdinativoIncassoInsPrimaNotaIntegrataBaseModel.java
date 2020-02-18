/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.incasso;

/**
 * Completamento e validazione della prima nota integrata sull'ordinativo di incasso, base.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/10/2015
 */
public abstract class CompletaValidaOrdinativoIncassoInsPrimaNotaIntegrataBaseModel extends GestioneOrdinativoIncassoPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -8158264034254047057L;

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
		return "completaValidaOrdinativoIncassoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
