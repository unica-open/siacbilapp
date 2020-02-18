/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreResponse;

/**
 * Executor per la ricerca del tipo classificatore.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 03/08/2017
 *
 */
public class RicercaTipoClassificatoreExecutor implements ServiceExecutor<RicercaTipoClassificatore, RicercaTipoClassificatoreResponse> {

	private final ClassificatoreBilService classificatoreBilService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param classificatoreBilService il servizio da injettare
	 */
	public RicercaTipoClassificatoreExecutor(ClassificatoreBilService classificatoreBilService) {
		this.classificatoreBilService = classificatoreBilService;
	}
	
	@Override
	public RicercaTipoClassificatoreResponse executeService(RicercaTipoClassificatore req) {
		return classificatoreBilService.ricercaTipoClassificatore(req);
	}
	
}