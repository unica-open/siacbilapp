/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazioneResponse;

/**
 * Executor per la lettura dei tipi variazione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaTipoVariazioneExecutor implements ServiceExecutor<RicercaTipoVariazione, RicercaTipoVariazioneResponse> {

	private final VariazioneDiBilancioService variazioneDiBilancioService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param variazioneDiBilancioService il servizio da injettare
	 */
	public RicercaTipoVariazioneExecutor(VariazioneDiBilancioService variazioneDiBilancioService) {
		this.variazioneDiBilancioService = variazioneDiBilancioService;
	}
	
	@Override
	public RicercaTipoVariazioneResponse executeService(RicercaTipoVariazione req) {
		return variazioneDiBilancioService.ricercaTipoVariazione(req);
	}
	
}