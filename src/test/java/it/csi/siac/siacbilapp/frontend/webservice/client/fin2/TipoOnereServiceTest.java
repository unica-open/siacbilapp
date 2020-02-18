/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin2;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfin2ser.frontend.webservice.TipoOnereService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.TipoOnereModelDetail;

/**
 * Test per i servizi del Documento.
 * 
 * @author Marchino Alessandro
 *
 */
public class TipoOnereServiceTest extends BaseProxyServiceTest<TipoOnereService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/TipoOnereService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void dettaglioStoricoTipoOnere() {
		DettaglioStoricoTipoOnere request = new DettaglioStoricoTipoOnere();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		TipoOnere tipoOnere = new TipoOnere();
		tipoOnere.setUid(33);
		request.setTipoOnere(tipoOnere);
		
		DettaglioStoricoTipoOnereResponse response = service.dettaglioStoricoTipoOnere(request);
		log.logXmlTypeObject(response, "RESPONSE");
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioTipoOnere() {
		RicercaDettaglioTipoOnere request = new RicercaDettaglioTipoOnere();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		TipoOnere tipoOnere = new TipoOnere();
		tipoOnere.setUid(83);
		request.setTipoOnere(tipoOnere);
		
		request.setTipoOnereModelDetails(TipoOnereModelDetail.Attr, TipoOnereModelDetail.Causali, TipoOnereModelDetail.Attivita);
		
		RicercaDettaglioTipoOnereResponse response = service.ricercaDettaglioTipoOnere(request);
		logResponse(response);
	}
	
}
