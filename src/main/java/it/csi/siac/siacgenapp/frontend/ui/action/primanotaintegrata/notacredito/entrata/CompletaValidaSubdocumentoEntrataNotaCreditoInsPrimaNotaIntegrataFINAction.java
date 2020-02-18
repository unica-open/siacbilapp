/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.notacredito.entrata;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.notacredito.entrata.InserisciPrimaNotaIntegrataNotaCreditoEntrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.notacredito.entrata.InserisciPrimaNotaIntegrataSubdocumentoEntrataNotaCreditoBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPFINSelector;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.notacredito.entrata.CompletaValidaNotaCreditoEntrataInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per la validazione della prima integrata per il subdocumento di entrata di una nota di credito. Modulo GEN
 * 
 * @author Valentina
 * @version 1.0.0 - 14/03/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(InserisciPrimaNotaIntegrataNotaCreditoEntrataBaseAction.MODEL_COMPLETA_E_VALIDA_NOTA_CREDITO_ENTRATA_FIN)
public class CompletaValidaSubdocumentoEntrataNotaCreditoInsPrimaNotaIntegrataFINAction extends InserisciPrimaNotaIntegrataSubdocumentoEntrataNotaCreditoBaseAction<CompletaValidaNotaCreditoEntrataInsPrimaNotaIntegrataFINModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8277431079759370760L;
	
	@Override
	protected BilSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}

	@Override
	protected CausaleEPSelector istanziaSelettoreCausale() {
		return new CausaleEPFINSelector(ottieniElementoPianoDeiContiDaMovimento(), ottieniSoggettoDaMovimento());
	}
}

