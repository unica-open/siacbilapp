/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;

/**
 * Executor per la lettura delle liste.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class ListeExecutor implements ServiceExecutor<Liste, ListeResponse> {

	private final GenericService genericService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param genericService il servizio da injettare
	 */
	public ListeExecutor(GenericService genericService) {
		this.genericService = genericService;
	}
	
	@Override
	public ListeResponse executeService(Liste req) {
		return genericService.liste(req);
	}
	
}