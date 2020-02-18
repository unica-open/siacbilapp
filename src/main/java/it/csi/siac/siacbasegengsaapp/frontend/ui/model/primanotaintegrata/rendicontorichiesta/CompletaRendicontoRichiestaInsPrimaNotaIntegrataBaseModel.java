/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.rendicontorichiesta;



/**
 * Completamento della prima nota integrata sul rendiconto richiesta, base.
 * 
 * @author Simona Paggio 
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 */
public abstract class CompletaRendicontoRichiestaInsPrimaNotaIntegrataBaseModel extends GestioneRendicontoRichiestaPrimaNotaIntegrataBaseModel{

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
		return "completaRendicontoRichiestaInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}
