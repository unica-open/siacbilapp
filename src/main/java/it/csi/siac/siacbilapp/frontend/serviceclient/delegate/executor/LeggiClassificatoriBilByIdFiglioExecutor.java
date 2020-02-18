/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglio;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglioResponse;

/**
 * Executor per la lettura dei classificatori bil by id figlio.
 * 
 * @author Domenico Lisi
 *
 */
public class LeggiClassificatoriBilByIdFiglioExecutor implements ServiceExecutor<LeggiClassificatoriBilByIdFiglio, LeggiClassificatoriBilByIdFiglioResponse> {

	private final ClassificatoreBilService classificatoreBilService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param classificatoreBilService il servizio da injettare
	 */
	public LeggiClassificatoriBilByIdFiglioExecutor(ClassificatoreBilService classificatoreBilService) {
		this.classificatoreBilService = classificatoreBilService;
	}
	
	@Override
	public LeggiClassificatoriBilByIdFiglioResponse executeService(LeggiClassificatoriBilByIdFiglio req) {
		return classificatoreBilService.leggiClassificatoriByIdFiglio(req);
	}
	
}