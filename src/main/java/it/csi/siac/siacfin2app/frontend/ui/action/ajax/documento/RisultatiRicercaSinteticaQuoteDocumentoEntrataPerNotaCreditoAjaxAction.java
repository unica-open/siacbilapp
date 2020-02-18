/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.documento;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaSinteticaQuoteDocumentoEntrataPerNotaCreditoAjaxModel;

/**
 * Action per i risultati di ricerca delle quote di entrata.
 * 
 * @author Alessandro Marchino
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaSinteticaQuoteDocumentoEntrataPerNotaCreditoAjaxAction extends RisultatiRicercaSinteticaQuoteDocumentoEntrataBaseAjaxAction<RisultatiRicercaSinteticaQuoteDocumentoEntrataPerNotaCreditoAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2243997897249127874L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaSinteticaQuoteDocumentoEntrataPerNotaCreditoAjaxAction() {
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_NOTA_CREDITO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_NOTA_CREDITO);
	}
	
}
