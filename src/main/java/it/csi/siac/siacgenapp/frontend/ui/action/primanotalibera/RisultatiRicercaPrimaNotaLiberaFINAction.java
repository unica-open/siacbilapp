/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotalibera;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.RisultatiRicercaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotalibera.RisultatiRicercaPrimaNotaLiberaFINModel;

/**
 * Classe di action per i risultati di ricerca della prima nota libera.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 05/05/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrimaNotaLiberaFINAction extends RisultatiRicercaPrimaNotaLiberaBaseAction <RisultatiRicercaPrimaNotaLiberaFINModel> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -8283316480603327211L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Uso il metodo solo per valorizzare il breadcrumb
		return super.execute();
	}
	
}
