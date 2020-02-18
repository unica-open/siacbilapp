/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;

/**
 * Classe di test per i servizi relativi al soggetto Fin richiamati dal modulo Bil.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 09/10/2014
 *
 */
public class ProvvisorioServiceTest extends BaseProxyServiceTest<ProvvisorioService> {
	
	@Override
	protected String getEndpoint() {
//		return "http://dev-www.ruparpiemonte.it/siacbilser/ProvvisorioService";
		return "http://tst-srv-consip.bilancio.csi.it/siacbilser/ProvvisorioService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaProvvisoriDiCassa() {
		RicercaProvvisoriDiCassa req = new RicercaProvvisoriDiCassa();
		
		req.setDataOra(new Date());
		req.setEnte(getEnteTest());
		req.setNumPagina(1);
		req.setNumRisultatiPerPagina(10);
		req.setRichiedente(getRichiedenteTest());
		
		Bilancio bil = new Bilancio();
		bil.setUid(16);
		req.setBilancio(bil);
		
		ParametroRicercaProvvisorio parametroRicercaProvvisorio = new ParametroRicercaProvvisorio();
		parametroRicercaProvvisorio.setAnnoDa(Integer.valueOf(2014));
//		parametroRicercaProvvisorio.setNumeroDa(1);
//		parametroRicercaProvvisorio.setNumeroA(10);
		parametroRicercaProvvisorio.setImportoDa(new BigDecimal(90));
		parametroRicercaProvvisorio.setImportoDa(new BigDecimal(110));
		parametroRicercaProvvisorio.setTipoProvvisorio(TipoProvvisorioDiCassa.S);
		parametroRicercaProvvisorio.setFlagDaRegolarizzare("S");
		req.setParametroRicercaProvvisorio(parametroRicercaProvvisorio);
		
		RicercaProvvisoriDiCassaResponse res = service.ricercaProvvisoriDiCassa(req);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaProvvisoriDiCassaPerChiave() {
		final String methodName = "ricercaProvvisoriDiCassaPerChiave";
		RicercaProvvisorioDiCassaPerChiave req = new RicercaProvvisorioDiCassaPerChiave();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		req.setBilancio(getBilancio(131, 2017));
		
		RicercaProvvisorioDiCassaK pRicercaProvvisorioK = new RicercaProvvisorioDiCassaK();
		pRicercaProvvisorioK.setAnnoProvvisorioDiCassa(Integer.valueOf(2017));
		pRicercaProvvisorioK.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E);
		req.setpRicercaProvvisorioK(pRicercaProvvisorioK);
		
		Map<Integer, BigDecimal> results = new HashMap<Integer, BigDecimal>();
		
		for(int i = 200, j = 0; j < 50; i--, j++) {
			Integer numero = Integer.valueOf(i);
			pRicercaProvvisorioK.setNumeroProvvisorioDiCassa(numero);
			RicercaProvvisorioDiCassaPerChiaveResponse res = service.ricercaProvvisorioDiCassaPerChiave(req);
			if(!res.hasErrori()) {
				results.put(numero, res.getProvvisorioDiCassa().getImportoDaRegolarizzare());
			} else {
				results.put(numero, null);
			}
			
		}
		
		for(Entry<Integer, BigDecimal> entry : results.entrySet()) {
			log.info(methodName, "2017/" + entry.getKey() + " ==> " + entry.getValue());
		}
	}
	
}
