/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.registrazionemovfin;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaBaseAction;
import it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaFINModel;

/**
 * Consultazione per la modifica del movimento di gestione di spesa. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 17/11/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaFINAction extends ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaBaseAction<ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaFINModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2511080694015906004L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}
	
}
