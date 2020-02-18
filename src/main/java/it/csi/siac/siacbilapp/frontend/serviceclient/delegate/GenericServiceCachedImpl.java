/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.ListaComunePerNomeExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.ListaComuniExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.ListaSedimeExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.ListeExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.ListaComunePerNomeKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.ListaComuniKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.ListaSedimeKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.ListeKeyAdapter;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.CaricaDatiVisibilitaSacCapitolo;
import it.csi.siac.siacfinser.frontend.webservice.msg.CaricaDatiVisibilitaSacCapitoloResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNome;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNomeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSedime;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSedimeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccountPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccountPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaGruppoTipoAccreditoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaGruppoTipoAccreditoPerChiaveResponse;

/**
 * Cached version of {@link GenericService}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 *
 */
@Component
@CachedService
public class GenericServiceCachedImpl implements GenericService {

	@Autowired
	private transient GenericService genericService;

	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override
	public ListaComuniResponse cercaComuni(ListaComuni req) {
		return cachedServiceExecutor.executeService(req, new ListaComuniExecutor(genericService), new ListaComuniKeyAdapter());
	}

	@Override
	public EsistenzaProgettoResponse cercaProgetto(EsistenzaProgetto req) {
		return genericService.cercaProgetto(req);
	}

	@Override
	public ListaComunePerNomeResponse findComunePerNome(ListaComunePerNome req) {
		return cachedServiceExecutor.executeService(req, new ListaComunePerNomeExecutor(genericService), new ListaComunePerNomeKeyAdapter());
	}

	@Override
	public RicercaGruppoTipoAccreditoPerChiaveResponse findGruppoTipoAccreditoPerPk(RicercaGruppoTipoAccreditoPerChiave req) {
		return genericService.findGruppoTipoAccreditoPerPk(req);
	}

	@Override
	public ListaSedimeResponse listaSedime(ListaSedime req) {
		return cachedServiceExecutor.executeService(req, new ListaSedimeExecutor(genericService), new ListaSedimeKeyAdapter());
	}

	@Override
	public ListeResponse liste(Liste req) {
		return cachedServiceExecutor.executeService(req, new ListeExecutor(genericService), new ListeKeyAdapter());
	}

	@Override
	public RicercaAccountPerChiaveResponse ricercaAccountPerChiave(RicercaAccountPerChiave req) {
		return genericService.ricercaAccountPerChiave(req);
	}

	@Override
	public CaricaDatiVisibilitaSacCapitoloResponse caricaDatiVisibilitaSacCapitolo(
			CaricaDatiVisibilitaSacCapitolo request) {
		// TODO Auto-generated method stub
		return null;
	}

}
