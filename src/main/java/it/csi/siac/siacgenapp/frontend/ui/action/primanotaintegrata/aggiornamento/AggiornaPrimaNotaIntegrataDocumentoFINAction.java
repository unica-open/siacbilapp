/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.aggiornamento;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataDocumentoBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataDocumentoFINModel;

/**
 * Classe di action per la consultazione della prima nota integrata. Modulo GEN
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaPrimaNotaIntegrataDocumentoBaseAction.MODEL_SESSION_NAME_AGGIORNA_PRIMA_NOTA_DOCUMENTO_FIN)
public class AggiornaPrimaNotaIntegrataDocumentoFINAction extends AggiornaPrimaNotaIntegrataDocumentoBaseAction<AggiornaPrimaNotaIntegrataDocumentoFINModel> {


	/** Per la serializzazione*/
	private static final long serialVersionUID = -4340555218532673018L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() {
		return super.execute();
	}

	@Override
	protected BilSessionParameter getParametroSessioneRequest() {
		return BilSessionParameter.REQUEST_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GEN;
	}

	@Override
	protected BilSessionParameter getParametroSessioneLista() {
		return BilSessionParameter.RISULTATI_RICERCA_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GEN;
	}

}
