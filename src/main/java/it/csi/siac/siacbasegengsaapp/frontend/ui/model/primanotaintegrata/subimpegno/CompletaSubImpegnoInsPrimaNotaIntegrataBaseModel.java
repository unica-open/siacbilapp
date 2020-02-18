/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.subimpegno;



/**
 * Completamento della prima nota integrata sul subimpegno, base.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 */
public abstract class CompletaSubImpegnoInsPrimaNotaIntegrataBaseModel extends GestioneSubImpegnoPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 9100053457477779388L;

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
		return "completaSubImpegnoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
