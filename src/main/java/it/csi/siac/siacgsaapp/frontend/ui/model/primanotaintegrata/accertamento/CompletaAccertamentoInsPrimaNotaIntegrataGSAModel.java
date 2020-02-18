/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.accertamento;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.accertamento.CompletaAccertamentoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento della prima nota integrata sull'accertamento. Modulo GSA
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 */
public class CompletaAccertamentoInsPrimaNotaIntegrataGSAModel extends CompletaAccertamentoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -513525706836637004L;

	/** Costruttore vuoto di default */
	public CompletaAccertamentoInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
