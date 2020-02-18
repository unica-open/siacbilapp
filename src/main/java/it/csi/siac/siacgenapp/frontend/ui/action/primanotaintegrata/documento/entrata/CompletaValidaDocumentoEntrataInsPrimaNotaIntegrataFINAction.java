/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.documento.entrata;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento.entrata.InserisciPrimaNotaIntegrataDocumentoEntrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.documento.entrata.CompletaValidaDocumentoEntrataInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per la validazione della prima integrata per il documento di entrata. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/05/2015
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(InserisciPrimaNotaIntegrataDocumentoEntrataBaseAction.MODEL_COMPLETA_E_VALIDA_DOCUMENTO_ENTRATA_FIN)
public class CompletaValidaDocumentoEntrataInsPrimaNotaIntegrataFINAction extends InserisciPrimaNotaIntegrataDocumentoEntrataBaseAction<CompletaValidaDocumentoEntrataInsPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5083024437937293431L;

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

