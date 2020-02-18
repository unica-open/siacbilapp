/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCCResponse;

/**
 * Executor per la lettura dei codici PCC.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaCodicePCCExecutor implements ServiceExecutor<RicercaCodicePCC, RicercaCodicePCCResponse> {

	private final DocumentoService documentoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoService il servizio da injettare
	 */
	public RicercaCodicePCCExecutor(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}
	
	@Override
	public RicercaCodicePCCResponse executeService(RicercaCodicePCC req) {
		return documentoService.ricercaCodicePCC(req);
	}
	
}