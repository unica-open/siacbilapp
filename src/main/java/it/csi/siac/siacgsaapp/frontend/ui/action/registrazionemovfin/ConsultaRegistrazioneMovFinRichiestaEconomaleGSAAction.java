/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.registrazionemovfin;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.ConsultaRegistrazioneMovFinRichiestaEconomaleBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRichiestaEconomaleGSAModel;

/**
 * Consultazione base della registrazione movfin legata alla richiesta economale. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistrazioneMovFinRichiestaEconomaleGSAAction extends ConsultaRegistrazioneMovFinRichiestaEconomaleBaseAction<ConsultaRegistrazioneMovFinRichiestaEconomaleGSAModel>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -8446064308834424063L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}

}
