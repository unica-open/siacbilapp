/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.liquidazione;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.liquidazione.GestioneLiquidazioneInsContoPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.liquidazione.GestioneLiquidazioneInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.liquidazione.CompletaLiquidazioneInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio
 * 
 * @author Paggio Simona
 * @author Valentina
 * @version 1.0.0 - 06/05/2015
 * @version 1.0.0 - 14/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneLiquidazioneInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_INS_LIQUIDAZIONE_FIN)
public class CompletaLiquidazioneInsContoPrimaNotaIntegrataFINAction extends GestioneLiquidazioneInsContoPrimaNotaIntegrataBaseAction <CompletaLiquidazioneInsPrimaNotaIntegrataFINModel> {

	/** per serializzazione **/
	private static final long serialVersionUID = -8904794470424121747L;
	
	@Override
	protected BilSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}




}

