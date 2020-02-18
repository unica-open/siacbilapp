/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.documento;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaSinteticaQuoteDocumentoEntrataCollegatoAjaxModel;

/**
 * Action per i risultati di ricerca delle quote di spesa collegate.
 * 
 * @author Daniela Tarantino
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaSinteticaQuoteDocumentoEntrataCollegatoAjaxAction extends
		RisultatiRicercaSinteticaQuoteDocumentoEntrataCollegatoBaseAjaxAction<RisultatiRicercaSinteticaQuoteDocumentoEntrataCollegatoAjaxModel> {
	/** Per la serializzazione */
	private static final long serialVersionUID = -2521846964745106821L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaSinteticaQuoteDocumentoEntrataCollegatoAjaxAction() {
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_COLLEGATO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_COLLEGATO);
	}

}
