/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.entrata;

/**
 * Classe base di model per l'inserimento e la validazione della prima nota integrata collegata alla nota di credito. Per l'entrata.
 * 
 * @author Valentina
 * @version 1.0.0 - 14/03/2016
 */
public abstract class CompletaValidaNotaCreditoEntrataInsPrimaNotaIntegrataBaseModel extends InserisciPrimaNotaIntegrataNotaCreditoEntrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 4133354990532577066L;

	@Override
	public boolean isValidazione() {
		return true;
	}
	
	@Override
	public boolean isFromRegistrazione() {
		// TODO: come calcolarlo?
		return false;
	}
	
	@Override
	public String getBaseUrl() {
		return "completaValidaNotaCreditoEntrataInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}
	
	@Override
	public String getBaseUrlSubdocumento() {
		return "completaValidaSubdocumentoEntrataNotaCreditoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
