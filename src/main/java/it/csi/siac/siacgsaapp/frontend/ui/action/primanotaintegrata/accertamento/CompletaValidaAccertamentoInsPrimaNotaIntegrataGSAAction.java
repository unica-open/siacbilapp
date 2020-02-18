/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata.accertamento;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.accertamento.GestioneAccertamentoInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPGSASelector;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.accertamento.CompletaValidaAccertamentoInsPrimaNotaIntegrataGSAModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, accertamento. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneAccertamentoInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_ACCERTAMENTO_GSA)
public class CompletaValidaAccertamentoInsPrimaNotaIntegrataGSAAction extends GestioneAccertamentoInsPrimaNotaIntegrataBaseAction<CompletaValidaAccertamentoInsPrimaNotaIntegrataGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8485872692158201139L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return super.execute();
	}
	
	@Override
	protected BilSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GSA;
	}
	
	@Override
	protected CausaleEPSelector istanziaSelettoreCausale() {
		return new CausaleEPGSASelector();
	}
}

