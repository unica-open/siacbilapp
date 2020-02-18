/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.NonCachedService;
//import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiStrutturaAmministrativoContabileByIdExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiStrutturaAmministrativoContabileExecutor;
//import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiStrutturaAmminstrativoContabileByIdKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiStrutturaAmminstrativoContabileKeyAdapter;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;

/**
 * Cached version of {@link ClassificatoreService}.
 * 
 * @author Domenico
 * @version 1.0.0 - 30/09/2014
 *
 */
@Component
@CachedService
@Primary
public class ClassificatoreServiceCachedImpl implements ClassificatoreService {
	
	@Autowired
	@NonCachedService
	private transient ClassificatoreService classificatoreService;
	
	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override
	public LeggiStrutturaAmminstrativoContabileResponse leggiStrutturaAmminstrativoContabile(LeggiStrutturaAmminstrativoContabile req) {
		return cachedServiceExecutor.executeService(req, new LeggiStrutturaAmministrativoContabileExecutor(classificatoreService), new LeggiStrutturaAmminstrativoContabileKeyAdapter());
	}
	
	/*@Override
	public LeggiStrutturaAmminstrativoContabileResponse leggiStrutturaAmminstrativoContabileById(LeggiStrutturaAmminstrativoContabile req) {
		return cachedServiceExecutor.executeService(req, new LeggiStrutturaAmministrativoContabileByIdExecutor(classificatoreService), new LeggiStrutturaAmminstrativoContabileByIdKeyAdapter());
	}*/

}
