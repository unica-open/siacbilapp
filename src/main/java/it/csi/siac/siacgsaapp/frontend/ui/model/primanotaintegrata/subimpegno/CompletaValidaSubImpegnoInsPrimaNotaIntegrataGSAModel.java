/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.subimpegno;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.subimpegno.CompletaValidaSubImpegnoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento e validazione della prima nota integrata sul subimpegno. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 */
public class CompletaValidaSubImpegnoInsPrimaNotaIntegrataGSAModel extends CompletaValidaSubImpegnoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -1774119568844676014L;

	/** Costruttore vuoto di default */
	public CompletaValidaSubImpegnoInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
