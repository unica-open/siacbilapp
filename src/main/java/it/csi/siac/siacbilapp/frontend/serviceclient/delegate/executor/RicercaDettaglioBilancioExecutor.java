/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.BilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;

/**
 * Executor per la lettura di dettaglio del bilancio.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaDettaglioBilancioExecutor implements ServiceExecutor<RicercaDettaglioBilancio, RicercaDettaglioBilancioResponse> {

	private final BilancioService bilancioService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param bilancioService il servizio da injettare
	 */
	public RicercaDettaglioBilancioExecutor(BilancioService bilancioService) {
		this.bilancioService = bilancioService;
	}
	
	@Override
	public RicercaDettaglioBilancioResponse executeService(RicercaDettaglioBilancio req) {
		return bilancioService.ricercaDettaglioBilancio(req);
	}
	
}