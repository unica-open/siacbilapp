/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;

/**
 * Executor per la lettura delle liste gestione soggetto.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class ListeGestioneSoggettoExecutor implements ServiceExecutor<ListeGestioneSoggetto, ListeGestioneSoggettoResponse> {

	private final SoggettoService soggettoService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param soggettoService il servizio da injettare
	 */
	public ListeGestioneSoggettoExecutor(SoggettoService soggettoService) {
		this.soggettoService = soggettoService;
	}
	
	@Override
	public ListeGestioneSoggettoResponse executeService(ListeGestioneSoggetto req) {
		return soggettoService.listeGestioneSoggetto(req);
	}
	
}