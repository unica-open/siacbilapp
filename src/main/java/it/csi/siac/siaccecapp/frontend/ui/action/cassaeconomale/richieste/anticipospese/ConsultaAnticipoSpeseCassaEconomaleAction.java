/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospese;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese.ConsultaAnticipoSpeseCassaEconomaleModel;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * Classe di action per la consultazione della richiesta.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaAnticipoSpeseCassaEconomaleAction extends BaseConsultaRichiestaEconomaleAction<ConsultaAnticipoSpeseCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5919233579274846264L;
	
	@Override
	protected void impostaRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		super.impostaRichiestaEconomale(richiestaEconomale);
		model.setListaGiustificativo(richiestaEconomale.getGiustificativi());
	}

	@Override
	protected AzioniConsentite[] retrieveAzioniConsentite() {
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_RICERCA, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA};
	}
	
}
