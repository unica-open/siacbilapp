/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


/**
 * Classe di Action per la gestione della consultazione della Variazione Importi.
 * 
 * @author Elisa Chiari
 * @version 1.1.0 28/02/2017
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaVariazioneImportiUEBAction extends ConsultaVariazioneImportiBaseAction {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 6473748987704952488L;

	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		log.debugEnd(methodName, "");
	}
	

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}
	
}
