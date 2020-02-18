/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata.notacredito.spesa;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.notacredito.spesa.InserisciPrimaNotaIntegrataNotaCreditoSpesaBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.notacredito.spesa.CompletaValidaNotaCreditoSpesaInsPrimaNotaIntegrataGSAModel;

/**
 * Classe di action per la validazione della prima integrata per la nota di credito. Modulo GSA
 * 
 * @author Valentina
 * @version 1.0.0 - 14/03/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(InserisciPrimaNotaIntegrataNotaCreditoSpesaBaseAction.MODEL_COMPLETA_E_VALIDA_NOTA_CREDITO_SPESA_GSA)
public class CompletaValidaNotaCreditoSpesaInsPrimaNotaIntegrataGSAAction extends InserisciPrimaNotaIntegrataNotaCreditoSpesaBaseAction<CompletaValidaNotaCreditoSpesaInsPrimaNotaIntegrataGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6388914847920566506L;


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
		return null;
	}
}

