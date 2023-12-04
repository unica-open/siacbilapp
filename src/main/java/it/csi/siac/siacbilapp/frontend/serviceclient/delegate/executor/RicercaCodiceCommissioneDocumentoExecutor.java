/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceCommissioneDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceCommissioneDocumentoResponse;

/**
 * Executor per la lettura dei codici commissioni documento.
 * 
 *
 */
public class RicercaCodiceCommissioneDocumentoExecutor implements ServiceExecutor<RicercaCodiceCommissioneDocumento, RicercaCodiceCommissioneDocumentoResponse> {

	private final DocumentoService documentoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoService il servizio da injettare
	 */
	public RicercaCodiceCommissioneDocumentoExecutor(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}
	
	@Override
	public RicercaCodiceCommissioneDocumentoResponse executeService(RicercaCodiceCommissioneDocumento req) {
		return documentoService.ricercaCodiceCommissioneDocumento(req);
	}
	
}