/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin2;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;

/**
 * Test per i servizi del Documento Iva di spesa.
 * 
 * @author Marchino Alessandro
 *
 */
public class DocumentoIvaSpesaServiceTest extends BaseProxyServiceTest<DocumentoIvaSpesaService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/DocumentoIvaSpesaService";
	}
	
	/**
	 * Ricerca di dettaglio del subdocumento iva di spesa
	 */
	@Test
	public void ricercaDettaglioSubdocumentoIvaSpesa() {
		RicercaDettaglioSubdocumentoIvaSpesa req = new RicercaDettaglioSubdocumentoIvaSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		
		SubdocumentoIvaSpesa subdocumentoIvaSpesa = new SubdocumentoIvaSpesa();
		subdocumentoIvaSpesa.setUid(15);
		req.setSubdocumentoIvaSpesa(subdocumentoIvaSpesa);
		
		RicercaDettaglioSubdocumentoIvaSpesaResponse res = service.ricercaDettaglioSubdocumentoIvaSpesa(req);
		assertNotNull(res);
		assertTrue(res.getErrori().isEmpty());
	}
	
	/**
	 * Conta i dati collegati al subdocumento iva di spesa
	 */
	@Test
	public void contaDatiCollegatiSubdocumentoIvaSpesa() {
		ContaDatiCollegatiSubdocumentoIvaSpesa req = new ContaDatiCollegatiSubdocumentoIvaSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		
		SubdocumentoIvaSpesa subdocumentoIvaSpesa = new SubdocumentoIvaSpesa();
		subdocumentoIvaSpesa.setUid(281);
		req.setSubdocumentoIvaSpesa(subdocumentoIvaSpesa);
		
		ContaDatiCollegatiSubdocumentoIvaSpesaResponse res = service.contaDatiCollegatiSubdocumentoIvaSpesa(req);
		assertNotNull(res);
		assertTrue(res.getErrori().isEmpty());
	}
}
