/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.entrata;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.entrata.GestioneModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPGSASelector;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.entrata.CompletaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataGSAModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, modifica del movimento di gestione di entrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/11/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_INS_MODIFICA_MOVIMENTO_GESTIONE_ENTRATA_GSA)
public class CompletaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataGSAAction extends GestioneModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataBaseAction<CompletaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4882122579370276908L;

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

