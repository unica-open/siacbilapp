/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiopeResponse;

/**
 * Executor per la lettura del tree siope entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class LeggiTreeSiopeEntrataExecutor implements ServiceExecutor<LeggiTreeSiope, LeggiTreeSiopeResponse> {

	private final ClassificatoreBilService classificatoreBilService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param classificatoreBilService il servizio da injettare
	 */
	public LeggiTreeSiopeEntrataExecutor(ClassificatoreBilService classificatoreBilService) {
		this.classificatoreBilService = classificatoreBilService;
	}
	
	@Override
	public LeggiTreeSiopeResponse executeService(LeggiTreeSiope req) {
		return classificatoreBilService.leggiTreeSiopeEntrata(req);
	}
	
}