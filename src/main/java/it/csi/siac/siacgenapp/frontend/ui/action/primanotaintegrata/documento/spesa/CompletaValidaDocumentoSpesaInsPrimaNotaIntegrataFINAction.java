/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.documento.spesa;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento.spesa.InserisciPrimaNotaIntegrataDocumentoSpesaBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.documento.spesa.CompletaValidaDocumentoSpesaInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per la validazione della prima integrata per il documento. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(InserisciPrimaNotaIntegrataDocumentoSpesaBaseAction.MODEL_COMPLETA_E_VALIDA_DOCUMENTO_SPESA_FIN)
public class CompletaValidaDocumentoSpesaInsPrimaNotaIntegrataFINAction extends InserisciPrimaNotaIntegrataDocumentoSpesaBaseAction<CompletaValidaDocumentoSpesaInsPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5893213923179511388L;

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
		return null;
	}
}

