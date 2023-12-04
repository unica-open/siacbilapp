/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.documento;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaSinteticaQuoteDocumentoSpesaBaseAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Action per i risultati di ricerca delle quote di spesa.
 * 
 * @author Alessandro Marchino
 * @param <M> la tipizzazione del model
 */
public abstract class RisultatiRicercaSinteticaQuoteDocumentoSpesaBaseAjaxAction<M extends RisultatiRicercaSinteticaQuoteDocumentoSpesaBaseAjaxModel>
	extends PagedDataTableAjaxAction<ElementoSubdocumentoSpesa, M, SubdocumentoSpesa, RicercaSinteticaModulareQuoteByDocumentoSpesa, RicercaSinteticaModulareQuoteByDocumentoSpesaResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2521846964745106820L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaModulareQuoteByDocumentoSpesa request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaModulareQuoteByDocumentoSpesa request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoSubdocumentoSpesa getInstance(SubdocumentoSpesa e) {
		return new ElementoSubdocumentoSpesa(e, model.isGestioneUEB());
	}
	
	@Override
	protected RicercaSinteticaModulareQuoteByDocumentoSpesaResponse getResponse(RicercaSinteticaModulareQuoteByDocumentoSpesa request) {
		return documentoSpesaService.ricercaSinteticaModulareQuoteByDocumentoSpesa(request);
	}
	
	@Override
	protected ListaPaginata<SubdocumentoSpesa> ottieniListaRisultati(RicercaSinteticaModulareQuoteByDocumentoSpesaResponse response) {
		return response.getSubdocumentiSpesa();
	}

	@Override
	protected boolean isAggiornareRientroPosizioneStart() {
		return false;
	}
}
