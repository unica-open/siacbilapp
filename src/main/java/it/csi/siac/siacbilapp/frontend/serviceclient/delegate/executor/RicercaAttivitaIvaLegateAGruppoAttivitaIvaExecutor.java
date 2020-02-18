/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaLegateAGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaLegateAGruppoAttivitaIvaResponse;

/**
 * Executor per la lettura delle attivit&agrave; iva legate a gruppoa ttivit&agrave; iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class RicercaAttivitaIvaLegateAGruppoAttivitaIvaExecutor implements ServiceExecutor<RicercaAttivitaIvaLegateAGruppoAttivitaIva, RicercaAttivitaIvaLegateAGruppoAttivitaIvaResponse> {

	private final DocumentoIvaService documentoIvaService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param documentoIvaService il servizio da injettare
	 */
	public RicercaAttivitaIvaLegateAGruppoAttivitaIvaExecutor(DocumentoIvaService documentoIvaService) {
		this.documentoIvaService = documentoIvaService;
	}
	
	@Override
	public RicercaAttivitaIvaLegateAGruppoAttivitaIvaResponse executeService(RicercaAttivitaIvaLegateAGruppoAttivitaIva req) {
		return documentoIvaService.ricercaAttivitaIvaLegateAGruppoAttivitaIva(req);
	}
	
}