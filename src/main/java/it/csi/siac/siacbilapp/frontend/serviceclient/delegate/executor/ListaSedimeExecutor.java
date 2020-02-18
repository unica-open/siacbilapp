/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSedime;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSedimeResponse;

/**
 * Executor per la lettura della lista sedime.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class ListaSedimeExecutor implements ServiceExecutor<ListaSedime, ListaSedimeResponse> {

	private final GenericService genericService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param genericService il servizio da injettare
	 */
	public ListaSedimeExecutor(GenericService genericService) {
		this.genericService = genericService;
	}
	
	@Override
	public ListaSedimeResponse executeService(ListaSedime req) {
		return genericService.listaSedime(req);
	}
	
}