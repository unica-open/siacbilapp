/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.registrazionemovfin;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.ConsultaRegistrazioneMovFinSubImpegnoBaseAction;
import it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinSubImpegnoFINModel;

/**
 * Consultazione per il subimpegno. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistrazioneMovFinSubImpegnoFINAction extends ConsultaRegistrazioneMovFinSubImpegnoBaseAction<ConsultaRegistrazioneMovFinSubImpegnoFINModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3977900365086298557L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}
	
}
