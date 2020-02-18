/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;

/**
 * Classe di test per i serviz&icirc; di Variazione di Bilancio.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 09/01/2014
 *
 */
public class VariazioneBilancioServiceTest extends BaseProxyServiceTest<VariazioneDiBilancioService> {
	
	@Override
	protected String getEndpoint() {
		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/VariazioneDiBilancioService";
		// return "http://tst-www.ruparpiemonte.it/siacbilser/VariazioneDiBilancioService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void tipiVariazione() {
		final String methodName = "tipiVariazione";
		RicercaTipoVariazione request = new RicercaTipoVariazione();
		request.setEnte(getEnteTest(2));
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		log.info(methodName, ToStringBuilder.reflectionToString(request, ToStringStyle.MULTI_LINE_STYLE));
		
		RicercaTipoVariazioneResponse response = service.ricercaTipoVariazione(request);
		
		try {
			log.info(methodName, ToStringBuilder.reflectionToString(response, ToStringStyle.MULTI_LINE_STYLE));
			Assert.assertNotNull(response.getElencoTipiVariazione());
		} catch(Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioVariazioneCodifiche() {
		RicercaDettaglioVariazioneCodifiche request = new RicercaDettaglioVariazioneCodifiche();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		request.setUidVariazione(127);
		
		RicercaDettaglioVariazioneCodificheResponse response = service.ricercaDettaglioVariazioneCodifiche(request);
		logResponse(response);
	}
	
	/**
	 * Definizione della variazione di codifiche
	 */
	@Test
	public void definisceVariazioneCodifiche(){
		
		DefinisceVariazioneCodifiche request = new DefinisceVariazioneCodifiche();
		request.setIdAttivita("VariazioneDiBilancio--1.0--260--VariazioneDiBilancio_DefinizioneDellaVariazione--it99cc205d-1ea0-4a10-a134-fedf864f944e--mainActivityInstance--noLoop");	
		request.setRichiedente(getRichiedenteTest());
		
		VariazioneCodificaCapitolo variazioneCodificaCapitolo = new VariazioneCodificaCapitolo();
		variazioneCodificaCapitolo.setEnte(getEnteTest());
		variazioneCodificaCapitolo.setBilancio(getBilancio(6, 2015));
		variazioneCodificaCapitolo.setUid(0);
		request.setVariazioneCodificaCapitolo(variazioneCodificaCapitolo);
		
		DefinisceVariazioneCodificheResponse response = service.definisceVariazioneCodifiche(request);
		assertNotNull(response);
		
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioAnagraficaVariazioneBilancio() {
		RicercaDettaglioAnagraficaVariazioneBilancio request = new RicercaDettaglioAnagraficaVariazioneBilancio();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		request.setUidVariazione(422);
		
		RicercaDettaglioAnagraficaVariazioneBilancioResponse response = service.ricercaDettaglioAnagraficaVariazioneBilancio(request);
		logResponse(response);
	}
	
	/**
	 * Ricerca per la variazione di bilancio
	 */
	@Test
	public void ricercaVariazioneBilancio() {
		RicercaVariazioneBilancio request = new RicercaVariazioneBilancio();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		request.setParametriPaginazione(getParametriPaginazioneTest());
		
		VariazioneImportoCapitolo vic = new VariazioneImportoCapitolo();
		request.setVariazioneImportoCapitolo(vic);
		
		vic.setBilancio(getBilancio(16, 2015));
		vic.setEnte(getEnteTest());
		vic.setUid(422);
		
//		// Numero della variazione
//		if(numeroVariazione != null && numeroVariazione.intValue() != 0) {
//			utility.setNumero(numeroVariazione);
//		}
//		if(applicazioneVariazione!=null){
//			// TODO: applicazione?
////			utility.setApplicazione(applicazioneVariazione);
//		}
//		if(descrizioneVariazione!=null && !descrizioneVariazione.isEmpty()){
//			utility.setDescrizione(descrizioneVariazione);
//		}
//		if(tipoVariazione!=null){
//			utility.setTipoVariazione(tipoVariazione);
//		}
//		if(statoVariazione!=null){
//			utility.setStatoOperativoVariazioneDiBilancio(statoVariazione);
//		}
//		
//		if(uidProvvedimento!=null && uidProvvedimento.intValue() != 0){
//			AttoAmministrativo attoAmministrativoDaInjettare = new AttoAmministrativo();
//			attoAmministrativoDaInjettare.setUid(uidProvvedimento);
//			utility.setAttoAmministrativo(attoAmministrativoDaInjettare);
//		}
//			AttoAmministrativo attoAmministrativoVariazioneDiBilancioDaInjettare = new AttoAmministrativo();
//			attoAmministrativoVariazioneDiBilancioDaInjettare.setUid(37888);
//			utility.setAttoAmministrativoVariazioneBilancio(attoAmministrativoVariazioneDiBilancioDaInjettare);
//			request.setVariazioneImportoCapitolo(utility);
		
		RicercaVariazioneBilancioResponse response = service.ricercaVariazioneBilancio(request);
		logResponse(response);
	}

	/**
	 * Ricerca dettagli variazione importo capitolo nella variazione
	 */
	@Test
	public void ricercaDettagliVariazioneImportoCapitoloNellaVariazione() {
		RicercaDettagliVariazioneImportoCapitoloNellaVariazione req = new RicercaDettagliVariazioneImportoCapitoloNellaVariazione();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setUidVariazione(419);
		
		RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse res = service.ricercaDettagliVariazioneImportoCapitoloNellaVariazione(req);
		assertNotNull(res);
	}
	
	/**
	 * Ricerca del singolo dettaglio variazione importo capitolo nella variazione
	 */
	@Test
	public void ricercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione req = new RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("forn2", "coal"));
		req.setUidVariazione(573);
		req.setBilancio(getBilancio(138, 2017));
		req.setCapitolo(create(CapitoloUscitaPrevisione.class, 0));
		
		req.getCapitolo().setAnnoCapitolo(2017);
		req.getCapitolo().setNumeroCapitolo(101010304);
		req.getCapitolo().setNumeroArticolo(0);
		req.getCapitolo().setNumeroUEB(1);
		
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse res = service.ricercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione(req);
		assertNotNull(res);
	}
}
