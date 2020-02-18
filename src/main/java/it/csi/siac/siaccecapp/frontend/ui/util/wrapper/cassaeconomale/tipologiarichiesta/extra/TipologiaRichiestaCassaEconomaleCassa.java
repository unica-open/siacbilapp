/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.extra;

import java.util.List;

import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Tipologia di richiesta per la cassa economale: azioni extra della cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/12/2014
 *
 */
public class TipologiaRichiestaCassaEconomaleCassa extends TipologiaRichiestaCassaEconomaleExtra {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 505080631857638811L;

	/**
	 * Costruttore a partire dalle azioni consentite all'utente.
	 * 
	 * @param azioniConsentite le azioni consentite
	 */
	public TipologiaRichiestaCassaEconomaleCassa(List<AzioneConsentita> azioniConsentite) {
		super();
		
		populateTipoRichiestaEconomale("Cassa");
		addAbilitazioneRichiestaCassaEconomale("gestisci", "cassaEconomaleCassaGestione.do", azioniConsentite, AzioniConsentite.CASSA_ECONOMALE_GESTIONE_CASSA_ECONOMALE);
		// Movimenti non piu' disponibili per jira 1985
	}
	
}
