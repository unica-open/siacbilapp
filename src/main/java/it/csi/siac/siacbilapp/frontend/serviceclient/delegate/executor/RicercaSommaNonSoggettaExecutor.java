/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggetta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggettaResponse;

/**
 * Executor per la lettura della somma non soggetto.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/06/2016
 *
 */
public class RicercaSommaNonSoggettaExecutor implements ServiceExecutor<RicercaSommaNonSoggetta, RicercaSommaNonSoggettaResponse> {

	private final DocumentoService documentoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoService il servizio da injettare
	 */
	public RicercaSommaNonSoggettaExecutor(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}
	
	@Override
	public RicercaSommaNonSoggettaResponse executeService(RicercaSommaNonSoggetta req) {
		return documentoService.ricercaSommaNonSoggetta(req);
	}
	
}