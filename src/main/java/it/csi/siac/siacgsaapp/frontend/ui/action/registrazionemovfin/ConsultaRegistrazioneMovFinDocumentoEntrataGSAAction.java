/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.registrazionemovfin;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.ConsultaRegistrazioneMovFinDocumentoEntrataBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinDocumentoEntrataGSAModel;

/**
 * Consultazione della registrazione del movimento finanziario per il documento di entrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015 - gestione GEN/GSA
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistrazioneMovFinDocumentoEntrataGSAAction extends ConsultaRegistrazioneMovFinDocumentoEntrataBaseAction<ConsultaRegistrazioneMovFinDocumentoEntrataGSAModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7109738200899550806L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}
	
}
