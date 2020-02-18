/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.notacredito.entrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.entrata.CompletaValidaNotaCreditoEntrataInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'inserimento e la validazione della prima nota integrata collegata al documento. Per l'entrata. Modulo GEN
 * 
 * @author Valentina
 * @version 1.0.0 - 14/03/2016
 */
public class CompletaValidaNotaCreditoEntrataInsPrimaNotaIntegrataFINModel extends CompletaValidaNotaCreditoEntrataInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -6862798688007585586L;

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
