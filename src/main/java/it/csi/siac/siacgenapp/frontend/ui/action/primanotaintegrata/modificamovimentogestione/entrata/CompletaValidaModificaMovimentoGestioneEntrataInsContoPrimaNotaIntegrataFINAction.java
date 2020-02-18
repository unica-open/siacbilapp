/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.entrata;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.entrata.GestioneModificaMovimentoGestioneEntrataInsContoPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.entrata.GestioneModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.entrata.CompletaValidaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, modifica del movimento di gestione di entrata. Modulo FIN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/11/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_INS_MODIFICA_MOVIMENTO_GESTIONE_ENTRATA_FIN)
public class CompletaValidaModificaMovimentoGestioneEntrataInsContoPrimaNotaIntegrataFINAction extends GestioneModificaMovimentoGestioneEntrataInsContoPrimaNotaIntegrataBaseAction <CompletaValidaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6555754929254651186L;

	@Override
	protected BilSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}

}

