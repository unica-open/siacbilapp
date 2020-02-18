/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;

/**
 * Classe di model per l'inserimento del PreDocumento di Entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/04/2014
 *
 */
public class InserisciPreDocumentoEntrataModel extends BaseInserimentoPreDocumentoEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3734444795369284930L;

	/** Costruttore vuoto di default */
	public InserisciPreDocumentoEntrataModel() {
		setTitolo("Inserimento Predisposizione di Incasso");
		setNomeAzioneDecentrata(BilConstants.INSERISCI_PREDOCUMENTO_ENTRATA_DECENTRATO.getConstant());
	}

}
