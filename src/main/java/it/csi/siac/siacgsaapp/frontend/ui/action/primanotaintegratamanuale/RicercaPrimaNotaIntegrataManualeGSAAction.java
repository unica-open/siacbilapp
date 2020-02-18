/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegratamanuale;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegratamanuale.RicercaPrimaNotaIntegrataManualeBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale.RicercaPrimaNotaIntegrataManualeGSAModel;

/**
 * Classe di action per la ricerca della prima nota integrata manuale
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaPrimaNotaIntegrataManualeGSAAction extends RicercaPrimaNotaIntegrataManualeBaseAction<RicercaPrimaNotaIntegrataManualeGSAModel>{
	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -5660349524466498446L;


	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Uso il metodo solo per forzare il breadcrumb
		return super.execute();
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRequest() {
		return BilSessionParameter.REQUEST_RICERCA_PRIMANOTAINTEGRATAMANUALE_GSA;
	}
	
	
	@Override
	protected BilSessionParameter getBilSessionParameterRisultati() {
		return BilSessionParameter.RISULTATI_RICERCA_PRIMANOTAINTEGRATAMANUALE_GSA;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterListeCausali() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_MANUALE_GSA;
	}

	@Override
	protected boolean checkUlterioriCampi() {
		return checkCampoValorizzato(model.getPrimaNotaLibera().getClassificatoreGSA(), "classificatore");
	}

}
