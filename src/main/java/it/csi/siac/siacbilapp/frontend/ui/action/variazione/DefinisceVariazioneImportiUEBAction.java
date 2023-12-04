/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;


/**
 * Classe di Action per la gestione della consultazione della Variazione Importi UEB.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 06/07/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class DefinisceVariazioneImportiUEBAction extends DefinisceVariazioneImportiBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1724316301273240379L;
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}
	
}
