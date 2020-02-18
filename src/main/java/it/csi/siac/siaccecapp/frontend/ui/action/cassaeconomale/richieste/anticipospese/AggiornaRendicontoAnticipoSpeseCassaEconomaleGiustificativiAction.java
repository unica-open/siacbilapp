/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospese;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese.AggiornaRendicontoAnticipoSpeseCassaEconomaleModel;

/**
 * Classe di action per l'aggiornamento del rendiconto dell'anticipo spese, azioni sui giustificativi.
 * 
 * @author Marchino Alessandro - Valentina Triolo
 * @version 1.0.0 - 18/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoSpeseCassaEconomaleAction.MODEL_SESSION_NAME_AGGIORNAMENTO_RENDICONTO)
public class AggiornaRendicontoAnticipoSpeseCassaEconomaleGiustificativiAction
		extends BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleGiustificativiAction<AggiornaRendicontoAnticipoSpeseCassaEconomaleModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2988647065241472680L;
	
	@Override
	protected AzioniConsentite[] retrieveAzioniConsentite() {
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_AGGIORNA_RENDICONTO, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA};
	}


}
