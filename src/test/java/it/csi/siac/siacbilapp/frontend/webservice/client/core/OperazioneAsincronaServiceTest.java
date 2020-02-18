/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.core;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.frontend.webservice.OperazioneAsincronaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetDettaglioOperazioneAsincrona;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetDettaglioOperazioneAsincronaResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetNotificheOperazioneAsincrona;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetNotificheOperazioneAsincronaResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetOperazioneAsincResponse;

/**
 * Test per il CommonService.
 * 
 * @author Marchino Alessandro
 *
 */
public class OperazioneAsincronaServiceTest extends BaseProxyServiceTest<OperazioneAsincronaService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/OperazioneAsincronaService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void getDettaglioOperazioneAsincrona() {
		GetDettaglioOperazioneAsincrona request = new GetDettaglioOperazioneAsincrona();
		
		request.setDataOra(new Date());
		request.setOpAsincId(Integer.valueOf(70));
		request.setParametriPaginazione(getParametriPaginazioneTest());
		request.setRichiedente(getRichiedenteTest());
		
		log.logXmlTypeObject(request, "REQUEST");
		
		GetDettaglioOperazioneAsincronaResponse response = service.getDettaglioOperazioneAsincrona(request);
		log.logXmlTypeObject(response, "RESPONSE");
	}
	
	/**
	 * Test
	 */
	@Test
	public void getNotificheOperazioneAsincrona() {
		GetNotificheOperazioneAsincrona request = new GetNotificheOperazioneAsincrona();
		
		request.setAccountId(Integer.valueOf(1));
		request.setAzioneId(Integer.valueOf(4363));
		request.setDataOra(new Date());
		request.setEnteProprietarioId(Integer.valueOf(1));
		request.setParametriPaginazione(getParametriPaginazioneTest());
		request.setRichiedente(getRichiedenteTest());
		
		log.logXmlTypeObject(request, "REQUEST");
		
		GetNotificheOperazioneAsincronaResponse response = service.getNotificheOperazioneAsincrona(request);
		log.logXmlTypeObject(response, "RESPONSE");
	}
	
	/**
	 * Test
	 */
	@Test
	public void getOperazioneAsinc() {
		GetOperazioneAsinc request = new GetOperazioneAsinc();
		
		request.setDataOra(new Date());
		request.setIdOperazione(Integer.valueOf(22));
		request.setRichiedente(getRichiedenteTest());
		
		log.logXmlTypeObject(request, "REQUEST");
		
		GetOperazioneAsincResponse response = service.getOperazioneAsinc(request);
		log.logXmlTypeObject(response, "RESPONSE");
		
	}
	
}
