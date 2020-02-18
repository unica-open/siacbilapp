/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresaResponse;

/**
 * Executor per la lettura dei tipi impresa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaTipoImpresaExecutor implements ServiceExecutor<RicercaTipoImpresa, RicercaTipoImpresaResponse> {

	private final DocumentoService documentoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoService il servizio da injettare
	 */
	public RicercaTipoImpresaExecutor(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}
	
	@Override
	public RicercaTipoImpresaResponse executeService(RicercaTipoImpresa req) {
		return documentoService.ricercaTipoImpresa(req);
	}
	
}