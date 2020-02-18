/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.entrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.entrata.CompletaValidaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Completamento e validazione della prima nota integrata sulla modifica del movimento di gestione di entrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/11/2015
 */
public class CompletaValidaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataGSAModel extends CompletaValidaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -8543504703146108751L;

	/** Costruttore vuoto di default */
	public CompletaValidaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
