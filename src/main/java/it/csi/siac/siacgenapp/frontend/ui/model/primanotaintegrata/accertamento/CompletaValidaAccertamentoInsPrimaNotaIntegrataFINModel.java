/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.accertamento;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.accertamento.CompletaValidaAccertamentoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento e validazione della prima nota integrata sull'accertamento. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 */
public class CompletaValidaAccertamentoInsPrimaNotaIntegrataFINModel extends CompletaValidaAccertamentoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 8850620811747795652L;

	/** Costruttore vuoto di default */
	public CompletaValidaAccertamentoInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
