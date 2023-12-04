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
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * Action per i risultati di ricerca delle quote di entrata da associare ad un provvisorio.
 * 
 * @author Valentina
 * @version 1.0.0 - 17/03/2016
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaQuoteEntrataDaAssociareAProvvisorioAjaxAction extends PagedDataTableAjaxAction<ElementoSubdocumentoDaAssociare, 
		RisultatiRicercaQuoteDaAssociareAProvvisorioAjaxModel, SubdocumentoEntrata , RicercaQuotePerProvvisorioEntrata, RicercaQuotePerProvvisorioEntrataResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4681974151414187865L;
	
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteEntrataDaAssociareAProvvisorioAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_QUOTE_ENTRATA_PER_PROVVISORIO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_QUOTE_ENTRATA_PER_PROVVISORIO);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaQuotePerProvvisorioEntrata request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaQuotePerProvvisorioEntrata request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoSubdocumentoDaAssociare getInstance(SubdocumentoEntrata e) {
		return ElementoSubdocumentoDaAssociareFactory.getInstance(e);
	}
	
	@Override
	protected RicercaQuotePerProvvisorioEntrataResponse getResponse(RicercaQuotePerProvvisorioEntrata request) {	
		return documentoEntrataService.ricercaQuotePerProvvisorioEntrata(request);
	}
	
	@Override
	protected ListaPaginata<SubdocumentoEntrata> ottieniListaRisultati(RicercaQuotePerProvvisorioEntrataResponse response) {
		return response.getListaSubdocumenti();
	}
	
}
