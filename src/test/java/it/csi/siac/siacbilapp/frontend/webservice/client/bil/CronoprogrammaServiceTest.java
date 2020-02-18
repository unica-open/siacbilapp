/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.ProgettoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogrammaResponse;
import it.csi.siac.siacbilser.model.Cronoprogramma;

/**
 * Classe di test per i serviz&icirc; del Cronoprogramma.
 * 
 * @author Marchino Alessandro
 *
 */
public class CronoprogrammaServiceTest extends BaseProxyServiceTest<ProgettoService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/ProgettoService";
	}
	
	/**
	 * Test di ricerca dettaglio
	 */
	public void ricercaDettaglioCronoprogramma() {
		RicercaDettaglioCronoprogramma req = new RicercaDettaglioCronoprogramma();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		
		Cronoprogramma cronoprogramma = new Cronoprogramma();
		cronoprogramma.setUid(41);
		req.setCronoprogramma(cronoprogramma);
		
		RicercaDettaglioCronoprogrammaResponse res = service.ricercaDettaglioCronoprogramma(req);
		assertNotNull(res);
	}
	
}
