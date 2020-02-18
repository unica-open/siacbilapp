/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.spesa;


/**
 * Classe base di model per l'inserimento della prima nota integrata collegata alla nota di credito. Per la spesa.
 * 
 * @author Valentina
 * @version 1.0.0 - 10/03/2016
 */
public abstract class CompletaNotaCreditoSpesaInsPrimaNotaIntegrataBaseModel extends InserisciPrimaNotaIntegrataNotaCreditoSpesaBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 6728480492101897538L;

	@Override
	public boolean isValidazione() {
		return false;
	}
	
	@Override
	public boolean isFromRegistrazione() {
		// TODO: come calcolarlo?
		return false;
	}
	
	@Override
	public String getBaseUrl() {
		return "completaNotaCreditoSpesaInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}
	
	@Override
	public String getBaseUrlSubdocumento() {
		return "completaSubdocumentoSpesaNotaCreditoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
