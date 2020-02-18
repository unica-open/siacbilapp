/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.accertamento;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.accertamento.CompletaAccertamentoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento della prima nota integrata sull'accertamento. Modulo GEN
 *
 * @author Marchino Alessandro
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 */
public class CompletaAccertamentoInsPrimaNotaIntegrataFINModel extends CompletaAccertamentoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 7393765070240276142L;

	/** Costruttore vuoto di default */
	public CompletaAccertamentoInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
