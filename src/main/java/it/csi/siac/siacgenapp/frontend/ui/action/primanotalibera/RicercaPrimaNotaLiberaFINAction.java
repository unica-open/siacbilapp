/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotalibera;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.RicercaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotalibera.RicercaPrimaNotaLiberaFINModel;

/**
 * Classe di action per la ricerca della prima nota libera
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 05/05/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 08/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaPrimaNotaLiberaFINAction extends RicercaPrimaNotaLiberaBaseAction<RicercaPrimaNotaLiberaFINModel> {
	

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 8839454792052684074L;


	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Uso il metodo solo per forzare il breadcrumb
		return super.execute();
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRequest() {
		return BilSessionParameter.REQUEST_RICERCA_PRIMANOTALIBERA_GEN;
	}
	
	
	@Override
	protected BilSessionParameter getBilSessionParameterRisultati() {
		return BilSessionParameter.RISULTATI_RICERCA_PRIMANOTALIBERA_GEN;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterListeCausali() {
		return BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_GEN;
	}

}
