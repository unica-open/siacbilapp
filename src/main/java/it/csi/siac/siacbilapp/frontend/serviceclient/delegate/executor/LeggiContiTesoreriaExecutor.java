/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;

/**
 * Executor per la lettura dei conti tesoreria.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class LeggiContiTesoreriaExecutor implements ServiceExecutor<LeggiContiTesoreria, LeggiContiTesoreriaResponse> {

	private final PreDocumentoSpesaService preDocumentoSpesaService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param preDocumentoSpesaService il servizio da injettare
	 */
	public LeggiContiTesoreriaExecutor(PreDocumentoSpesaService preDocumentoSpesaService) {
		this.preDocumentoSpesaService = preDocumentoSpesaService;
	}
	
	@Override
	public LeggiContiTesoreriaResponse executeService(LeggiContiTesoreria req) {
		return preDocumentoSpesaService.leggiContiTesoreria(req);
	}
	
}