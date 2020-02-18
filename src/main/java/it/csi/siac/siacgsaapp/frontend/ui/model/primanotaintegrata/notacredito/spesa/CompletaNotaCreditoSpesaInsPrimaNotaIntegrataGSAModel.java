/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.notacredito.spesa;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.spesa.CompletaNotaCreditoSpesaInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'inserimento della prima nota integrata collegata al documento. Per la spesa. Modulo GEN
 * 
 * @author Valentina
 * @version 1.0.0 - 14/03/2016
 */
public class CompletaNotaCreditoSpesaInsPrimaNotaIntegrataGSAModel extends CompletaNotaCreditoSpesaInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -8177880667329025681L;


	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
