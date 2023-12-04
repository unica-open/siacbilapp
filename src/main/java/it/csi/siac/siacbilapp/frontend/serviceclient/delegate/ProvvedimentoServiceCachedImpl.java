/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaPuntualeProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaPuntualeProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaSinteticaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaSinteticaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.VerificaAnnullabilitaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.VerificaAnnullabilitaProvvedimentoResponse;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.TipiProvvedimentoExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.TipiProvvedimentoKeyAdapter;

/**
 * Cached version of {@link ProvvedimentoService}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 *
 */
@Component
@CachedService
public class ProvvedimentoServiceCachedImpl implements ProvvedimentoService {

	@Autowired
	private transient ProvvedimentoService provvedimentoService;

	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override
	public InserisceProvvedimentoResponse inserisceProvvedimento(InserisceProvvedimento req) {
		return provvedimentoService.inserisceProvvedimento(req);
	}

	@Override
	public AggiornaProvvedimentoResponse aggiornaProvvedimento(AggiornaProvvedimento req) {
		return provvedimentoService.aggiornaProvvedimento(req);
	}

	@Override
	public RicercaProvvedimentoResponse ricercaProvvedimento(RicercaProvvedimento req) {
		return provvedimentoService.ricercaProvvedimento(req);
	}

	@Override
	public VerificaAnnullabilitaProvvedimentoResponse verificaAnnullabilitaProvvedimento(VerificaAnnullabilitaProvvedimento req) {
		return provvedimentoService.verificaAnnullabilitaProvvedimento(req);
	}

	@Override
	public TipiProvvedimentoResponse getTipiProvvedimento(TipiProvvedimento req) {
		return cachedServiceExecutor.executeService(req, new TipiProvvedimentoExecutor(provvedimentoService), new TipiProvvedimentoKeyAdapter());
	}

	@Override
	public RicercaSinteticaProvvedimentoResponse ricercaSinteticaProvvedimento(
			RicercaSinteticaProvvedimento req) {
		return provvedimentoService.ricercaSinteticaProvvedimento(req);
	}

	@Override
	public RicercaProvvedimentoResponse ricercaProvvedimentoConParametri(RicercaProvvedimento parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RicercaPuntualeProvvedimentoResponse ricercaPuntualeProvvedimento(RicercaPuntualeProvvedimento parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
