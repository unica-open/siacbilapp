/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.entrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.entrata.CompletaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Completamento della prima nota integrata sulla modifica del movimento di gestione di entrata. Modulo FIN
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/11/2015
 */
public class CompletaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataFINModel extends CompletaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 8095493957851286262L;

	/** Costruttore vuoto di default */
	public CompletaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
