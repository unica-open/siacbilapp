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
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiClassificatoriBilByIdFiglioExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiClassificatoriBilByIdPadreExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiClassificatoriByRelazioneExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiClassificatoriByTipoElementoBilExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiClassificatoriGenericiByTipoElementoBilExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiTreePianoDeiContiExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiTreeSiopeEntrataExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiTreeSiopeSpesaExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaPuntualeClassificatoreExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaTipoClassificatoreExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaTipoClassificatoreGenericoExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiClassificatoriBilByIdFiglioKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiClassificatoriBilByIdPadreKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiClassificatoriByRelazioneKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiClassificatoriByTipoElementoBilKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiClassificatoriGenericiByTipoElementoBilKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiTreePianoDeiContiKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiTreeSiopeKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaPuntualeClassificatoreKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaTipoClassificatoreGenericoKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaTipoClassificatoreKeyAdapter;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaClassificatoriERestituisciSeSingolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaClassificatoriERestituisciSeSingoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglio;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatori;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatoriResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiopeResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeClassificatoreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatoreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenerico;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenericoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreResponse;

/**
 * Cached version of {@link ClassificatoreBilService}.
 * 
 * @author Domenico
 * @version 1.0.0 - 30/09/2014
 *
 */
@Component
@CachedService
@Primary
public class ClassificatoreBilServiceCachedImpl implements ClassificatoreBilService {
	
	@Autowired
	@NonCachedService
	private transient ClassificatoreBilService classificatoreBilService;
	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;
	
	@Override
	public LeggiClassificatoriBilByIdPadreResponse leggiClassificatoriByIdPadre(LeggiClassificatoriBilByIdPadre req) {
		return cachedServiceExecutor.executeService(req, new LeggiClassificatoriBilByIdPadreExecutor(classificatoreBilService), new LeggiClassificatoriBilByIdPadreKeyAdapter());
	}

	@Override
	public LeggiClassificatoriByRelazioneResponse leggiClassificatoriByRelazione(LeggiClassificatoriByRelazione req) {
		return cachedServiceExecutor.executeService(req, new LeggiClassificatoriByRelazioneExecutor(classificatoreBilService), new LeggiClassificatoriByRelazioneKeyAdapter());
	}

	@Override
	public LeggiClassificatoriByTipoElementoBilResponse leggiClassificatoriByTipoElementoBil(LeggiClassificatoriByTipoElementoBil req) {
		return cachedServiceExecutor.executeService(req, new LeggiClassificatoriByTipoElementoBilExecutor(classificatoreBilService), new LeggiClassificatoriByTipoElementoBilKeyAdapter());
	}

	@Override
	public LeggiClassificatoriGenericiByTipoElementoBilResponse leggiClassificatoriGenericiByTipoElementoBil(LeggiClassificatoriGenericiByTipoElementoBil req) {
		return cachedServiceExecutor.executeService(req, new LeggiClassificatoriGenericiByTipoElementoBilExecutor(classificatoreBilService), new LeggiClassificatoriGenericiByTipoElementoBilKeyAdapter());
	}

	@Override
	public LeggiTreePianoDeiContiResponse leggiTreePianoDeiConti(LeggiTreePianoDeiConti req) {
		return cachedServiceExecutor.executeService(req, new LeggiTreePianoDeiContiExecutor(classificatoreBilService), new LeggiTreePianoDeiContiKeyAdapter());
	}

	@Override
	public LeggiTreeSiopeResponse leggiTreeSiopeEntrata(LeggiTreeSiope req) {
		return cachedServiceExecutor.executeService(req, new LeggiTreeSiopeEntrataExecutor(classificatoreBilService), new LeggiTreeSiopeKeyAdapter());
	}

	@Override
	public LeggiTreeSiopeResponse leggiTreeSiopeSpesa(LeggiTreeSiope req) {
		return cachedServiceExecutor.executeService(req, new LeggiTreeSiopeSpesaExecutor(classificatoreBilService), new LeggiTreeSiopeKeyAdapter());
	}

	@Override
	public LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse leggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno req) {
		return classificatoreBilService.leggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(req);
	}

	@Override
	public LeggiElementoPianoDeiContiByCodiceAndAnnoResponse leggiElementoPianoDeiContiByCodiceAndAnno(LeggiElementoPianoDeiContiByCodiceAndAnno req) {
		return classificatoreBilService.leggiElementoPianoDeiContiByCodiceAndAnno(req);
	}

	@Override
	public RicercaSinteticaClassificatoreResponse ricercaSinteticaClassificatore(RicercaSinteticaClassificatore request) {
		return classificatoreBilService.ricercaSinteticaClassificatore(request);
	}

	@Override
	public ContaClassificatoriERestituisciSeSingoloResponse contaClassificatoriERestituisciSeSingolo(ContaClassificatoriERestituisciSeSingolo request) {
		return classificatoreBilService.contaClassificatoriERestituisciSeSingolo(request);
	}

	@Override
	public LeggiClassificatoriBilByIdFiglioResponse leggiClassificatoriByIdFiglio(LeggiClassificatoriBilByIdFiglio req) {
		return cachedServiceExecutor.executeService(req, new LeggiClassificatoriBilByIdFiglioExecutor(classificatoreBilService), new LeggiClassificatoriBilByIdFiglioKeyAdapter());
	}

	@Override
	public LeggiClassificatoriByTipologieClassificatoriResponse leggiClassificatoriByTipologieClassificatori(LeggiClassificatoriByTipologieClassificatori request) {
		return classificatoreBilService.leggiClassificatoriByTipologieClassificatori(request);
	}

	@Override
	public RicercaPuntualeClassificatoreResponse ricercaPuntualeClassificatore(RicercaPuntualeClassificatore req) {
		return cachedServiceExecutor.executeService(req, new RicercaPuntualeClassificatoreExecutor(classificatoreBilService), new RicercaPuntualeClassificatoreKeyAdapter());
	}

	@Override
	public RicercaTipoClassificatoreResponse ricercaTipoClassificatore(RicercaTipoClassificatore req) {
		return cachedServiceExecutor.executeService(req, new RicercaTipoClassificatoreExecutor(classificatoreBilService), new RicercaTipoClassificatoreKeyAdapter());
	}

	@Override
	public RicercaTipoClassificatoreGenericoResponse ricercaTipoClassificatoreGenerico(RicercaTipoClassificatoreGenerico req) {
		return cachedServiceExecutor.executeService(req, new RicercaTipoClassificatoreGenericoExecutor(classificatoreBilService), new RicercaTipoClassificatoreGenericoKeyAdapter());
	}

}
