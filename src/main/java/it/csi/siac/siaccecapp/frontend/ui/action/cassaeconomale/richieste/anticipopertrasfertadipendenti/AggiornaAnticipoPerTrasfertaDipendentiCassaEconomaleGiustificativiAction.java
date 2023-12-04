/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipopertrasfertadipendenti;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipopertrasfertadipendenti.AggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleModel;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di action per l'aggiornamento dell'anticipo spese per trasferta, azioni sui giustificativi.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 16/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleAction.MODEL_SESSION_NAME_AGGIORNAMENTO)
public class AggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleGiustificativiAction
		extends BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleGiustificativiAction<AggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -1428762323298154433L;
	
	@Override
	protected AzioneConsentitaEnum[] retrieveAzioniConsentite() {
		return new AzioneConsentitaEnum[] {AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_PER_TRASFERTA_DIPENDENTI_AGGIORNA, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_PER_TRASFERTA_DIPENDENTI_ABILITA};
	}
}
