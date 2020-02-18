/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.causali;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.ConsultaCausaleEPBaseAction;
import it.csi.siac.siacgenapp.frontend.ui.model.causali.ConsultaCausaleEPFINModel;

/**
 * Classe di action per la consultazione della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/04/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaCausaleEPFINAction extends ConsultaCausaleEPBaseAction<ConsultaCausaleEPFINModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2144951325800069394L;
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Aggiungo solo il breadcrumb
		return super.execute();
	}
}
