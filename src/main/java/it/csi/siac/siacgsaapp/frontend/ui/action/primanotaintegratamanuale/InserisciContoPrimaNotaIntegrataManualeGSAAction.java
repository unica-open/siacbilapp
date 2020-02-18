/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegratamanuale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaContoPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale.InserisciPrimaNotaIntegrataManualeGSAModel;
/**
 * Classe di action per l'inserimento della prima nota integrata manuale, sezione dei movimenti dettaglio
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaPrimaNotaLiberaBaseAction.MODEL_SESSION_NAME_INTEGRATA_MANUALE_INSERIMENTO_GSA)
public class InserisciContoPrimaNotaIntegrataManualeGSAAction extends BaseInserisciAggiornaContoPrimaNotaLiberaBaseAction<InserisciPrimaNotaIntegrataManualeGSAModel>{

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -2595486159216056610L;

	@Override
	protected BilSessionParameter getBilSessionParameterListeCausali() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_MANUALE_GSA;
	}
}
