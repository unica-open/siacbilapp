/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotalibera;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaContoPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotalibera.AggiornaPrimaNotaLiberaFINModel;

/**
 * Classe di action per l'aggiornamento della prima notalibera, sezione dei movimenti dettaglio
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 06/05/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaPrimaNotaLiberaBaseAction.MODEL_SESSION_NAME_AGGIORNAMENTO_FIN)
public class AggiornaContoPrimaNotaLiberaFINAction extends BaseInserisciAggiornaContoPrimaNotaLiberaBaseAction <AggiornaPrimaNotaLiberaFINModel> {

	/** per serializzazione **/
	private static final long serialVersionUID = 27235591759371702L;

	@Override
	protected BilSessionParameter getBilSessionParameterListeCausali() {
		return BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_GEN;
	}
}
