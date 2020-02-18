/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.impegno;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.impegno.CompletaImpegnoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento della prima nota integrata sull'impegno. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/10/2015
 */
public class CompletaImpegnoInsPrimaNotaIntegrataGSAModel extends CompletaImpegnoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione **/
	private static final long serialVersionUID = -733634591907800316L;

	/** Costruttore vuoto di default */
	public CompletaImpegnoInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
