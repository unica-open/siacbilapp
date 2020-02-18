/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.registrazionemovfin;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleGSAModel;

/**
 * Consultazione base della registrazione movfin legata alla richiesta economale(Rendiconto). Modulo GSA
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 12/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleGSAAction extends ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleBaseAction<ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleGSAModel>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -8446064308834424063L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}

}
