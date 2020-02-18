/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvviso;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvvisoResponse;

/**
 * Executor per la lettura dei tipi avviso.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaTipoAvvisoExecutor implements ServiceExecutor<RicercaTipoAvviso, RicercaTipoAvvisoResponse> {

	private final DocumentoService documentoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoService il servizio da injettare
	 */
	public RicercaTipoAvvisoExecutor(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}
	
	@Override
	public RicercaTipoAvvisoResponse executeService(RicercaTipoAvviso req) {
		return documentoService.ricercaTipoAvviso(req);
	}
	
}