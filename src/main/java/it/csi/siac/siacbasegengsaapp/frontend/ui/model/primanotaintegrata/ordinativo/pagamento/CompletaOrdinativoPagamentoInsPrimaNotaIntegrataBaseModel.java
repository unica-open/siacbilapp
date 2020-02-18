/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento;

/**
 * Completamento della prima nota integrata sull'ordinativo di pagamento, base.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 */
public abstract class CompletaOrdinativoPagamentoInsPrimaNotaIntegrataBaseModel extends GestioneOrdinativoPagamentoPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -3786038440911940667L;

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
		return "completaOrdinativoPagamentoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
