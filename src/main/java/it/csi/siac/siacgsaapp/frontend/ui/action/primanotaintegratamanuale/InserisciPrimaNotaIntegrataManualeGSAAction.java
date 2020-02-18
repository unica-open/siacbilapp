/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegratamanuale;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegratamanuale.InserisciPrimaNotaIntegrataManualeBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale.InserisciPrimaNotaIntegrataManualeGSAModel;

/**
 * Classe di action per l'inserimento e l'aggiornamento della prima nota integrata manuale
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaPrimaNotaLiberaBaseAction.MODEL_SESSION_NAME_INTEGRATA_MANUALE_INSERIMENTO_GSA)
public class InserisciPrimaNotaIntegrataManualeGSAAction extends InserisciPrimaNotaIntegrataManualeBaseAction<InserisciPrimaNotaIntegrataManualeGSAModel>{

	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 1367341637673004639L;

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
	
	@Override
	protected boolean getFaseDiBilancioNonCompatibile(FaseBilancio faseBilancio) {
	    return 
		FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
		FaseBilancio.PREVISIONE.equals(faseBilancio);
	}
	
	
}
