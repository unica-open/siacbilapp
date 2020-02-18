/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.frontend.webservice.AttoDiLeggeService;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLeggeResponse;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaTipiAttoDiLeggeExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaTipiAttoDiLeggeKeyAdapter;

/**
 * Cached version of {@link AttoDiLeggeService}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 *
 */
@Component
@CachedService
public class AttoDiLeggeServiceCachedImpl implements AttoDiLeggeService {

	@Autowired
	private transient AttoDiLeggeService attoDiLeggeService;

	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override
	public InserisceAttoDiLeggeResponse inserisceAttoDiLegge(InserisceAttoDiLegge req) {
		return attoDiLeggeService.inserisceAttoDiLegge(req);
	}

	@Override
	public AggiornaAttoDiLeggeResponse aggiornaAttoDiLegge(AggiornaAttoDiLegge req) {
		return attoDiLeggeService.aggiornaAttoDiLegge(req);
	}

	@Override
	public RicercaAttoDiLeggeResponse ricercaAttoDiLegge(RicercaAttoDiLegge req) {
		return attoDiLeggeService.ricercaAttoDiLegge(req);
	}

	@Override
	public RicercaAttoDiLeggeResponse ricercaPuntualeAttoDiLegge(RicercaAttoDiLegge req) {
		return attoDiLeggeService.ricercaAttoDiLegge(req);
	}

	@Override
	public CancellaAttoDiLeggeResponse cancellaAttoDiLegge(CancellaAttoDiLegge req) {
		return attoDiLeggeService.cancellaAttoDiLegge(req);
	}

	@Override
	public RicercaTipiAttoDiLeggeResponse getTipiAttoLegge(RicercaTipiAttoDiLegge req) {
		return cachedServiceExecutor.executeService(req, new RicercaTipiAttoDiLeggeExecutor(attoDiLeggeService), new RicercaTipiAttoDiLeggeKeyAdapter());
	}

}
