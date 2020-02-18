/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;

/**
 * Executor per la lettura dei tipi documento.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaTipoDocumentoExecutor implements ServiceExecutor<RicercaTipoDocumento, RicercaTipoDocumentoResponse> {

	private final DocumentoService documentoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoService il servizio da injettare
	 */
	public RicercaTipoDocumentoExecutor(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}
	
	@Override
	public RicercaTipoDocumentoResponse executeService(RicercaTipoDocumento req) {
		return documentoService.ricercaTipoDocumento(req);
	}
	
}