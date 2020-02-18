/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.documento;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaSinteticaQuoteDocumentoEntrataPerIvaAjaxModel;

/**
 * Action per i risultati di ricerca delle quote di entrata.
 * 
 * @author Alessandro Marchino
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaSinteticaQuoteDocumentoEntrataPerIvaAjaxAction extends RisultatiRicercaSinteticaQuoteDocumentoEntrataBaseAjaxAction<RisultatiRicercaSinteticaQuoteDocumentoEntrataPerIvaAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2521846964745106820L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaSinteticaQuoteDocumentoEntrataPerIvaAjaxAction() {
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_IVA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_IVA);
	}
}
