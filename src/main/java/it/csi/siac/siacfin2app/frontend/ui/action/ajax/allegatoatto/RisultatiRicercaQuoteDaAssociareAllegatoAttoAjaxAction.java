/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto.RisultatiRicercaQuoteDaAssociareAllegatoAttoAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaAssociareAllegatoAtto;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaAssociareAllegatoAttoEntrata;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoDaAssociareAllegatoAttoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociare;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociareResponse;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Action per i risultati di ricerca delle quote da associare all'allegato atto.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 22/09/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaQuoteDaAssociareAllegatoAttoAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoSubdocumentoDaAssociareAllegatoAtto<?, ?, ?, ?, ?>, 
		RisultatiRicercaQuoteDaAssociareAllegatoAttoAjaxModel, Subdocumento<?,?> , RicercaQuoteDaAssociare, RicercaQuoteDaAssociareResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4277598998587312201L;
	@Autowired private transient DocumentoService documentoService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaQuoteDaAssociareAllegatoAttoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_QUOTE_DA_ASSOCIARE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_QUOTE_DA_ASSOCIARE);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaQuoteDaAssociare request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaQuoteDaAssociare request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoSubdocumentoDaAssociareAllegatoAtto<?, ?, ?, ?, ?> ottieniIstanza(Subdocumento<?,?> e) {
		if(e instanceof SubdocumentoSpesa) {
			return new ElementoSubdocumentoDaAssociareAllegatoAttoSpesa((SubdocumentoSpesa) e);
		}
		if(e instanceof SubdocumentoEntrata) {
			return new ElementoSubdocumentoDaAssociareAllegatoAttoEntrata((SubdocumentoEntrata) e);
		}
		return null;
	}
	
	@Override
	protected RicercaQuoteDaAssociareResponse ottieniResponse(RicercaQuoteDaAssociare request) {
		return documentoService.ricercaQuoteDaAssociare(request);
	}
	
	@Override
	protected ListaPaginata<Subdocumento<?,?>> ottieniListaRisultati(RicercaQuoteDaAssociareResponse response) {
		return response.getListaSubdocumenti();
	}
	
}
