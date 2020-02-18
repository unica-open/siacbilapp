/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenerico;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenericoResponse;

/**
 * Executor per la ricerca del tipo classificatore generico.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 07/09/2017
 *
 */
public class RicercaTipoClassificatoreGenericoExecutor implements ServiceExecutor<RicercaTipoClassificatoreGenerico, RicercaTipoClassificatoreGenericoResponse> {

	private final ClassificatoreBilService classificatoreBilService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param classificatoreBilService il servizio da injettare
	 */
	public RicercaTipoClassificatoreGenericoExecutor(ClassificatoreBilService classificatoreBilService) {
		this.classificatoreBilService = classificatoreBilService;
	}
	
	@Override
	public RicercaTipoClassificatoreGenericoResponse executeService(RicercaTipoClassificatoreGenerico req) {
		return classificatoreBilService.ricercaTipoClassificatoreGenerico(req);
	}
	
}