/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeClassificatoreResponse;

/**
 * Executor per la ricerca puntuale del classificatore.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 01/03/2017
 *
 */
public class RicercaPuntualeClassificatoreExecutor implements ServiceExecutor<RicercaPuntualeClassificatore, RicercaPuntualeClassificatoreResponse> {

	private final ClassificatoreBilService classificatoreBilService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param classificatoreBilService il servizio da injettare
	 */
	public RicercaPuntualeClassificatoreExecutor(ClassificatoreBilService classificatoreBilService) {
		this.classificatoreBilService = classificatoreBilService;
	}
	
	@Override
	public RicercaPuntualeClassificatoreResponse executeService(RicercaPuntualeClassificatore req) {
		return classificatoreBilService.ricercaPuntualeClassificatore(req);
	}
	
}