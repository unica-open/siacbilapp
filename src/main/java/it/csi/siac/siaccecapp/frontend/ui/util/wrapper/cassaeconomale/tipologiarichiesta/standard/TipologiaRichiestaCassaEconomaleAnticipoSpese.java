/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.standard;

import java.util.List;

import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
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
public class TipologiaRichiestaCassaEconomaleAnticipoSpese extends TipologiaRichiestaCassaEconomale {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4211263036550725407L;

	/**
	 * Costruttore a partire dalle azioni consentite all'utente.
	 * 
	 * @param azioniConsentite le azioni consentite
	 */
	public TipologiaRichiestaCassaEconomaleAnticipoSpese(List<AzioneConsentita> azioniConsentite) {
		super();
		
		TipoRichiestaEconomale tipoRichiestaEconomale = new TipoRichiestaEconomale("", "Anticipo spese");
		setTipoRichiestaEconomale(tipoRichiestaEconomale);
		
		addAbilitazioneRichiestaCassaEconomale("inserisci", "inserisciAnticipoSpeseCassaEconomale.do", azioniConsentite,
				AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_INSERISCI, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA);
		addAbilitazioneRichiestaCassaEconomale("ricerca", "ricercaAnticipoSpeseCassaEconomale.do", azioniConsentite,
				AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_RICERCA, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA);
	}
	
}
