/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.extra;

import java.util.List;

import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Tipologia di richiesta per la cassa economale: azioni extra delle liquidazioni.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/12/2014
 *
 */
public class TipologiaRichiestaCassaEconomaleLiquidazioni extends TipologiaRichiestaCassaEconomaleExtra {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4211263036550725407L;

	/**
	 * Costruttore a partire dalle azioni consentite all'utente.
	 * 
	 * @param azioniConsentite le azioni consentite
	 */
	public TipologiaRichiestaCassaEconomaleLiquidazioni(List<AzioneConsentita> azioniConsentite) {
		super();
		
		populateTipoRichiestaEconomale("Liquidazioni");
		addAbilitazioneRichiestaCassaEconomale("gestisci", "cassaEconomaleLiquidazioniGestione.do", azioniConsentite, AzioneConsentitaEnum.CASSA_ECONOMALE_GESTIONE_LIQUIDAZIONI);
	}
	
}
