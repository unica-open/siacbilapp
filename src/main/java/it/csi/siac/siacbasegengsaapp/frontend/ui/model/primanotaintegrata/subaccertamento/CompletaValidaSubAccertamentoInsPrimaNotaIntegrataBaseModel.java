/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.subaccertamento;


/**
 * Completamento e validazione della prima nota integrata sul subaccertamento, base.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 */
public abstract class CompletaValidaSubAccertamentoInsPrimaNotaIntegrataBaseModel extends GestioneSubAccertamentoPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -4723311273299551486L;

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
		return "completaValidaSubAccertamentoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
