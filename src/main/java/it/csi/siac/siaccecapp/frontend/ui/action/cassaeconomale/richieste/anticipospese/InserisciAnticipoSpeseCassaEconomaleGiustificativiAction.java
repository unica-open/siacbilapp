/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospese;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese.InserisciAnticipoSpeseCassaEconomaleModel;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di action per l'inserimento della richiesta, azioni sui giustificativi.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoSpeseCassaEconomaleAction.MODEL_SESSION_NAME_INSERIMENTO)
public class InserisciAnticipoSpeseCassaEconomaleGiustificativiAction extends BaseInserisciAggiornaAnticipoSpeseCassaEconomaleAction<InserisciAnticipoSpeseCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5794877047411814537L;

	@Override
	protected AzioneConsentitaEnum[] retrieveAzioniConsentite() {
		return new AzioneConsentitaEnum[] {AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_INSERISCI, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA};
	}

}
