/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.predocumento;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;

/**
 * The Class RiepilogoCompletaDefinisciPreDocumentoEntrataAjaxAction.
 * @author elisa.chiari@csi.it
 * @version 1.0.0 - 11-02-2020
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RiepilogoCompletaDefinisciPreDocumentoEntrataAjaxAction extends BaseRisultatiRicercaPreDocumentoEntrataAjaxAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5263247481268403876L;

	/**
	 * Instantiates a new riepilogo completa definisci pre documento entrata ajax action.
	 */
	public RiepilogoCompletaDefinisciPreDocumentoEntrataAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.LISTA_PREDOC_SELEZIONATI);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_PREDOCUMENTI_SELEZIONATI_COMPLETA_DEFINISCI);
	}
	
	@Override
	protected void executeEnd() {
		model.addMoreData("importoTotale", sessionHandler.getParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA));
	}
	
}
