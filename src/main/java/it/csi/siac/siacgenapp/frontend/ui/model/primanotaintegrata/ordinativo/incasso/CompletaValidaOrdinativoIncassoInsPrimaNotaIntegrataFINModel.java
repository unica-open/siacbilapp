/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.ordinativo.incasso;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.incasso.CompletaValidaOrdinativoIncassoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento e validazione della prima nota integrata sull'ordinativo di incasso. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 13/10/2015 - gestione GEN/GSA
 */
public class CompletaValidaOrdinativoIncassoInsPrimaNotaIntegrataFINModel extends CompletaValidaOrdinativoIncassoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 3505028878813498267L;

	/** Costruttore vuoto di default */
	public CompletaValidaOrdinativoIncassoInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
