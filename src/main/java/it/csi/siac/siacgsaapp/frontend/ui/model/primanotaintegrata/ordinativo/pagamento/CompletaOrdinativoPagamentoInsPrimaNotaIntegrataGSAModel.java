/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento.CompletaOrdinativoPagamentoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Completamento della prima nota integrata sull'ordinativo di pagamento. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 */
public class CompletaOrdinativoPagamentoInsPrimaNotaIntegrataGSAModel extends CompletaOrdinativoPagamentoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -1711263390123722952L;

	/** Costruttore vuoto di default */
	public CompletaOrdinativoPagamentoInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
