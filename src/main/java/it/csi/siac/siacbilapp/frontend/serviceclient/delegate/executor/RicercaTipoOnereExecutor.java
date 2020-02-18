/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnereResponse;

/**
 * Executor per la lettura dei tipi onere.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaTipoOnereExecutor implements ServiceExecutor<RicercaTipoOnere, RicercaTipoOnereResponse> {

	private final DocumentoService documentoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoService il servizio da injettare
	 */
	public RicercaTipoOnereExecutor(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}
	
	@Override
	public RicercaTipoOnereResponse executeService(RicercaTipoOnere req) {
		return documentoService.ricercaTipoOnere(req);
	}
	
}