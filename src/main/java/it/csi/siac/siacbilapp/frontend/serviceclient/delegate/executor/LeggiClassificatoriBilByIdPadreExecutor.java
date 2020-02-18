/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;

/**
 * Executor per la lettura dei classificatori bil by id padre.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class LeggiClassificatoriBilByIdPadreExecutor implements ServiceExecutor<LeggiClassificatoriBilByIdPadre, LeggiClassificatoriBilByIdPadreResponse> {

	private final ClassificatoreBilService classificatoreBilService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param classificatoreBilService il servizio da injettare
	 */
	public LeggiClassificatoriBilByIdPadreExecutor(ClassificatoreBilService classificatoreBilService) {
		this.classificatoreBilService = classificatoreBilService;
	}
	
	@Override
	public LeggiClassificatoriBilByIdPadreResponse executeService(LeggiClassificatoriBilByIdPadre req) {
		return classificatoreBilService.leggiClassificatoriByIdPadre(req);
	}
	
}