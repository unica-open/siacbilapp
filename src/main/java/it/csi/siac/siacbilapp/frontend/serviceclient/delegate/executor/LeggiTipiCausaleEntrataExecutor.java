/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrataResponse;

/**
 * Executor per la lettura dei tipi causale entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class LeggiTipiCausaleEntrataExecutor implements ServiceExecutor<LeggiTipiCausaleEntrata, LeggiTipiCausaleEntrataResponse> {

	private final PreDocumentoEntrataService preDocumentoEntrataService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param preDocumentoEntrataService il servizio da injettare
	 */
	public LeggiTipiCausaleEntrataExecutor(PreDocumentoEntrataService preDocumentoEntrataService) {
		this.preDocumentoEntrataService = preDocumentoEntrataService;
	}
	
	@Override
	public LeggiTipiCausaleEntrataResponse executeService(LeggiTipiCausaleEntrata req) {
		return preDocumentoEntrataService.leggiTipiCausaleEntrata(req);
	}
	
}