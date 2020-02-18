/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin;

import java.util.Date;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;

/**
 * Test per il CommonService.
 * 
 * @author Marchino Alessandro
 *
 */
public class GenericServiceTest extends BaseProxyServiceTest<GenericService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/GenericService";
	}
	
	/**
	 * Test Liste
	 */
	@Test
	public void liste() {
		Liste request = new Liste();
		request.setRichiedente(getRichiedenteTest(getEnteTest()));
		//request.setRichiedente(getRichiedenteTest());
		//request.setEnte(getEnteTest());
		request.setDataOra(new Date());
		
		request.setEnte(getEnteTest());
		request.setTipi(new TipiLista[]{/*TipiLista.NAZIONI, TipiLista.CONTO_TESORERIA, TipiLista.TIPO_IMPEGNO*/ TipiLista.DISTINTA_ENTRATA});
		
		log.logXmlTypeObject(request, "Request");
		
		ListeResponse response = service.liste(request);

		log.logXmlTypeObject(response, "Response");
	}
	
	
	/**
	 * Test Liste
	 */
	@Test
	public void listeVarie() {
		Liste request = new Liste();
		request.setRichiedente(getRichiedenteTest(getEnteTest()));
		//request.setRichiedente(getRichiedenteTest());
		//request.setEnte(getEnteTest());
		request.setDataOra(new Date());
		
		request.setEnte(getEnteTest());
		request.setTipi(new TipiLista[]{TipiLista.NAZIONI});
		
		log.logXmlTypeObject(request, "Request");
		
		ListeResponse response = service.liste(request);

		log.logXmlTypeObject(response, "Response");
		
//		try {
//			Assert.assertTrue(!response.isFallimento());
//			if(response.getClassiSoggetto() != null) {
//				for(CodificaExtFin s : response.getClassiSoggetto()){
//					log.debug(methodName, s);
//				}
//			}
//		} catch(Throwable t) {
//			t.printStackTrace();
//			fail(t.getMessage());
//		}
	}
	
	/**
	 * Test
	 */
	@Test
	public void cercaComuni() {
		ListaComuni request = new ListaComuni();
		request.setRichiedente(getRichiedenteTest());
		request.setDataOra(new Date());
		request.setDescrizioneComune("Riv");
		request.setIdStato("1");
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		ListaComuniResponse response = service.cercaComuni(request);
		stopWatch.stop();
		
		logResponse(response);
		
		log.debug("testCercaComuni", stopWatch.toString());
	}
	
	
	
}
