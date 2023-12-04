/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.subaccertamento;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.subaccertamento.GestioneSubAccertamentoInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPFINSelector;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.subaccertamento.CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, subaccertamento. Modulo FIN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneSubAccertamentoInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_SUBACCERTAMENTO_FIN)
public class CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINAction extends GestioneSubAccertamentoInsPrimaNotaIntegrataBaseAction<CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6431532668393724639L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return super.execute();
	}
	
	@Override
	protected BilSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}
	
	@Override
	protected CausaleEPSelector istanziaSelettoreCausale() {
		return new CausaleEPFINSelector(ottieniElementoPianoDeiContiDaMovimento(), ottieniSoggettoDaMovimento());
	}
}

