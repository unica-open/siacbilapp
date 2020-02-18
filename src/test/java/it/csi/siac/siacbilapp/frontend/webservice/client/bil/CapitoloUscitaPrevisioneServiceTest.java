/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;


/**
 * @author Marchino Alessandro
 *
 */
public class CapitoloUscitaPrevisioneServiceTest extends BaseProxyServiceTest<CapitoloUscitaPrevisioneService> {

	@Override
	protected String getEndpoint() {
		return "http://tst-srv-consip.bilancio.csi.it/siacbilser/CapitoloUscitaPrevisioneService";
	}

	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglio() {
		Richiedente richiedente = getRichiedenteTest();
		Ente ente = getEnteTest();
		
		RicercaDettaglioCapitoloUPrev criteriRicerca = new RicercaDettaglioCapitoloUPrev();

		criteriRicerca.setChiaveCapitolo(8339);
		RicercaDettaglioCapitoloUscitaPrevisione req = new RicercaDettaglioCapitoloUscitaPrevisione();
		req.setRichiedente(richiedente);
		req.setEnte(ente);
		req.setRicercaDettaglioCapitoloUPrev(criteriRicerca);
		RicercaDettaglioCapitoloUscitaPrevisioneResponse res = service
				.ricercaDettaglioCapitoloUscitaPrevisione(req);
		logResponse(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSinteticaCapitoloUscitaPrevisione() {
		RicercaSinteticaCapitoloUscitaPrevisione req = new RicercaSinteticaCapitoloUscitaPrevisione();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setDataOra(new Date());
		req.setParametriPaginazione(getParametriPaginazioneTest(10, 0));
		req.setCalcolaTotaleImporti(Boolean.TRUE);
		
		RicercaSinteticaCapitoloUPrev util = new RicercaSinteticaCapitoloUPrev();
		req.setRicercaSinteticaCapitoloUPrev(util);
		
		util.setAnnoEsercizio(Integer.valueOf(2018));
		util.setAnnoCapitolo(Integer.valueOf(2018));
		util.setCodiceRicorrenteSpesa("3");
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse res = service.ricercaSinteticaCapitoloUscitaPrevisione(req);
		assertNotNull(res);
	}
	
}
