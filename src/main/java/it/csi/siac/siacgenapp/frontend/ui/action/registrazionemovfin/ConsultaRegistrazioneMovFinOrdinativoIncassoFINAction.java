/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.registrazionemovfin;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.ConsultaRegistrazioneMovFinOrdinativoIncassoBaseAction;
import it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinOrdinativoIncassoFINModel;

/**
 * Consultazione dell'Ordinativo di Incasso. Modulo GEN
 * 
 * @author Valentina Triolo
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/05/2015
 * @version 1.1.0 - 07/10/2015 - gestione GEN/GSA
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistrazioneMovFinOrdinativoIncassoFINAction extends ConsultaRegistrazioneMovFinOrdinativoIncassoBaseAction<ConsultaRegistrazioneMovFinOrdinativoIncassoFINModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8054475988146715642L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}
	
}
