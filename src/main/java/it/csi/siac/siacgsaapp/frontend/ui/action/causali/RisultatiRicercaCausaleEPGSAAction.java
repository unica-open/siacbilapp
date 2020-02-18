/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.causali;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.RisultatiRicercaCausaleEPBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.causali.RisultatiRicercaCausaleEPGSAModel;

/**
 * Classe di action per i risultati di ricerca della causale EP.
 * 
 * @author Simona Paggio
 * @version 1.0.0 - 06/05/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCausaleEPGSAAction extends RisultatiRicercaCausaleEPBaseAction<RisultatiRicercaCausaleEPGSAModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2808587914516851358L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Aggiungo solo il breadcrumb
		return super.execute();
	}
	
}
