/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValutaResponse;

/**
 * Executor per la lettura della valuta.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaValutaExecutor implements ServiceExecutor<RicercaValuta, RicercaValutaResponse> {

	private final DocumentoIvaService documentoIvaService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoIvaService il servizio da injettare
	 */
	public RicercaValutaExecutor(DocumentoIvaService documentoIvaService) {
		this.documentoIvaService = documentoIvaService;
	}
	
	@Override
	public RicercaValutaResponse executeService(RicercaValuta req) {
		return documentoIvaService.ricercaValuta(req);
	}
	
}