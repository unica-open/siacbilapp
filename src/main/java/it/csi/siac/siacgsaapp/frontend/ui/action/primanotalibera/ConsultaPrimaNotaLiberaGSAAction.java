/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotalibera;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.ConsultaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotalibera.ConsultaPrimaNotaLiberaGSAModel;

/**
 * Classe di action per la consultazione della prima nota libera GSA
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 15/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)

public class ConsultaPrimaNotaLiberaGSAAction extends ConsultaPrimaNotaLiberaBaseAction <ConsultaPrimaNotaLiberaGSAModel>{

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
