/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.ordinativo.incasso;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.incasso.CompletaOrdinativoIncassoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Completamento della prima nota integrata sull'ordinativo di incasso. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/10/2015
 */
public class CompletaOrdinativoIncassoInsPrimaNotaIntegrataGSAModel extends CompletaOrdinativoIncassoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -3605025019568932074L;

	/** Costruttore vuoto di default */
	public CompletaOrdinativoIncassoInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
