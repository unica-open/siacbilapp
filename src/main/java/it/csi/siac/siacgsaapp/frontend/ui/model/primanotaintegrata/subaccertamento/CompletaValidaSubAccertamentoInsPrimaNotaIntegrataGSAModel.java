/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.subaccertamento;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.subaccertamento.CompletaValidaSubAccertamentoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento e validazione della prima nota integrata sul subaccertamento. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 */
public class CompletaValidaSubAccertamentoInsPrimaNotaIntegrataGSAModel extends CompletaValidaSubAccertamentoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 1974530519973849091L;

	/** Costruttore vuoto di default */
	public CompletaValidaSubAccertamentoInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
