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
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotalibera.InserisciPrimaNotaLiberaGSAModel;
/**
 * Classe di action per l'inserimento della prima notalibera, sezione dei movimenti dettaglio
 * 
 * 
 * @author Elisa Chiari
 * @version 1.0.1 - 15/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaPrimaNotaLiberaBaseAction.MODEL_SESSION_NAME_INSERIMENTO_GSA)
public class InserisciContoPrimaNotaLiberaGSAAction extends BaseInserisciAggiornaContoPrimaNotaLiberaBaseAction<InserisciPrimaNotaLiberaGSAModel>{

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -2595486159216056610L;

	@Override
	protected BilSessionParameter getBilSessionParameterListeCausali() {
		return BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_GSA;
	}
	
	@Override
	protected boolean getFaseDiBilancioNonCompatibile(FaseBilancio faseBilancio) {
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		return
		FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
		FaseBilancio.PREVISIONE.equals(faseBilancio);
	}
}
