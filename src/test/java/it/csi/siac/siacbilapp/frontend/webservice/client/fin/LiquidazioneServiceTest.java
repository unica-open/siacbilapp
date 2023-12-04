/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;

/**
 * Classe di test per i servizi relativi al soggetto Fin richiamati dal modulo Bil.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 26/11/2014
 *
 */
public class LiquidazioneServiceTest extends BaseProxyServiceTest<LiquidazioneService> {
	
	@Override
	protected String getEndpoint() {
		//return "http://127.0.0.1:8080/siacbilser/LiquidazioneService";
		return "http://10.136.6.151/siacbilser/LiquidazioneService";
		//return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/LiquidazioneService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaLiquidazionePerChiave() {
		RicercaLiquidazionePerChiave req = new RicercaLiquidazionePerChiave();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		RicercaLiquidazioneK pRicercaLiquidazioneK = new RicercaLiquidazioneK();
		pRicercaLiquidazioneK.setBilancio(getBilancio(143, 2017));
		pRicercaLiquidazioneK.setAnnoEsercizio(Integer.valueOf(2017));
		
		Liquidazione liquidazione = new Liquidazione();
		liquidazione.setAnnoLiquidazione(Integer.valueOf(2017));
		liquidazione.setNumeroLiquidazione(new BigDecimal("1335"));
		pRicercaLiquidazioneK.setAnnoLiquidazione(Integer.valueOf(2016));
		
		pRicercaLiquidazioneK.setAnnoLiquidazione(liquidazione.getAnnoLiquidazione());
		pRicercaLiquidazioneK.setNumeroLiquidazione(liquidazione.getNumeroLiquidazione());
		pRicercaLiquidazioneK.setLiquidazione(liquidazione);
		pRicercaLiquidazioneK.setTipoRicerca(CostantiFin.TIPO_RICERCA_DA_EMISSIONE_ORDINATIVO);
		req.setpRicercaLiquidazioneK(pRicercaLiquidazioneK);
		
		RicercaLiquidazionePerChiaveResponse res = service.ricercaLiquidazionePerChiave(req);
		assertNotNull(res);
	}
	
	/**
	 * Richiesta della liquidazione per chiave su dev
	 */
	@Test
	public void ricercaLiquidazionePerChiaveDev(){
		final String methodName = "ricercaLiquidazionePerChiaveDev";
		RicercaLiquidazionePerChiave reqRL = new RicercaLiquidazionePerChiave();
		reqRL.setRichiedente(getRichiedenteTest());
		reqRL.setEnte(getRichiedenteTest().getAccount().getEnte());
		reqRL.setDataOra(new Date());
		
		Liquidazione liquidazione = new Liquidazione();
		liquidazione.setAnnoLiquidazione(Integer.valueOf(2015));
		liquidazione.setNumeroLiquidazione(new BigDecimal("615"));
		
		RicercaLiquidazioneK ricercaLiquidazioneK = new RicercaLiquidazioneK();
		Bilancio bilancio = getBilancio(16, 2015);
		ricercaLiquidazioneK.setBilancio(bilancio);
		ricercaLiquidazioneK.setAnnoEsercizio(bilancio.getAnno());
		ricercaLiquidazioneK.setAnnoLiquidazione(liquidazione.getAnnoLiquidazione());
		ricercaLiquidazioneK.setNumeroLiquidazione(liquidazione.getNumeroLiquidazione());
		ricercaLiquidazioneK.setLiquidazione(liquidazione);
		ricercaLiquidazioneK.setTipoRicerca(CostantiFin.TIPO_RICERCA_DA_EMISSIONE_ORDINATIVO);
		
		reqRL.setpRicercaLiquidazioneK(ricercaLiquidazioneK);
		RicercaLiquidazionePerChiaveResponse resRL = service.ricercaLiquidazionePerChiave(reqRL);
		log.info(methodName, "Disp a pagare: " + resRL.getLiquidazione().getDisponibilitaPagare());
		
	}

}
