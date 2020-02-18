/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;

/**
 * Executor per la lettura dei classificatori by relazione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class LeggiClassificatoriByRelazioneExecutor implements ServiceExecutor<LeggiClassificatoriByRelazione, LeggiClassificatoriByRelazioneResponse> {

	private final ClassificatoreBilService classificatoreBilService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param classificatoreBilService il servizio da injettare
	 */
	public LeggiClassificatoriByRelazioneExecutor(ClassificatoreBilService classificatoreBilService) {
		this.classificatoreBilService = classificatoreBilService;
	}
	
	@Override
	public LeggiClassificatoriByRelazioneResponse executeService(LeggiClassificatoriByRelazione req) {
		return classificatoreBilService.leggiClassificatoriByRelazione(req);
	}
	
}