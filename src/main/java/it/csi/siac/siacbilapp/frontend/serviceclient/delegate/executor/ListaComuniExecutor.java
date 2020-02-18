/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;

/**
 * Executor per la lettura della lista comuni.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class ListaComuniExecutor implements ServiceExecutor<ListaComuni, ListaComuniResponse> {

	private final GenericService genericService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param genericService il servizio da injettare
	 */
	public ListaComuniExecutor(GenericService genericService) {
		this.genericService = genericService;
	}
	
	@Override
	public ListaComuniResponse executeService(ListaComuni req) {
		return genericService.cercaComuni(req);
	}
	
}