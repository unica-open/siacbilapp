/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fel;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.sirfelser.frontend.webservice.FatturaElettronicaService;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronicaResponse;
import it.csi.siac.sirfelser.model.FatturaFEL;

/**
 * Test per il FatturaElettronicaService.
 * 
 * @author Marchino Alessandro
 *
 */
public class FatturaElettronicaServiceTest extends BaseProxyServiceTest<FatturaElettronicaService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/FatturaElettronicaService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioFatturaElettronica() {
		RicercaDettaglioFatturaElettronica request = new RicercaDettaglioFatturaElettronica();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		FatturaFEL fatturaFEL = new FatturaFEL();
		fatturaFEL.setEnte(getEnteTest());
		fatturaFEL.setIdFattura(Integer.valueOf(20167));
		request.setFatturaFEL(fatturaFEL);
		
		RicercaDettaglioFatturaElettronicaResponse response = service.ricercaDettaglioFatturaElettronica(request);
		log.logXmlTypeObject(response, "RESPONSE");
		
	}
	
}
