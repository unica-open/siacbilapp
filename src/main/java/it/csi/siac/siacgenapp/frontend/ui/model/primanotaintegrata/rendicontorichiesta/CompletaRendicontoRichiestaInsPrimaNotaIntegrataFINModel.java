/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.rendicontorichiesta;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.rendicontorichiesta.CompletaRendicontoRichiestaInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento della prima nota integrata sul rendiconto richiesta. Modulo GEN
 * 
 * @author Simona Paggio
 * @version 1.1.0 - 15/10/2015 - gestione GEN/GSA
 */
public class CompletaRendicontoRichiestaInsPrimaNotaIntegrataFINModel extends CompletaRendicontoRichiestaInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione **/
	private static final long serialVersionUID = 694629638127888034L;

	/** Costruttore vuoto di default */
	public CompletaRendicontoRichiestaInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
