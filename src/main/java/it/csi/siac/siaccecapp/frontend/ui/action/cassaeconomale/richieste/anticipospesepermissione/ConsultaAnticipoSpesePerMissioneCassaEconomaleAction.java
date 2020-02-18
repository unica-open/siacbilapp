/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospesepermissione;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione.ConsultaAnticipoSpesePerMissioneCassaEconomaleModel;

/**
 * Classe di action per la consultazione dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/01/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaAnticipoSpesePerMissioneCassaEconomaleAction extends BaseConsultaRichiestaEconomaleAction<ConsultaAnticipoSpesePerMissioneCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1197723571321922628L;

	@Override
	protected AzioniConsentite[] retrieveAzioniConsentite() {
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_RICERCA, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_ABILITA};
	}
}
