/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospesepermissione;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione.InserisciRendicontoAnticipoSpesePerMissioneCassaEconomaleModel;

/**
 * Classe di action per l'inserimento del rendiconto per l'anticipo spese per missione, azioni sui giustificativi.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleAction.MODEL_SESSION_NAME_INSERIMENTO_RENDICONTO)
public class InserisciRendicontoAnticipoSpesePerMissioneCassaEconomaleGiustificativiAction
		extends BaseInserisciAggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleGiustificativiAction<InserisciRendicontoAnticipoSpesePerMissioneCassaEconomaleModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2330956539667149071L;
	
	@Override
	protected AzioniConsentite[] retrieveAzioniConsentite() {
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_INSERISCI, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_ABILITA};
	}
}
