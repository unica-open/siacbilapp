/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.documento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaQuoteEntrataAjaxModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * Action per i risultati di ricerca delle quote di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 22/09/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaQuoteEntrataAjaxAction extends GenericRisultatiRicercaAjaxAction<SubdocumentoEntrata, 
		RisultatiRicercaQuoteEntrataAjaxModel, SubdocumentoEntrata, RicercaQuotaEntrata, RicercaQuotaEntrataResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -888982202153585035L;
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteEntrataAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_QUOTE_ENTRATA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_QUOTE_ENTRATA);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaQuotaEntrata request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaQuotaEntrata request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected SubdocumentoEntrata ottieniIstanza(SubdocumentoEntrata e) {
		return e;
	}
	
	@Override
	protected RicercaQuotaEntrataResponse ottieniResponse(RicercaQuotaEntrata request) {	
		return documentoEntrataService.ricercaQuotaEntrata(request);
	}
	
	@Override
	protected ListaPaginata<SubdocumentoEntrata> ottieniListaRisultati(RicercaQuotaEntrataResponse response) {
		return response.getListaSubdocumenti();
	}
	
}
