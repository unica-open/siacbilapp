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
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaQuoteSpesaAjaxModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Action per i risultati di ricerca delle quote di spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 22/09/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaQuoteSpesaAjaxAction extends GenericRisultatiRicercaAjaxAction<SubdocumentoSpesa, 
		RisultatiRicercaQuoteSpesaAjaxModel, SubdocumentoSpesa, RicercaQuotaSpesa, RicercaQuotaSpesaResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1305483203485651012L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteSpesaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_QUOTE_SPESA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_QUOTE_SPESA);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaQuotaSpesa request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaQuotaSpesa request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected SubdocumentoSpesa ottieniIstanza(SubdocumentoSpesa e) {
		return e;
	}
	
	@Override
	protected RicercaQuotaSpesaResponse ottieniResponse(RicercaQuotaSpesa request) {	
		return documentoSpesaService.ricercaQuotaSpesa(request);
	}
	
	@Override
	protected ListaPaginata<SubdocumentoSpesa> ottieniListaRisultati(RicercaQuotaSpesaResponse response) {
		return response.getListaSubdocumenti();
	}
	
}
