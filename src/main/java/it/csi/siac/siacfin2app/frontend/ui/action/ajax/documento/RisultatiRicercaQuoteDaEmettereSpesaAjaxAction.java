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
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaQuoteDaEmettereSpesaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaEmettereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Action per i risultati di ricerca delle quote da emettere di spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 20/11/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaQuoteDaEmettereSpesaAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoSubdocumentoDaEmettereSpesa,
		RisultatiRicercaQuoteDaEmettereSpesaAjaxModel, SubdocumentoSpesa, RicercaQuoteDaEmettereSpesa, RicercaQuoteDaEmettereSpesaResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2744620065296968603L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteDaEmettereSpesaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_QUOTE_DA_EMETTERE_SPESA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_QUOTE_DA_EMETTERE_SPESA);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaQuoteDaEmettereSpesa request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaQuoteDaEmettereSpesa request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoSubdocumentoDaEmettereSpesa ottieniIstanza(SubdocumentoSpesa e) throws FrontEndBusinessException {
		return new ElementoSubdocumentoDaEmettereSpesa(e, model.isGestioneUEB());
	}
	
	@Override
	protected RicercaQuoteDaEmettereSpesaResponse ottieniResponse(RicercaQuoteDaEmettereSpesa request) {
		return documentoSpesaService.ricercaQuoteDaEmettereSpesa(request);
	}
	
	@Override
	protected ListaPaginata<SubdocumentoSpesa> ottieniListaRisultati(RicercaQuoteDaEmettereSpesaResponse response) {
		return response.getListaSubdocumenti();
	}
	
}