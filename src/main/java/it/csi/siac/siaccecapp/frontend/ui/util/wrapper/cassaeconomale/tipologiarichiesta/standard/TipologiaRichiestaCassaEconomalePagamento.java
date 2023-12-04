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
 * @author Simona Paggio
 * @version 1.0.0 - 01/02/2016
 *
 */
public class TipologiaRichiestaCassaEconomalePagamento extends TipologiaRichiestaCassaEconomale {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4211263036550725407L;

	/**
	 * Costruttore a partire dalle azioni consentite all'utente.
	 * 
	 * @param azioniConsentite le azioni consentite
	 */
	public TipologiaRichiestaCassaEconomalePagamento(List<AzioneConsentita> azioniConsentite) {
		super();
		
		TipoRichiestaEconomale tipoRichiestaEconomale = new TipoRichiestaEconomale("", "Pagamento");
		setTipoRichiestaEconomale(tipoRichiestaEconomale);
		
		addAbilitazioneRichiestaCassaEconomale("inserisci", "inserisciPagamentoCassaEconomale.do", azioniConsentite,
				AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_INSERISCI, AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_ABILITA);
		addAbilitazioneRichiestaCassaEconomale("ricerca", "ricercaPagamentoCassaEconomale.do", azioniConsentite,
				AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_RICERCA, AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_ABILITA);
	}
	
}
