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
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.documento.entrata.CompletaDocumentoEntrataInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata per il documento di entrata. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(InserisciPrimaNotaIntegrataDocumentoEntrataBaseAction.MODEL_COMPLETA_DOCUMENTO_ENTRATA_FIN)
public class CompletaDocumentoEntrataInsPrimaNotaIntegrataFINAction extends InserisciPrimaNotaIntegrataDocumentoEntrataBaseAction<CompletaDocumentoEntrataInsPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6158252603729018430L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		//settato il titolo del breadcrum, chiamo l'execute della superclasse
		return super.execute();
	}

	@Override
	protected BilSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		//ambito FIN: seleziono il parametro
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}

	@Override
	protected CausaleEPSelector istanziaSelettoreCausale() {
		//non devo filtrare le causali
		return null;
	}
	
}

