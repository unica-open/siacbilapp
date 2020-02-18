/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.registrazionemovfin;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.ConsultaRegistrazioneMovFinNotaCreditoSpesaBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinNotaCreditoSpesaGSAModel;

/**
 * Consultazione della registrazione MovFin. Modulo GEN
 * 
 * @author Marchino Alessandro
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistrazioneMovFinNotaCreditoSpesaGSAAction extends ConsultaRegistrazioneMovFinNotaCreditoSpesaBaseAction<ConsultaRegistrazioneMovFinNotaCreditoSpesaGSAModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7672822543683611381L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}
	
}
