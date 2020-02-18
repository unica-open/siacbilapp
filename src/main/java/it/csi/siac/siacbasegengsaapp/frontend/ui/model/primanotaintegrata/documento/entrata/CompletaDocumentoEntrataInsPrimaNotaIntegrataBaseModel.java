/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.entrata;


/**
 * Classe base di model per l'inserimento della prima nota integrata collegata al documento. Per l'entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 */
public abstract class CompletaDocumentoEntrataInsPrimaNotaIntegrataBaseModel extends InserisciPrimaNotaIntegrataDocumentoEntrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -7714893479854399167L;

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
		return "completaDocumentoEntrataInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}
	
	@Override
	public String getBaseUrlSubdocumento() {
		return "completaSubdocumentoEntrataInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
