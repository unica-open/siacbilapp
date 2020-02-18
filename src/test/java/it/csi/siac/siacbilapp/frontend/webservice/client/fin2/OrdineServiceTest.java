/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin2;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfin2ser.frontend.webservice.OrdineService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumentoResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * Test per i servizi dell'Ordine.
 * 
 * @author Marchino Alessandro
 *
 */
public class OrdineServiceTest extends BaseProxyServiceTest<OrdineService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/OrdineService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaOrdiniDocumento() {
		RicercaOrdiniDocumento req = new RicercaOrdiniDocumento();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		
		DocumentoSpesa documento = new DocumentoSpesa();
		documento.setUid(540);
		req.setDocumento(documento);
		
		RicercaOrdiniDocumentoResponse res = service.ricercaOrdiniDocumento(req);
		logResponse(res);
	}
}
