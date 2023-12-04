/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.previmpacc;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.previmpacc.RisultatiRicercaPrevisioneImpegnatoCapUGModel;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;

/**
 * The Class RisultatiRicercaPrevisioneImpegnatoCapUGAction.
 *
 * @author elisa
 * @version 1.0.0 15 ott 2021
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrevisioneImpegnatoCapUGAction extends RisultatiRicercaPrevisioneImpegnatoAccertatoBaseAction<RisultatiRicercaPrevisioneImpegnatoCapUGModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4621243758939208451L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		impostaStartPosition();
		model.setTotaleImporti(sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE_IMPORTI, ImportiCapitoloUG.class));
		return SUCCESS;

	}
	
}
