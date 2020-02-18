/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceExecutor;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;

/**
 * Executor per la lettura della struttura amministrativo contabile.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 17/03/2016
 *
 */
public class LeggiStrutturaAmministrativoContabileExecutor implements ServiceExecutor<LeggiStrutturaAmminstrativoContabile, LeggiStrutturaAmminstrativoContabileResponse> {

	private final ClassificatoreService classificatoreService;
	
	/**
	 * Costruttore di injezione.
	 * 
	 * @param classificatoreService il servizio da injettare
	 */
	public LeggiStrutturaAmministrativoContabileExecutor(ClassificatoreService classificatoreService) {
		this.classificatoreService = classificatoreService;
	}
	
	@Override
	public LeggiStrutturaAmminstrativoContabileResponse executeService(LeggiStrutturaAmminstrativoContabile req) {
		return classificatoreService.leggiStrutturaAmminstrativoContabile(req);
	}
	
}