/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.spesa;

import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe base di model per l'inserimento e la validazione della prima nota integrata collegata al documento. Per la spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 */
public abstract class CompletaValidaDocumentoSpesaInsPrimaNotaIntegrataBaseModel extends InserisciPrimaNotaIntegrataDocumentoSpesaBaseModel{

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
		return "completaValidaDocumentoSpesaInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}
	
	@Override
	public String getBaseUrlSubdocumento() {
		return "completaValidaSubdocumentoSpesaInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

	/**
	 * Checks if is validazione abilitata.
	 *
	 * @return true, if is validazione abilitata
	 */
	public boolean isValidazioneAbilitata() {
		return StatoOperativoPrimaNota.PROVVISORIO.equals( this.getPrimaNota().getStatoOperativoPrimaNota());
	}
}
