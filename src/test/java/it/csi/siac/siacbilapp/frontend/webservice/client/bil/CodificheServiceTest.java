/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacgenser.model.Evento;

/**
 * Classe di test per i serviz&icirc; del Bilancio.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 09/01/2014
 *
 */
public class CodificheServiceTest extends BaseProxyServiceTest<CodificheService> {
	
	@Override
	protected String getEndpoint() {
		return "http://10.136.6.151/siacbilser/CodificheService";
	}
	
	/**
	 * Test
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void ricercaCodifiche() {
		RicercaCodifiche request = new RicercaCodifiche();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		//request.addTipiCodifica(TipoGiustificativo.class, MezziDiTrasporto.class, TipoAtto.class);
		request.addTipiCodifica(Evento.class);
		logRequest(request);
		
		RicercaCodificheResponse response = service.ricercaCodifiche(request);
		logResponse(response);
	}
	
}
