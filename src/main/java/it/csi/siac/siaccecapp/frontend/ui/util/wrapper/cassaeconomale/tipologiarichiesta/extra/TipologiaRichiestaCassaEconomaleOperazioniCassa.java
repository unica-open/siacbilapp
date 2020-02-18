/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.extra;

import java.util.List;

import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Tipologia di richiesta per la cassa economale: azioni extra delle operazioni di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/12/2014
 *
 */
public class TipologiaRichiestaCassaEconomaleOperazioniCassa extends TipologiaRichiestaCassaEconomaleExtra {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8442333069030465218L;

	/**
	 * Costruttore a partire dalle azioni consentite all'utente.
	 * 
	 * @param azioniConsentite le azioni consentite
	 */
	public TipologiaRichiestaCassaEconomaleOperazioniCassa(List<AzioneConsentita> azioniConsentite) {
		super();
		
		populateTipoRichiestaEconomale("Operazioni cassa");
		addAbilitazioneRichiestaCassaEconomale("gestisci", "cassaEconomaleOperazioniCassaGestione.do", azioniConsentite,
				AzioniConsentite.CASSA_ECONOMALE_GESTIONE_OPERAZIONI_CASSA);
	}

}
