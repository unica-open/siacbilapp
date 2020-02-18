/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotalibera;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaContoPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotalibera.AggiornaPrimaNotaLiberaGSAModel;

/**
 * Aggiornamento del conto per la prima nota libera. Modulo GEN
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 15/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaPrimaNotaLiberaBaseAction.MODEL_SESSION_NAME_AGGIORNAMENTO_GSA)
public class AggiornaContoPrimaNotaLiberaGSAAction extends BaseInserisciAggiornaContoPrimaNotaLiberaBaseAction <AggiornaPrimaNotaLiberaGSAModel>{

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 6329271644679623072L;

	@Override
	protected BilSessionParameter getBilSessionParameterListeCausali() {
		return BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_GSA;
	}
}
