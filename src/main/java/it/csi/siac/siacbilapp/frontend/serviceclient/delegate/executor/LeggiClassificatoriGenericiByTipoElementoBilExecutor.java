/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;

/**
 * Executor per la lettura dei classificatori generici by tipo elemento bil.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class LeggiClassificatoriGenericiByTipoElementoBilExecutor implements ServiceExecutor<LeggiClassificatoriGenericiByTipoElementoBil, LeggiClassificatoriGenericiByTipoElementoBilResponse> {

	private final ClassificatoreBilService classificatoreBilService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param classificatoreBilService il servizio da injettare
	 */
	public LeggiClassificatoriGenericiByTipoElementoBilExecutor(ClassificatoreBilService classificatoreBilService) {
		this.classificatoreBilService = classificatoreBilService;
	}
	
	@Override
	public LeggiClassificatoriGenericiByTipoElementoBilResponse executeService(LeggiClassificatoriGenericiByTipoElementoBil req) {
		return classificatoreBilService.leggiClassificatoriGenericiByTipoElementoBil(req);
	}
	
}