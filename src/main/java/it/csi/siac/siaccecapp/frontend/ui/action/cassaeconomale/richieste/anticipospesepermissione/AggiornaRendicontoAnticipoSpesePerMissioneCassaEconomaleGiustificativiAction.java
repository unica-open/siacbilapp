/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospesepermissione;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione.AggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di action per l'aggiornamento del rendiconto dell'anticipo spese per missione, azioni sui giustificativi.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleAction.MODEL_SESSION_NAME_AGGIORNAMENTO_RENDICONTO)
public class AggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleGiustificativiAction
		extends BaseInserisciAggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleGiustificativiAction<AggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2988647065241472680L;
	
	@Override
	protected AzioneConsentitaEnum[] retrieveAzioniConsentite() {
		return new AzioneConsentitaEnum[] {AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_AGGIORNA_RENDICONTO, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_ABILITA};
	}
}
