/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.standard;

import java.util.List;

import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.TipologiaRichiestaCassaEconomale;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Tipologia di richiesta per la cassa economale: azioni extra delle liquidazioni.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/12/2014
 *
 */
public class TipologiaRichiestaCassaEconomaleRimborsoSpese extends TipologiaRichiestaCassaEconomale {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4211263036550725407L;

	/**
	 * Costruttore a partire dalle azioni consentite all'utente.
	 * 
	 * @param azioniConsentite le azioni consentite
	 */
	public TipologiaRichiestaCassaEconomaleRimborsoSpese(List<AzioneConsentita> azioniConsentite) {
		super();
		
		TipoRichiestaEconomale tipoRichiestaEconomale = new TipoRichiestaEconomale("", "Rimborso spese");
		setTipoRichiestaEconomale(tipoRichiestaEconomale);
		
		addAbilitazioneRichiestaCassaEconomale("inserisci", "inserisciRimborsoSpeseCassaEconomale.do", azioniConsentite,
				AzioneConsentitaEnum.CASSA_ECONOMALE_RIMBORSO_SPESE_INSERISCI, AzioneConsentitaEnum.CASSA_ECONOMALE_RIMBORSO_SPESE_ABILITA);
		addAbilitazioneRichiestaCassaEconomale("ricerca", "ricercaRimborsoSpeseCassaEconomale.do", azioniConsentite,
				AzioneConsentitaEnum.CASSA_ECONOMALE_RIMBORSO_SPESE_RICERCA, AzioneConsentitaEnum.CASSA_ECONOMALE_RIMBORSO_SPESE_ABILITA);
	}
	
}
