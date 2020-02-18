/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin2;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIvaResponse;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;

/**
 * Test per i servizi del Documento Iva.
 * 
 * @author Marchino Alessandro
 *
 */
public class DocumentoIvaServiceTest extends BaseProxyServiceTest<DocumentoIvaService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/DocumentoIvaService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaAliquotaIvaService() {
		RicercaAliquotaIva request = new RicercaAliquotaIva();
		
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest());
		
		RicercaAliquotaIvaResponse response = service.ricercaAliquotaIva(request);
		log.logXmlTypeObject(response, "Response");
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaAttivitaIvaService() {
		RicercaAttivitaIva request = new RicercaAttivitaIva();
		
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest());
		
		RicercaAttivitaIvaResponse response = service.ricercaAttivitaIva(request);
		log.logXmlTypeObject(response, "Response");
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaTipoRegistrazioneIvaService() {
		RicercaTipoRegistrazioneIva request = new RicercaTipoRegistrazioneIva();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		TipoRegistrazioneIva tri = new TipoRegistrazioneIva();
		tri.setEnte(getEnteTest());
		request.setTipoRegistrazioneIva(tri);
		
		RicercaTipoRegistrazioneIvaResponse response = service.ricercaTipoRegistrazioneIva(request);
		log.logXmlTypeObject(response, "Response");
	}
	
}
