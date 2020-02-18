/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnereResponse;

/**
 * Executor per la lettura delle nature onere.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaNaturaOnereExecutor implements ServiceExecutor<RicercaNaturaOnere, RicercaNaturaOnereResponse> {

	private final DocumentoService documentoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoService il servizio da injettare
	 */
	public RicercaNaturaOnereExecutor(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}
	
	@Override
	public RicercaNaturaOnereResponse executeService(RicercaNaturaOnere req) {
		return documentoService.ricercaNaturaOnere(req);
	}
	
}