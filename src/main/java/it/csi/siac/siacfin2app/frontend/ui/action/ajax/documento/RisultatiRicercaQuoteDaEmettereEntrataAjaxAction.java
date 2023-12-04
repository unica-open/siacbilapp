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
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaQuoteDaEmettereEntrataAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaEmettereEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * Action per i risultati di ricerca delle quote da emettere di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 20/11/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaQuoteDaEmettereEntrataAjaxAction extends PagedDataTableAjaxAction<ElementoSubdocumentoDaEmettereEntrata,
		RisultatiRicercaQuoteDaEmettereEntrataAjaxModel, SubdocumentoEntrata, RicercaQuoteDaEmettereEntrata, RicercaQuoteDaEmettereEntrataResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5224771706342784957L;
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteDaEmettereEntrataAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_QUOTE_DA_EMETTERE_ENTRATA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_QUOTE_DA_EMETTERE_ENTRATA);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaQuoteDaEmettereEntrata request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaQuoteDaEmettereEntrata request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoSubdocumentoDaEmettereEntrata getInstance(SubdocumentoEntrata e) throws FrontEndBusinessException {
		return new ElementoSubdocumentoDaEmettereEntrata(e, model.isGestioneUEB());
	}
	
	@Override
	protected RicercaQuoteDaEmettereEntrataResponse getResponse(RicercaQuoteDaEmettereEntrata request) {
		return documentoEntrataService.ricercaQuoteDaEmettereEntrata(request);
	}
	
	@Override
	protected ListaPaginata<SubdocumentoEntrata> ottieniListaRisultati(RicercaQuoteDaEmettereEntrataResponse response) {
		return response.getListaSubdocumenti();
	}
	
}