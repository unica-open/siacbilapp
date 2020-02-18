/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.registroa;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccespapp.frontend.ui.model.registroa.ConsultaRegistroACespiteModel;

/**
 * Action per la consultazione del registro A(prime note verso inventario contabile)
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/10/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRegistroACespiteAction extends BaseConsultaAggiornaRegistroACespiteAction<ConsultaRegistroACespiteModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 668980240603794206L;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		boolean movimentiCaricatiCorrettamente = caricaMovimentiDettaglio();
		if(!movimentiCaricatiCorrettamente) {
			return INPUT;
		}
		return SUCCESS;
	}

	@Override
	public String ottieniTabellaCespiti() {
		return SUCCESS;
	}
}
