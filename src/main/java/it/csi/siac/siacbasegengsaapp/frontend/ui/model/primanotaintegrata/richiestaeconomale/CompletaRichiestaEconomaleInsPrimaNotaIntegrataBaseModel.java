/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.richiestaeconomale;



/**
 * Completamento della prima nota integrata sulla richiesta economale, base.
 * 
 * @author Simona Paggio 
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 */
public abstract class CompletaRichiestaEconomaleInsPrimaNotaIntegrataBaseModel extends GestioneRichiestaEconomalePrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -2774660985466483971L;

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
		return "completaRichiestaEconomaleInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
