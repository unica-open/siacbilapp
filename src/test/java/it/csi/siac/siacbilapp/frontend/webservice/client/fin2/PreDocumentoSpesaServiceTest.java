/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin2;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;

/**
 * Test per i servizi del Documento.
 * 
 * @author Marchino Alessandro
 *
 */
public class PreDocumentoSpesaServiceTest extends BaseProxyServiceTest<PreDocumentoSpesaService> {
	
	@Override
	protected String getEndpoint() {
		//return "http://dev-www.ruparpiemonte.it/siacbilser/PreDocumentoSpesaService";
		//return "http://localhost:8080/siacbilser/PreDocumentoSpesaService";
		return "http://tst-srv-consip.bilancio.csi.it/siacbilser/PreDocumentoSpesaService";
//		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/PreDocumentoSpesaService";
	}
	
	/**
	 * Ricerca di dettaglio del predocumento di spesa
	 */
	@Test
	public void ricercaDettaglioPreDocumentoSpesa() {
		RicercaDettaglioPreDocumentoSpesa req = new RicercaDettaglioPreDocumentoSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest("AAAAAA00A11L000T", 51, 5));
		
		PreDocumentoSpesa preDocumentoSpesa = new PreDocumentoSpesa();
		preDocumentoSpesa.setUid(1737);
		req.setPreDocumentoSpesa(preDocumentoSpesa);
		
		RicercaDettaglioPreDocumentoSpesaResponse res = service.ricercaDettaglioPreDocumentoSpesa(req);
		assertNotNull(res);
	}
	
	/**
	 * Ricerca sintetica del predocumento di spesa
	 */
	@Test
	public void ricercaSinteticaPreDocumentoSpesa() {
		RicercaSinteticaPreDocumentoSpesa req = new RicercaSinteticaPreDocumentoSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setParametriPaginazione(getParametriPaginazioneTest(10, 0));
		
		req.setPreDocumentoSpesa(create(PreDocumentoSpesa.class, 0));
		req.getPreDocumentoSpesa().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getPreDocumentoSpesa().setNumero(Integer.valueOf(47352));
		
//		req.getPreDocumentoSpesa().setDatiAnagraficiPreDocumento(create(DatiAnagraficiPreDocumentoSpesa.class, 0));
//		req.getPreDocumentoSpesa().getDatiAnagraficiPreDocumento().setCognome("Azienda");
		
		req.setSoggettoMancante(Boolean.TRUE);
		
//		req.setTipoCausale(create(TipoCausale.class, 1));
		
		RicercaSinteticaPreDocumentoSpesaResponse res = service.ricercaSinteticaPreDocumentoSpesa(req);
		assertNotNull(res);
	}
	
	/**
	 * Test per DettaglioStoricoCausaleSpesa
	 */
	@Test
	public void dettaglioStoricoCausaleSpesa() {
		DettaglioStoricoCausaleSpesa request = new DettaglioStoricoCausaleSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		CausaleSpesa causale = new CausaleSpesa();
		causale.setUid(23);
		request.setCausale(causale);
		
		DettaglioStoricoCausaleSpesaResponse response = service.dettaglioStoricoCausaleSpesa(request);
		log.logXmlTypeObject(response, "RESPONSE");
	}
	
	/**
	 * Test
	 */
	@Test
	public void marshaller() {
		Periodo p = Periodo.AGOSTO;
		log.logXmlTypeObject(p, "PERIODO");
		String xml = JAXBUtility.marshall(p);
		log.debug("marshaller", "XML: \n" + xml);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSinteticaCausaleSpesa() {
		RicercaSinteticaCausaleSpesa req = new RicercaSinteticaCausaleSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		CausaleSpesa causaleSpesa = new CausaleSpesa();
		causaleSpesa.setEnte(getEnteTest());
		causaleSpesa.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
		req.setCausaleSpesa(causaleSpesa );
		
		RicercaSinteticaCausaleSpesaResponse res = service.ricercaSinteticaCausaleSpesa(req);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioCausaleSpesa() {
		RicercaDettaglioCausaleSpesa req = new RicercaDettaglioCausaleSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		
		CausaleSpesa causaleSpesa = new CausaleSpesa();
		causaleSpesa.setEnte(getEnteTest());
		causaleSpesa.setUid(26);
		req.setCausaleSpesa(causaleSpesa );
		
		RicercaDettaglioCausaleSpesaResponse res = service.ricercaDettaglioCausaleSpesa(req);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
}
