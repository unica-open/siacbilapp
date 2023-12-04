/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.documento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaSinteticaQuoteDocumentoEntrataBaseAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * Action per i risultati di ricerca delle quote di entrata.
 * 
 * @author Alessandro Marchino
 * @param <M> la tipizzazione del model
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaSinteticaQuoteDocumentoEntrataBaseAjaxAction<M extends RisultatiRicercaSinteticaQuoteDocumentoEntrataBaseAjaxModel>
	extends PagedDataTableAjaxAction<ElementoSubdocumentoEntrata, M, SubdocumentoEntrata, RicercaSinteticaModulareQuoteByDocumentoEntrata, RicercaSinteticaModulareQuoteByDocumentoEntrataResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2521846964745106820L;
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaModulareQuoteByDocumentoEntrata request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaModulareQuoteByDocumentoEntrata request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoSubdocumentoEntrata getInstance(SubdocumentoEntrata e) {
		return new ElementoSubdocumentoEntrata(e, model.isGestioneUEB());
	}
	
	@Override
	protected RicercaSinteticaModulareQuoteByDocumentoEntrataResponse getResponse(RicercaSinteticaModulareQuoteByDocumentoEntrata request) {
		return documentoEntrataService.ricercaSinteticaModulareQuoteByDocumentoEntrata(request);
	}
	
	@Override
	protected ListaPaginata<SubdocumentoEntrata> ottieniListaRisultati(RicercaSinteticaModulareQuoteByDocumentoEntrataResponse response) {
		return response.getSubdocumentiEntrata();
	}

	@Override
	protected boolean isAggiornareRientroPosizioneStart() {
		return false;
	}
}
