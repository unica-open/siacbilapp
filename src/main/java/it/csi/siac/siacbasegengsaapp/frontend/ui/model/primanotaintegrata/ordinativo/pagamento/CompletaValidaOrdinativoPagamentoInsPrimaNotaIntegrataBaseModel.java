/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento;


/**
 * Completamento e validazione della prima nota integrata sull'ordinativo di pagamento, base.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 */
public abstract class CompletaValidaOrdinativoPagamentoInsPrimaNotaIntegrataBaseModel extends GestioneOrdinativoPagamentoPrimaNotaIntegrataBaseModel{

	/** Per la validazione */
	private static final long serialVersionUID = 5531382841449446048L;

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
		return "completaValidaOrdinativoPagamentoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}


}
