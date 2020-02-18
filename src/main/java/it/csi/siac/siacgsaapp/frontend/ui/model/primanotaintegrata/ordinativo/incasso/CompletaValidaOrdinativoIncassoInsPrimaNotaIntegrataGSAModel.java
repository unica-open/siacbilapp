/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.ordinativo.incasso;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.incasso.CompletaValidaOrdinativoIncassoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento e validazione della prima nota integrata sull'ordinativo di incasso. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/10/2015
 */
public class CompletaValidaOrdinativoIncassoInsPrimaNotaIntegrataGSAModel extends CompletaValidaOrdinativoIncassoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 4989602507808122050L;

	/** Costruttore vuoto di default */
	public CompletaValidaOrdinativoIncassoInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
