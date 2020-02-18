/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacattser.frontend.webservice.AttoDiLeggeService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLeggeResponse;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;

/**
 * Executor per la lettura dei tipi atto di legge.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaTipiAttoDiLeggeExecutor implements ServiceExecutor<RicercaTipiAttoDiLegge, RicercaTipiAttoDiLeggeResponse> {

	private final AttoDiLeggeService attoDiLeggeService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param attoDiLeggeService il servizio da injettare
	 */
	public RicercaTipiAttoDiLeggeExecutor(AttoDiLeggeService attoDiLeggeService) {
		this.attoDiLeggeService = attoDiLeggeService;
	}
	
	@Override
	public RicercaTipiAttoDiLeggeResponse executeService(RicercaTipiAttoDiLegge req) {
		return attoDiLeggeService.getTipiAttoLegge(req);
	}
	
}