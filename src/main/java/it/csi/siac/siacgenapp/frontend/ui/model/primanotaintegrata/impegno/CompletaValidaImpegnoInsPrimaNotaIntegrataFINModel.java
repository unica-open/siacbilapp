/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.impegno;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.impegno.CompletaValidaImpegnoInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;


/**
 * Completamento e validazione della prima nota integrata sull'impegno. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 13/10/2015 - gestione GEN/GSA
 */
public class CompletaValidaImpegnoInsPrimaNotaIntegrataFINModel extends CompletaValidaImpegnoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione **/
	private static final long serialVersionUID = 8083617231521938382L;

	/** Costruttore vuoto di default */
	public CompletaValidaImpegnoInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
