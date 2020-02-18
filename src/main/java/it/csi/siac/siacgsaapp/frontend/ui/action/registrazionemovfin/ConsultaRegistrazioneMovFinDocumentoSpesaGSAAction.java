/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.registrazionemovfin;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.ConsultaRegistrazioneMovFinDocumentoSpesaBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinDocumentoSpesaGSAModel;

/**
 * Consultazione della registrazione MovFin. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistrazioneMovFinDocumentoSpesaGSAAction extends ConsultaRegistrazioneMovFinDocumentoSpesaBaseAction<ConsultaRegistrazioneMovFinDocumentoSpesaGSAModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5705365055065458204L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}
	
}
