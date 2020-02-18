/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.causali;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.RisultatiRicercaCausaleEPBaseAction;
import it.csi.siac.siacgenapp.frontend.ui.model.causali.RisultatiRicercaCausaleEPFINModel;

/**
 * Classe di action per i risultati di ricerca della causale EP.
 * 
 * @author Simona Paggio
 * @version 1.0.0 - 06/05/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCausaleEPFINAction extends RisultatiRicercaCausaleEPBaseAction<RisultatiRicercaCausaleEPFINModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9102546185859078208L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Aggiungo solo il breadcrumb
		return super.execute();
	}
	
}
