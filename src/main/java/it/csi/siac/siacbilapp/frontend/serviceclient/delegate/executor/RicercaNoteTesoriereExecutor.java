/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriereResponse;

/**
 * Executor per la lettura delle note tesoriere.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaNoteTesoriereExecutor implements ServiceExecutor<RicercaNoteTesoriere, RicercaNoteTesoriereResponse> {

	private final DocumentoService documentoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoService il servizio da injettare
	 */
	public RicercaNoteTesoriereExecutor(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}
	
	@Override
	public RicercaNoteTesoriereResponse executeService(RicercaNoteTesoriere req) {
		return documentoService.ricercaNoteTesoriere(req);
	}
	
}