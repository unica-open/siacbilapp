/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.ProgettoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;

/**
 * Executor per la lettura dei tipi ambito.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaTipiAmbitoExecutor implements ServiceExecutor<RicercaTipiAmbito, RicercaTipiAmbitoResponse> {

	private final ProgettoService progettoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param progettoService il servizio da injettare
	 */
	public RicercaTipiAmbitoExecutor(ProgettoService progettoService) {
		this.progettoService = progettoService;
	}
	
	@Override
	public RicercaTipiAmbitoResponse executeService(RicercaTipiAmbito req) {
		return progettoService.ricercaTipiAmbito(req);
	}
	
}