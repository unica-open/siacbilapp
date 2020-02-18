/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegratamanuale;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegratamanuale.ConsultaPrimaNotaIntegrataManualeBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale.ConsultaPrimaNotaIntegrataManualeGSAModel;

/**
 * Classe di action per la consultazione della prima nota integrata manuale GSA
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaPrimaNotaIntegrataManualeGSAAction extends ConsultaPrimaNotaIntegrataManualeBaseAction <ConsultaPrimaNotaIntegrataManualeGSAModel>{

	/**
	 * Per la serializzazione 
	 */
	private static final long serialVersionUID = 1051645555892667982L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Uso il metodo solo per forzare il breadcrumb
		return super.execute();
	}

}
