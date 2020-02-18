/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.spesa;

/**
 * Classe base di model per l'inserimento e la validazione della prima nota integrata collegata alla nota di credito. Per la spesa.
 * 
 * @author Valentina
 * @version 1.0.0 - 10/03/2016
 */
public abstract class CompletaValidaNotaCreditoSpesaInsPrimaNotaIntegrataBaseModel extends InserisciPrimaNotaIntegrataNotaCreditoSpesaBaseModel{

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
		return "completaValidaNotaCreditoSpesaInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}
	
	@Override
	public String getBaseUrlSubdocumento() {
		return "completaValidaSubdocumentoSpesaNotaCreditoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
