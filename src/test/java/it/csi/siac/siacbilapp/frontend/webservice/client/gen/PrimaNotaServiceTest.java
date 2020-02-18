/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.gen;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * Test per i servizi dell'Allegato Atto.
 * 
 * @author Marchino Alessandro
 *
 */
public class PrimaNotaServiceTest extends BaseProxyServiceTest<PrimaNotaService> {
	
	@Override
	protected String getEndpoint() {
		return "http://10.136.6.151/siacbilser/PrimaNotaService";
		//return "http://localhost:8080/siacbilser/PrimaNotaService";
		//return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/PrimaNotaService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSinteticaPrimaNotaIntegrata() {
		RicercaSinteticaPrimaNotaIntegrata req = new RicercaSinteticaPrimaNotaIntegrata();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		req.setBilancio(getBilancio(6, 2015));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		req.setNumeroMovimento("us-2015-1");
		req.setAnnoMovimento(Integer.valueOf(2015));
		req.setNumeroSubmovimento(Integer.valueOf(1));
		
		PrimaNota primaNota = new PrimaNota();
		req.setPrimaNota(primaNota);
		
		RicercaSinteticaPrimaNotaIntegrataResponse res = service.ricercaSinteticaPrimaNotaIntegrata(req);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioPrimaNotaIntegrata() {
		RicercaDettaglioPrimaNotaIntegrata req = new RicercaDettaglioPrimaNotaIntegrata();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setPrimaNota(create(PrimaNota.class, 24145));
		
		RicercaDettaglioPrimaNotaIntegrataResponse res = service.ricercaDettaglioPrimaNotaIntegrata(req);
		assertNotNull(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioPrimaNotaLibera() {
		RicercaDettaglioPrimaNota req = new RicercaDettaglioPrimaNota();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setPrimaNota(create(PrimaNota.class, 24145));
		
		RicercaDettaglioPrimaNotaResponse res = service.ricercaDettaglioPrimaNota(req);
		assertNotNull(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioPrimaNota() {
		RicercaDettaglioPrimaNota req = new RicercaDettaglioPrimaNota();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		PrimaNota primaNota = new PrimaNota();
		primaNota.setUid(586);
		req.setPrimaNota(primaNota);
		
		RicercaDettaglioPrimaNotaResponse res = service.ricercaDettaglioPrimaNota(req);
		assertNotNull(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ottieniEntitaCollegatePrimaNota() {
		OttieniEntitaCollegatePrimaNota req = new OttieniEntitaCollegatePrimaNota();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 773, 30));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		PrimaNota primaNota = new PrimaNota();
		primaNota.setUid(164199);
		req.setPrimaNota(primaNota);
		
		req.setTipoCollegamento(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA);
		req.setModelDetails();
		
		OttieniEntitaCollegatePrimaNotaResponse res = service.ottieniEntitaCollegatePrimaNota(req);
		assertNotNull(res);
	}
}
