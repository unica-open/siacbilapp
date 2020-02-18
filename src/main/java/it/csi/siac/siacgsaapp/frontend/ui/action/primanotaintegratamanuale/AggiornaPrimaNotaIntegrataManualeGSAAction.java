/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegratamanuale;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegratamanuale.AggiornaPrimaNotaIntegrataManualeBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale.AggiornaPrimaNotaIntegrataManualeGSAModel;
/**
 * Classe di action per l'aggiornamentO della prima nota integrata manuale
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaPrimaNotaLiberaBaseAction.MODEL_SESSION_NAME_INTEGRATA_MANUALE_AGGIORNAMENTO_GSA)
public class AggiornaPrimaNotaIntegrataManualeGSAAction extends AggiornaPrimaNotaIntegrataManualeBaseAction<AggiornaPrimaNotaIntegrataManualeGSAModel> {
	

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -4644850459220159949L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Uso il metodo solo per forzare il breadcrumb
		return super.execute();
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterListeCausali() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_MANUALE_GSA;
	}

}
