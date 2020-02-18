/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.subimpegno;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.subimpegno.CompletaSubImpegnoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento della prima nota integrata sul subimpegno. Modulo FIN
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 */
public class CompletaSubImpegnoInsPrimaNotaIntegrataFINModel extends CompletaSubImpegnoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -1620652192388428577L;

	/** Costruttore vuoto di default */
	public CompletaSubImpegnoInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
