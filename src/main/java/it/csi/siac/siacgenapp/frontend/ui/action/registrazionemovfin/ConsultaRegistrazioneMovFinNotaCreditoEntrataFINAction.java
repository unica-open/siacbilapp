/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.registrazionemovfin;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.ConsultaRegistrazioneMovFinNotaCreditoEntrataBaseAction;
import it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinNotaCreditoEntrataFINModel;

/**
 * Consultazione della registrazione MovFin. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/01/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistrazioneMovFinNotaCreditoEntrataFINAction extends ConsultaRegistrazioneMovFinNotaCreditoEntrataBaseAction<ConsultaRegistrazioneMovFinNotaCreditoEntrataFINModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2907779657655792689L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}
	
}
