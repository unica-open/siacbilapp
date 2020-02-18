/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.gen;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacgenser.frontend.webservice.RegistrazioneMovFinService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Test per i servizi dell'Allegato Atto.
 * 
 * @author Marchino Alessandro
 *
 */
public class RegistrazioneMovFinServiceTest extends BaseProxyServiceTest<RegistrazioneMovFinService> {
	
	@Override
	protected String getEndpoint() {
		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/RegistrazioneMovFinService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSinteticaPrimaNotaIntegrata() {
		RicercaSinteticaRegistrazioneMovFin req = new RicercaSinteticaRegistrazioneMovFin();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		Evento evento = new Evento();
		evento.setUid(281);
		req.setEvento(evento);
		
		req.setIdDocumento(Integer.valueOf(166));
		
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setBilancio(getBilancio(6, 2015));
		req.setRegistrazioneMovFin(registrazioneMovFin);
		
		RicercaSinteticaRegistrazioneMovFinResponse res = service.ricercaSinteticaRegistrazioneMovFin(req);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioRegMovFin() {
		RicercaDettaglioRegistrazioneMovFin req = new RicercaDettaglioRegistrazioneMovFin();
		
		req.setDataOra(new Date());
		/*forn2.cove.codicefiscale = AAAAAA00A11E000M
forn2.cove.accountid = 773
forn2.cove.enteproprietarioid = 30
http://tst-srv1-forn2.bilancio.csi.it
		 * */
		req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 773, 30));
		
		
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		//registrazioneMovFin.setBilancio(getBilancio(101, 2016));
		registrazioneMovFin.setUid(215068);
		req.setRegistrazioneMovFin(registrazioneMovFin);
		
		RicercaDettaglioRegistrazioneMovFinResponse res = service.ricercaDettaglioRegistrazioneMovFin(req);
		log.logXmlTypeObject(res, "RESPONSE");
	}
}
