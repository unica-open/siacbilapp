/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;

/**
 * Executor per la lettura dei tipi provvedimento.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class TipiProvvedimentoExecutor implements ServiceExecutor<TipiProvvedimento, TipiProvvedimentoResponse> {

	private final ProvvedimentoService provvedimentoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param provvedimentoService il servizio da injettare
	 */
	public TipiProvvedimentoExecutor(ProvvedimentoService provvedimentoService) {
		this.provvedimentoService = provvedimentoService;
	}
	
	@Override
	public TipiProvvedimentoResponse executeService(TipiProvvedimento req) {
		return provvedimentoService.getTipiProvvedimento(req);
	}
	
}