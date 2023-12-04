/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospese;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseRisultatiRicercaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese.RisultatiRicercaAnticipoSpeseCassaEconomaleModel;

/**
 * Classe di action per i risultati della ricerca della richiesta.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaAnticipoSpeseCassaEconomaleAction extends BaseRisultatiRicercaRichiestaEconomaleAction<RisultatiRicercaAnticipoSpeseCassaEconomaleModel> {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = 8373416956895147617L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Richiamo il metodo execute della superclasse
		return super.execute();
	}
	
}
