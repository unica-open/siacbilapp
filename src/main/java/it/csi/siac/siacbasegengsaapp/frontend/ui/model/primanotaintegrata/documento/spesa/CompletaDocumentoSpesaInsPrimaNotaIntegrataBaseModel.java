/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.spesa;


/**
 * Classe base di model per l'inserimento della prima nota integrata collegata al documento. Per la spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 */
public abstract class CompletaDocumentoSpesaInsPrimaNotaIntegrataBaseModel extends InserisciPrimaNotaIntegrataDocumentoSpesaBaseModel{

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
		return "completaDocumentoSpesaInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}
	
	@Override
	public String getBaseUrlSubdocumento() {
		return "completaSubdocumentoSpesaInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
