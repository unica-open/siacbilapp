/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto.RisultatiRicercaQuoteDaAssociareAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaAssociare;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaAssociareFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociarePredocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociarePredocumentoResponse;
import it.csi.siac.siacfin2ser.model.Subdocumento;

/**
 * Action per i risultati di ricerca delle quote di spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 22/09/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaQuoteDaAssociareAjaxAction extends PagedDataTableAjaxAction<ElementoSubdocumentoDaAssociare, 
		RisultatiRicercaQuoteDaAssociareAjaxModel, Subdocumento<?,?> , RicercaQuoteDaAssociarePredocumento, RicercaQuoteDaAssociarePredocumentoResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1305483203485651012L;
	
	@Autowired private transient DocumentoService documentoService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteDaAssociareAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_QUOTE_DA_ASSOCIARE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_QUOTE_DA_ASSOCIARE);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaQuoteDaAssociarePredocumento request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaQuoteDaAssociarePredocumento request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoSubdocumentoDaAssociare getInstance(Subdocumento<?,?> e) {
		return ElementoSubdocumentoDaAssociareFactory.getInstance(e);
	}
	
	@Override
	protected RicercaQuoteDaAssociarePredocumentoResponse getResponse(RicercaQuoteDaAssociarePredocumento request) {	
		return documentoService.ricercaQuoteDaAssociarePredocumento(request);
	}
	
	@Override
	protected ListaPaginata<Subdocumento<?,?>> ottieniListaRisultati(RicercaQuoteDaAssociarePredocumentoResponse response) {
		return response.getListaSubdocumenti();
	}
	
}
