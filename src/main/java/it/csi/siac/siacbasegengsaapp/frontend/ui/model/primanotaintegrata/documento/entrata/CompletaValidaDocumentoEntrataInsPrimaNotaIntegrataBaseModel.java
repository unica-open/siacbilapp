/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.entrata;


/**
 * Classe base di model per l'inserimento e la validazione della prima nota integrata collegata al documento. Per l'entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 */
public abstract class CompletaValidaDocumentoEntrataInsPrimaNotaIntegrataBaseModel extends InserisciPrimaNotaIntegrataDocumentoEntrataBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3713955341662339385L;

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
		return "completaValidaDocumentoEntrataInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}
	
	@Override
	public String getBaseUrlSubdocumento() {
		return "completaValidaSubdocumentoEntrataInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
