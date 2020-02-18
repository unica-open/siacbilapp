/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.BilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;

/**
 * Classe di test per i serviz&icirc; del Bilancio.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 09/01/2014
 *
 */
public class BilancioServiceTest extends BaseProxyServiceTest<BilancioService> {
	
	@Override
	protected String getEndpoint() {
		// return "http://dev-www.ruparpiemonte.it/siacbilser/BilancioService";
//		return "http://localhost:8080/siacbilser/BilancioService";
		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/BilancioService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void bilancio() {
		RicercaDettaglioBilancio request = new RicercaDettaglioBilancio();
		request.setChiaveBilancio(Integer.valueOf(99));
		request.setDataOra(new Date());
		//request.setRichiedente(getRichiedenteTest());
		request.setRichiedente(getRichiedenteTest("ED878A58A9BB5C1E", 285, 29));
		
		RicercaDettaglioBilancioResponse response = service.ricercaDettaglioBilancio(request);
		assertNotNull(response);
	}
}
