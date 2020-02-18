/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.incasso;

/**
 * Completamento della prima nota integrata sull'ordinativo di incasso, base.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/10/2015
 */
public abstract class CompletaOrdinativoIncassoInsPrimaNotaIntegrataBaseModel extends GestioneOrdinativoIncassoPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -6533707036666200786L;

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
		return "completaOrdinativoIncassoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
