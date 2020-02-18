/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento.CompletaValidaOrdinativoPagamentoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Completamento e validazione della prima nota integrata sull'ordinativo di pagamento. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 */
public class CompletaValidaOrdinativoPagamentoInsPrimaNotaIntegrataFINModel extends CompletaValidaOrdinativoPagamentoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 4896337154538340354L;

	/** Costruttore vuoto di default */
	public CompletaValidaOrdinativoPagamentoInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
