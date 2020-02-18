/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.notacredito.entrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.entrata.CompletaNotaCreditoEntrataInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'inserimento della prima nota integrata collegata al documento. Per l'entrata. Modulo GSA
 * 
 * @author Valentina
 * @version 1.0.0 - 14/03/2016
 */
public class CompletaNotaCreditoEntrataInsPrimaNotaIntegrataGSAModel extends CompletaNotaCreditoEntrataInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -8165966520084458613L;

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
