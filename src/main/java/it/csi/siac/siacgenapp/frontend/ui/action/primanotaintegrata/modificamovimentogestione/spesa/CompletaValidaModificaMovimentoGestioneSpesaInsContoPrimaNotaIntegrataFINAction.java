/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.spesa;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.spesa.GestioneModificaMovimentoGestioneSpesaInsContoPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.spesa.GestioneModificaMovimentoGestioneSpesaInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.spesa.CompletaValidaModificaMovimentoGestioneSpesaInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, modifica del movimento di gestione di spesa. Modulo FIN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/11/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneModificaMovimentoGestioneSpesaInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_INS_MODIFICA_MOVIMENTO_GESTIONE_SPESA_FIN)
public class CompletaValidaModificaMovimentoGestioneSpesaInsContoPrimaNotaIntegrataFINAction extends GestioneModificaMovimentoGestioneSpesaInsContoPrimaNotaIntegrataBaseAction <CompletaValidaModificaMovimentoGestioneSpesaInsPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6555754929254651186L;

	@Override
	protected BilSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}

}

