/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.subimpegno;

/**
 * Completamento e validazione della prima nota integrata sul subimpegno, base.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 */
public abstract class CompletaValidaSubImpegnoInsPrimaNotaIntegrataBaseModel extends GestioneSubImpegnoPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 6649914538705470029L;

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
		return "completaValidaSubImpegnoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
