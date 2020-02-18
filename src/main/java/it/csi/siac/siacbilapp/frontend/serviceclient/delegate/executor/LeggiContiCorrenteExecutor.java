/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrenteResponse;

/**
 * Executor per la lettura dei conti corrente.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class LeggiContiCorrenteExecutor implements ServiceExecutor<LeggiContiCorrente, LeggiContiCorrenteResponse> {

	private final PreDocumentoEntrataService preDocumentoEntrataService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param preDocumentoEntrataService il servizio da injettare
	 */
	public LeggiContiCorrenteExecutor(PreDocumentoEntrataService preDocumentoEntrataService) {
		this.preDocumentoEntrataService = preDocumentoEntrataService;
	}
	
	@Override
	public LeggiContiCorrenteResponse executeService(LeggiContiCorrente req) {
		return preDocumentoEntrataService.leggiContiCorrente(req);
	}
	
}