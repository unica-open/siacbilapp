/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento.CompletaOrdinativoPagamentoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Completamento della prima nota integrata sull'ordinativo di pagamento. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 */
public class CompletaOrdinativoPagamentoInsPrimaNotaIntegrataFINModel extends CompletaOrdinativoPagamentoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -4212300635639702848L;

	/** Costruttore vuoto di default */
	public CompletaOrdinativoPagamentoInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
