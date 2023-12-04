/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.registrazionemovfin;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinGSAModel;

/**
 * Action per i risultati di ricerca della registrazione. Ambito GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRegistrazioneMovFinGSAAction extends RisultatiRicercaRegistrazioneMovFinBaseAction<RisultatiRicercaRegistrazioneMovFinGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 987666725788526331L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Aggiungo solo il breadcrumb
		return super.execute();
	}

}
