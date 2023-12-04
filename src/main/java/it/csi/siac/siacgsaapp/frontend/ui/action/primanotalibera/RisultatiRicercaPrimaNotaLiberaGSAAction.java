/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotalibera;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.RisultatiRicercaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotalibera.RisultatiRicercaPrimaNotaLiberaGSAModel;
/**
 * Classe di action per i risultati di ricerca della prima nota libera.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrimaNotaLiberaGSAAction extends RisultatiRicercaPrimaNotaLiberaBaseAction <RisultatiRicercaPrimaNotaLiberaGSAModel> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -1419927514061041550L;


	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Uso il metodo solo per valorizzare il breadcrumb
		return super.execute();
	}


}
