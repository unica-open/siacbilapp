/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIvaResponse;

/**
 * Executor per la lettura delle aliquote iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaAliquotaIvaExecutor implements ServiceExecutor<RicercaAliquotaIva, RicercaAliquotaIvaResponse> {

	private final DocumentoIvaService documentoIvaService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoIvaService il servizio da injettare
	 */
	public RicercaAliquotaIvaExecutor(DocumentoIvaService documentoIvaService) {
		this.documentoIvaService = documentoIvaService;
	}
	
	@Override
	public RicercaAliquotaIvaResponse executeService(RicercaAliquotaIva req) {
		return documentoIvaService.ricercaAliquotaIva(req);
	}
	
}