/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.accertamento;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.accertamento.CompletaValidaAccertamentoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento e validazione della prima nota integrata sull'accertamento. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 */
public class CompletaValidaAccertamentoInsPrimaNotaIntegrataGSAModel extends CompletaValidaAccertamentoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -6518366081845175962L;

	/** Costruttore vuoto di default */
	public CompletaValidaAccertamentoInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
