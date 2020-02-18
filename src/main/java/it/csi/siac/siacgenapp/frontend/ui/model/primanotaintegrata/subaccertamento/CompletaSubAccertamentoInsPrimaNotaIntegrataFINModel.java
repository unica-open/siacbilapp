/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.subaccertamento;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.subaccertamento.CompletaSubAccertamentoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento della prima nota integrata sul subaccertamento. Modulo FIN
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 */
public class CompletaSubAccertamentoInsPrimaNotaIntegrataFINModel extends CompletaSubAccertamentoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -1620652192388428577L;

	/** Costruttore vuoto di default */
	public CompletaSubAccertamentoInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
