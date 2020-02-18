/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesaResponse;

/**
 * Executor per la lettura dei tipi causale spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class LeggiTipiCausaleSpesaExecutor implements ServiceExecutor<LeggiTipiCausaleSpesa, LeggiTipiCausaleSpesaResponse> {

	private final PreDocumentoSpesaService preDocumentoSpesaService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param preDocumentoSpesaService il servizio da injettare
	 */
	public LeggiTipiCausaleSpesaExecutor(PreDocumentoSpesaService preDocumentoSpesaService) {
		this.preDocumentoSpesaService = preDocumentoSpesaService;
	}
	
	@Override
	public LeggiTipiCausaleSpesaResponse executeService(LeggiTipiCausaleSpesa req) {
		return preDocumentoSpesaService.leggiTipiCausaleSpesa(req);
	}
	
}