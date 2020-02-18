/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaAliquotaIvaExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaAttivitaIvaExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaAttivitaIvaLegateAGruppoAttivitaIvaExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaTipoRegistrazioneIvaExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaValutaExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaAliquotaIvaKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaAttivitaIvaKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaAttivitaIvaLegateAGruppoAttivitaIvaKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaTipoRegistrazioneIvaKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaValutaKeyAdapter;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaLegateAGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaLegateAGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValutaResponse;

/**
 * Cached version of {@link DocumentoIvaService}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 *
 */
@Component
@CachedService
public class DocumentoIvaServiceCachedImpl implements DocumentoIvaService {

	@Autowired
	private transient DocumentoIvaService documentoIvaService;

	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override
	public RicercaTipoRegistrazioneIvaResponse ricercaTipoRegistrazioneIva(RicercaTipoRegistrazioneIva req) {
		return cachedServiceExecutor.executeService(req, new RicercaTipoRegistrazioneIvaExecutor(documentoIvaService), new RicercaTipoRegistrazioneIvaKeyAdapter());
	}

	@Override
	public RicercaAttivitaIvaResponse ricercaAttivitaIva(RicercaAttivitaIva req) {
		return cachedServiceExecutor.executeService(req, new RicercaAttivitaIvaExecutor(documentoIvaService), new RicercaAttivitaIvaKeyAdapter());
	}

	@Override
	public RicercaAttivitaIvaLegateAGruppoAttivitaIvaResponse ricercaAttivitaIvaLegateAGruppoAttivitaIva(RicercaAttivitaIvaLegateAGruppoAttivitaIva req) {
		return cachedServiceExecutor.executeService(req, new RicercaAttivitaIvaLegateAGruppoAttivitaIvaExecutor(documentoIvaService), new RicercaAttivitaIvaLegateAGruppoAttivitaIvaKeyAdapter());
	}

	@Override
	public RicercaAliquotaIvaResponse ricercaAliquotaIva(RicercaAliquotaIva req) {
		return cachedServiceExecutor.executeService(req, new RicercaAliquotaIvaExecutor(documentoIvaService), new RicercaAliquotaIvaKeyAdapter());
	}

	@Override
	public RicercaValutaResponse ricercaValuta(RicercaValuta req) {
		return cachedServiceExecutor.executeService(req, new RicercaValutaExecutor(documentoIvaService), new RicercaValutaKeyAdapter());
	}

}
