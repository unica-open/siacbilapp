/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.provvisoriocassa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.provvisoriocassa.RisultatiRicercaQuoteDaAssociareAProvvisorioAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaAssociare;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaAssociareFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Action per i risultati di ricerca delle quote di entrata da associare ad un provvisorio.
 * 
 * @author Valentina
 * @version 1.0.0 - 17/03/2016
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaQuoteSpesaDaAssociareAProvvisorioAjaxAction extends PagedDataTableAjaxAction<ElementoSubdocumentoDaAssociare, 
		RisultatiRicercaQuoteDaAssociareAProvvisorioAjaxModel, SubdocumentoSpesa , RicercaQuotePerProvvisorioSpesa, RicercaQuotePerProvvisorioSpesaResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4681974151414187865L;
	
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteSpesaDaAssociareAProvvisorioAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_QUOTE_SPESA_PER_PROVVISORIO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_QUOTE_SPESA_PER_PROVVISORIO);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaQuotePerProvvisorioSpesa request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaQuotePerProvvisorioSpesa request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoSubdocumentoDaAssociare getInstance(SubdocumentoSpesa e) {
		return ElementoSubdocumentoDaAssociareFactory.getInstance(e);
	}
	
	@Override
	protected RicercaQuotePerProvvisorioSpesaResponse getResponse(RicercaQuotePerProvvisorioSpesa request) {	
		return documentoSpesaService.ricercaQuotePerProvvisorioSpesa(request);
	}
	
	@Override
	protected ListaPaginata<SubdocumentoSpesa> ottieniListaRisultati(RicercaQuotePerProvvisorioSpesaResponse response) {
		return response.getListaSubdocumenti();
	}
	
}
