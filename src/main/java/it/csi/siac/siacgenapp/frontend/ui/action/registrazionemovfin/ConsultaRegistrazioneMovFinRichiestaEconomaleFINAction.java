/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.registrazionemovfin;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.ConsultaRegistrazioneMovFinRichiestaEconomaleBaseAction;
import it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRichiestaEconomaleFINModel;

/**
 * Consultazione base della registrazione movfin legata alla richiesta economale. Modulo GEN
 * 
 * @author Nazha Ahmad
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2015
 * @version 1.1.0 - 08/10/2015 - gestione GEN/GSA
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistrazioneMovFinRichiestaEconomaleFINAction extends ConsultaRegistrazioneMovFinRichiestaEconomaleBaseAction<ConsultaRegistrazioneMovFinRichiestaEconomaleFINModel>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -4527176072845819694L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}

}
