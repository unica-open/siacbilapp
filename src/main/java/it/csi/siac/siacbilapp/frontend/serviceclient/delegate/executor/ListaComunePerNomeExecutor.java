/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNome;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNomeResponse;

/**
 * Executor per la lettura della lista comune per nome.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class ListaComunePerNomeExecutor implements ServiceExecutor<ListaComunePerNome, ListaComunePerNomeResponse> {

	private final GenericService genericService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param genericService il servizio da injettare
	 */
	public ListaComunePerNomeExecutor(GenericService genericService) {
		this.genericService = genericService;
	}
	
	@Override
	public ListaComunePerNomeResponse executeService(ListaComunePerNome req) {
		return genericService.findComunePerNome(req);
	}
	
}