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
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese.InserisciRendicontoAnticipoSpeseCassaEconomaleModel;

/**
 * Classe di action per l'inserimento del rendiconto per l'anticipo spese, azioni sui giustificativi.
 * 
 * @author Marchino Alessandro - Valentina Triolo
 * @version 1.0.0 - 18/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoSpeseCassaEconomaleAction.MODEL_SESSION_NAME_INSERIMENTO_RENDICONTO)
public class InserisciRendicontoAnticipoSpeseCassaEconomaleGiustificativiAction
		extends BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleGiustificativiAction<InserisciRendicontoAnticipoSpeseCassaEconomaleModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2330956539667149071L;
	
	@Override
	protected AzioniConsentite[] retrieveAzioniConsentite() {
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_INSERISCI_RENDICONTO, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA};
	}

}
