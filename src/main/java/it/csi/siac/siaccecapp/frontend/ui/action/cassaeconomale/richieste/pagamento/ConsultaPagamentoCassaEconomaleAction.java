/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.pagamento;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamento.ConsultaPagamentoCassaEconomaleModel;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di action per la consultazione del pagamento.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 01/02/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaPagamentoCassaEconomaleAction extends BaseConsultaRichiestaEconomaleAction<ConsultaPagamentoCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7388422337725204508L;

	@Override
	protected AzioneConsentitaEnum[] retrieveAzioniConsentite() {
		return new AzioneConsentitaEnum[] {AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_RICERCA, AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_ABILITA};
	}
}
