/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.registrazionemovfin;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.ConsultaRegistrazioneMovFinRateoRiscontoBaseAction;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RateoRisconto;
import it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRateoRiscontoGSAModel;

/**
 * Consultazione base della registrazione movfin legata al rateo. Modulo GSA
 * 
 * @author Valentina
 * @version 1.0.0 - 25/07/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistrazioneMovFinRateoGSAAction extends ConsultaRegistrazioneMovFinRateoRiscontoBaseAction<ConsultaRegistrazioneMovFinRateoRiscontoGSAModel>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -4527176072845819694L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}
	
	@Override
	protected RateoRisconto retrieveRateoORisconto(PrimaNota primaNota) {
		return primaNota.getRateo();
	}

}
