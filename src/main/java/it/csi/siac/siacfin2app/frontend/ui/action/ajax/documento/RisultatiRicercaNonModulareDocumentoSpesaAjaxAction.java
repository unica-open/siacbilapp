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
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaDocumentoAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumento;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoFactory;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * Action per i risultati di ricerca del documento.
 * 
 * @author Valentina
 * @version 1.0.0 - 04/05/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaNonModulareDocumentoSpesaAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoDocumento, 
	RisultatiRicercaDocumentoAjaxModel, DocumentoSpesa, RicercaSinteticaDocumentoSpesa, RicercaSinteticaDocumentoSpesaResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4918152133385653858L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaNonModulareDocumentoSpesaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_DOCUMENTI_SPESA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_DOCUMENTI_SPESA);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = false;
		if(Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO, Boolean.class))) {
			result = true;
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		}
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaDocumentoSpesa request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaDocumentoSpesa request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoDocumentoSpesa ottieniIstanza(DocumentoSpesa e) throws FrontEndBusinessException {
		return ElementoDocumentoFactory.getInstanceSpesa(e);
	}
	
	@Override
	protected RicercaSinteticaDocumentoSpesaResponse ottieniResponse(RicercaSinteticaDocumentoSpesa request) {	
		return documentoSpesaService.ricercaSinteticaDocumentoSpesa(request);
	}
	
	@Override
	protected ListaPaginata<DocumentoSpesa> ottieniListaRisultati(RicercaSinteticaDocumentoSpesaResponse response) {
		return response.getDocumenti();
	}
	
}
