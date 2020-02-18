/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIvaResponse;

/**
 * Executor per la lettura dei tipi di registrazione iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaTipoRegistrazioneIvaExecutor implements ServiceExecutor<RicercaTipoRegistrazioneIva, RicercaTipoRegistrazioneIvaResponse> {

	private final DocumentoIvaService documentoIvaService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoIvaService il servizio da injettare
	 */
	public RicercaTipoRegistrazioneIvaExecutor(DocumentoIvaService documentoIvaService) {
		this.documentoIvaService = documentoIvaService;
	}
	
	@Override
	public RicercaTipoRegistrazioneIvaResponse executeService(RicercaTipoRegistrazioneIva req) {
		return documentoIvaService.ricercaTipoRegistrazioneIva(req);
	}
	
}