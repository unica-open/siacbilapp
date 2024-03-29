/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.rimborsospese;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.rimborsospese.ConsultaRimborsoSpeseCassaEconomaleModel;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di action per la consultazione del rimborso spese.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRimborsoSpeseCassaEconomaleAction extends BaseConsultaRichiestaEconomaleAction<ConsultaRimborsoSpeseCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5885390402780986220L;

	@Override
	protected AzioneConsentitaEnum[] retrieveAzioniConsentite() {
		return new AzioneConsentitaEnum[] {AzioneConsentitaEnum.CASSA_ECONOMALE_RIMBORSO_SPESE_RICERCA, AzioneConsentitaEnum.CASSA_ECONOMALE_RIMBORSO_SPESE_ABILITA};
	}
}
