/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.ordinativo.pagamento;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.ordinativo.pagamento.GestioneOrdinativoPagamentoInsContoPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.ordinativo.pagamento.GestioneOrdinativoPagamentoInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento.CompletaOrdinativoPagamentoInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio
 * 
 * @author Paggio Simona
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/05/2015
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneOrdinativoPagamentoInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_INS_ORDINATIVO_PAGAMENTO_FIN)
public class CompletaOrdinativoPagamentoInsContoPrimaNotaIntegrataFINAction extends GestioneOrdinativoPagamentoInsContoPrimaNotaIntegrataBaseAction <CompletaOrdinativoPagamentoInsPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 161431115591904902L;

	@Override
	protected BilSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}

}
