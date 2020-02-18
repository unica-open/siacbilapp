/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaTipoVariazioneExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaTipoVariazioneKeyAdapter;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.GestisciDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.GestisciDettaglioVariazioneComponenteImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneCodificheResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaExcelVariazioneDiBilancio;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaExcelVariazioneDiBilancioResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;

/**
 * Cached version of {@link VariazioneDiBilancioService}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 *
 */
@Component
@CachedService
public class VariazioneDiBilancioServiceCachedImpl implements VariazioneDiBilancioService {

	@Autowired
	private transient VariazioneDiBilancioService variazioneDiBilancioService;

	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override
	@Deprecated
	public it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEBResponse inserisceStornoUEBEntrata(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEB req) {
		return variazioneDiBilancioService.inserisceStornoUEBEntrata(req);
	}

	@Override
	@Deprecated
	public it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEBResponse inserisceStornoUEBUscita(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEB req) {
		return variazioneDiBilancioService.inserisceStornoUEBEntrata(req);
	}

	@Override
	@Deprecated
	public it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEBResponse aggiornaStornoUEBEntrata(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEB req) {
		return variazioneDiBilancioService.aggiornaStornoUEBEntrata(req);
	}

	@Override
	@Deprecated
	public it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEBResponse aggiornaStornoUEBUscita(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEB req) {
		return variazioneDiBilancioService.aggiornaStornoUEBEntrata(req);
	}

	@Override
	@Deprecated
	public it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStornoUEBResponse ricercaStornoUEB(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStornoUEB req) {
		return variazioneDiBilancioService.ricercaStornoUEB(req);
	}

	@Override
	public RicercaVariazioneBilancioResponse ricercaVariazioneBilancio(RicercaVariazioneBilancio req) {
		return variazioneDiBilancioService.ricercaVariazioneBilancio(req);
	}

	@Override
	public InserisceVariazioneCodificheResponse inserisceVariazioneCodifiche(InserisceVariazioneCodifiche req) {
		return variazioneDiBilancioService.inserisceVariazioneCodifiche(req);
	}

	@Override
	public AggiornaVariazioneCodificheResponse aggiornaVariazioneCodifiche(AggiornaVariazioneCodifiche req) {
		return variazioneDiBilancioService.aggiornaVariazioneCodifiche(req);
	}

	@Override
	public DefinisceVariazioneCodificheResponse definisceVariazioneCodifiche(DefinisceVariazioneCodifiche req) {
		return variazioneDiBilancioService.definisceVariazioneCodifiche(req);
	}

	@Override
	public RicercaVariazioneCodificheResponse ricercaVariazioneCodifiche(RicercaVariazioneCodifiche req) {
		return variazioneDiBilancioService.ricercaVariazioneCodifiche(req);
	}

	@Override
	public RicercaDettaglioVariazioneCodificheResponse ricercaDettaglioVariazioneCodifiche(RicercaDettaglioVariazioneCodifiche req) {
		return variazioneDiBilancioService.ricercaDettaglioVariazioneCodifiche(req);
	}

	@Override
	public RicercaTipoVariazioneResponse ricercaTipoVariazione(RicercaTipoVariazione req) {
		return cachedServiceExecutor.executeService(req, new RicercaTipoVariazioneExecutor(variazioneDiBilancioService), new RicercaTipoVariazioneKeyAdapter());
	}

	@Override
	public InserisciDettaglioVariazioneImportoCapitoloResponse inserisciDettaglioVariazioneImportoCapitolo(InserisciDettaglioVariazioneImportoCapitolo parameters) {
		return variazioneDiBilancioService.inserisciDettaglioVariazioneImportoCapitolo(parameters);
	}

	@Override
	public AggiornaDettaglioVariazioneImportoCapitoloResponse aggiornaDettaglioVariazioneImportoCapitolo(AggiornaDettaglioVariazioneImportoCapitolo parameters) {
		return variazioneDiBilancioService.aggiornaDettaglioVariazioneImportoCapitolo(parameters);
	}

	@Override
	public EliminaDettaglioVariazioneImportoCapitoloResponse eliminaDettaglioVariazioneImportoCapitolo(EliminaDettaglioVariazioneImportoCapitolo parameters) {
		return variazioneDiBilancioService.eliminaDettaglioVariazioneImportoCapitolo(parameters);
	}

	@Override
	public AggiornaAnagraficaVariazioneBilancioResponse aggiornaAnagraficaVariazioneBilancio(AggiornaAnagraficaVariazioneBilancio parameters) {
		return variazioneDiBilancioService.aggiornaAnagraficaVariazioneBilancio(parameters);
	}

	@Override
	public AsyncServiceResponse aggiornaAnagraficaVariazioneBilancioAsync(AsyncServiceRequestWrapper<AggiornaAnagraficaVariazioneBilancio> parameters) {
		return variazioneDiBilancioService.aggiornaAnagraficaVariazioneBilancioAsync(parameters);
	}

	@Override
	public InserisceAnagraficaVariazioneBilancioResponse inserisceAnagraficaVariazioneBilancio(InserisceAnagraficaVariazioneBilancio parameters) {
		return variazioneDiBilancioService.inserisceAnagraficaVariazioneBilancio(parameters);
	}

	@Override
	public DefinisceAnagraficaVariazioneBilancioResponse definisceAnagraficaVariazioneBilancio(DefinisceAnagraficaVariazioneBilancio parameters) {
		return variazioneDiBilancioService.definisceAnagraficaVariazioneBilancio(parameters);
	}

	@Override
	public AsyncServiceResponse definisceAnagraficaVariazioneBilancioAsync(AsyncServiceRequestWrapper<DefinisceAnagraficaVariazioneBilancio> parameters) {
		return variazioneDiBilancioService.definisceAnagraficaVariazioneBilancioAsync(parameters);
	}

	@Override
	public RicercaDettaglioAnagraficaVariazioneBilancioResponse ricercaDettaglioAnagraficaVariazioneBilancio(RicercaDettaglioAnagraficaVariazioneBilancio parameters) {
		return variazioneDiBilancioService.ricercaDettaglioAnagraficaVariazioneBilancio(parameters);
	}

	@Override
	public RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse ricercaDettagliVariazioneImportoCapitoloNellaVariazione(RicercaDettagliVariazioneImportoCapitoloNellaVariazione parameters) {
		return variazioneDiBilancioService.ricercaDettagliVariazioneImportoCapitoloNellaVariazione(parameters);
	}

	@Override
	public RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse ricercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione(RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione parameters) {
		return variazioneDiBilancioService.ricercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione(parameters);
	}

	@Override
	public StampaExcelVariazioneDiBilancioResponse stampaExcelVariazioneDiBilancio(StampaExcelVariazioneDiBilancio parameters) {
		return  variazioneDiBilancioService.stampaExcelVariazioneDiBilancio(parameters);
	}

	@Override
	public GestisciDettaglioVariazioneComponenteImportoCapitoloResponse gestisciDettaglioVariazioneComponenteImportoCapitolo(GestisciDettaglioVariazioneComponenteImportoCapitolo parameters) {
		return variazioneDiBilancioService.gestisciDettaglioVariazioneComponenteImportoCapitolo(parameters);
	}

	@Override
	public RicercaDettaglioVariazioneComponenteImportoCapitoloResponse ricercaDettaglioVariazioneComponenteImportoCapitolo(RicercaDettaglioVariazioneComponenteImportoCapitolo parameters) {
		return variazioneDiBilancioService.ricercaDettaglioVariazioneComponenteImportoCapitolo(parameters);
	}

	@Override
	public RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse ricercaDettagloVariazionePrimoCapitoloNellaVariazione(
			RicercaDettagliVariazioneImportoCapitoloNellaVariazione parameters) {
		// TODO Auto-generated method stub
		return variazioneDiBilancioService.ricercaDettagloVariazionePrimoCapitoloNellaVariazione(parameters);
	}

}
