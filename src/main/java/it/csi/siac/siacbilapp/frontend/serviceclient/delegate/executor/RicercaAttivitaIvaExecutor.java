/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaResponse;

/**
 * Executor per la lettura delle attivit&agrave; iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaAttivitaIvaExecutor implements ServiceExecutor<RicercaAttivitaIva, RicercaAttivitaIvaResponse> {

	private final DocumentoIvaService documentoIvaService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoIvaService il servizio da injettare
	 */
	public RicercaAttivitaIvaExecutor(DocumentoIvaService documentoIvaService) {
		this.documentoIvaService = documentoIvaService;
	}
	
	@Override
	public RicercaAttivitaIvaResponse executeService(RicercaAttivitaIva req) {
		return documentoIvaService.ricercaAttivitaIva(req);
	}
	
}