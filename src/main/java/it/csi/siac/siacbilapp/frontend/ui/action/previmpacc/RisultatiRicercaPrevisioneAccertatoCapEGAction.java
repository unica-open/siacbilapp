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
import it.csi.siac.siacbilapp.frontend.ui.model.previmpacc.RisultatiRicercaPrevisioneAccertatoCapEGModel;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrevisioneAccertatoCapEGAction extends RisultatiRicercaPrevisioneImpegnatoAccertatoBaseAction<RisultatiRicercaPrevisioneAccertatoCapEGModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3940154634668639296L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		impostaStartPosition();
		model.setTotaleImporti(sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE_IMPORTI, ImportiCapitoloEG.class));
		return SUCCESS;

	}

	
	
}
