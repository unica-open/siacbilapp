/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.richiestaeconomale;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.richiestaeconomale.CompletaRichiestaEconomaleInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento della prima nota integrata sulla richiesta economale. Modulo GEN
 * 
 * @author Simona Paggio
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 */
public class CompletaRichiestaEconomaleInsPrimaNotaIntegrataGSAModel extends CompletaRichiestaEconomaleInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione **/
	private static final long serialVersionUID = 694629638127888034L;

	/** Costruttore vuoto di default */
	public CompletaRichiestaEconomaleInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
