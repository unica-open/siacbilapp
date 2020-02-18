/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770Response;

/**
 * Executor per la lettura delle causali 770.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaCausale770Executor implements ServiceExecutor<RicercaCausale770, RicercaCausale770Response> {

	private final DocumentoService documentoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoService il servizio da injettare
	 */
	public RicercaCausale770Executor(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}
	
	@Override
	public RicercaCausale770Response executeService(RicercaCausale770 req) {
		return documentoService.ricercaCausale770(req);
	}
	
}