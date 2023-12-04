/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.pagamentofatture;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseRisultatiRicercaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamentofatture.RisultatiRicercaPagamentoFattureCassaEconomaleModel;

/**
 * Classe di action per i risultati della ricerca della richiesta.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPagamentoFattureCassaEconomaleAction extends BaseRisultatiRicercaRichiestaEconomaleAction<RisultatiRicercaPagamentoFattureCassaEconomaleModel> {


	/** Per la serializzazione */
	private static final long serialVersionUID = 3739656416449097627L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Richiamo il metodo execute della superclasse
		return super.execute();
	}
	
}
