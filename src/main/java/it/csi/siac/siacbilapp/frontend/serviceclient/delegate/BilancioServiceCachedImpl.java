/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaDettaglioBilancioExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaDettaglioBilancioKeyAdapter;
import it.csi.siac.siacbilser.frontend.webservice.BilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;

/**
 * Cached version of {@link BilancioService}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/10/2014
 *
 */
@Component
@CachedService
public class BilancioServiceCachedImpl implements BilancioService {

	@Autowired
	private transient BilancioService bilancioService;

	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override
	public RicercaDettaglioBilancioResponse ricercaDettaglioBilancio(RicercaDettaglioBilancio req) {
		return cachedServiceExecutor.executeService(req, new RicercaDettaglioBilancioExecutor(bilancioService), new RicercaDettaglioBilancioKeyAdapter());
	}

	@Override
	public RicercaAttributiBilancioResponse ricercaAttributiBilancio(RicercaAttributiBilancio parameters) {
		return bilancioService.ricercaAttributiBilancio(parameters);
	}

	@Override
	public AggiornaAttributiBilancioResponse aggiornaAttributiBilancio(AggiornaAttributiBilancio parameters) {
		return bilancioService.aggiornaAttributiBilancio(parameters);
	}

}
