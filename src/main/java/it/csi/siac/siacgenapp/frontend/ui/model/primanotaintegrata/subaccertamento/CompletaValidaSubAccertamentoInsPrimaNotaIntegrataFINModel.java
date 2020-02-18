/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.subaccertamento;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.subaccertamento.CompletaValidaSubAccertamentoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento e validazione della prima nota integrata sul subaccertamento. Modulo FIN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 */
public class CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINModel extends CompletaValidaSubAccertamentoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -2841480388337906654L;

	/** Costruttore vuoto di default */
	public CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
