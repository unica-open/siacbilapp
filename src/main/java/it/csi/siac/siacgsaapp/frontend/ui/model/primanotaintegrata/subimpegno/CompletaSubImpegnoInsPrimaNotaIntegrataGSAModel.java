/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.subimpegno;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.subimpegno.CompletaSubImpegnoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento della prima nota integrata sul subimpegno. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 */
public class CompletaSubImpegnoInsPrimaNotaIntegrataGSAModel extends CompletaSubImpegnoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 6478866981602718578L;

	/** Costruttore vuoto di default */
	public CompletaSubImpegnoInsPrimaNotaIntegrataGSAModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
