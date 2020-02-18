/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.richiestaeconomale;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.richiestaeconomale.CompletaValidaRichiestaEconomaleInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento e validazione della prima nota integrata sulla richiesta economale. Modulo GEN
 * 
 * @author Simna Paggio
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 */
public class CompletaValidaRichiestaEconomaleInsPrimaNotaIntegrataGSAModel extends CompletaValidaRichiestaEconomaleInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione **/
	private static final long serialVersionUID = 8083617231521938382L;

	/** Costruttore vuoto di default */
	public CompletaValidaRichiestaEconomaleInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
