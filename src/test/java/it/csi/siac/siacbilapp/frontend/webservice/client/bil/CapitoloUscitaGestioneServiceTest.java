/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;


/**
 * @author Marchino Alessandro
 *
 */
public class CapitoloUscitaGestioneServiceTest extends BaseProxyServiceTest<CapitoloUscitaGestioneService> {

	@Override
	protected String getEndpoint() {
		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/CapitoloUscitaGestioneService";
	}

	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglio() {
		Richiedente richiedente = getRichiedenteTest();
		Ente ente = getEnteTest();
		
		RicercaDettaglioCapitoloUGest criteriRicerca = new RicercaDettaglioCapitoloUGest();

		criteriRicerca.setChiaveCapitolo(31003);
		RicercaDettaglioCapitoloUscitaGestione req = new RicercaDettaglioCapitoloUscitaGestione();
		req.setRichiedente(richiedente);
		req.setEnte(ente);
		req.setRicercaDettaglioCapitoloUGest(criteriRicerca);
		RicercaDettaglioCapitoloUscitaGestioneResponse res = service
				.ricercaDettaglioCapitoloUscitaGestione(req);
		logResponse(res);
	}
	
	/**
	 * Ricerca sintetica per il capitolo di entrata gestione
	 */
	@Test
	public void ricercaSinteticaCapitoloSpesaGestione(){
		final String methodName = "ricercaSinteticaCapitoloSpesaGestione";
		RicercaSinteticaCapitoloUscitaGestione req = new RicercaSinteticaCapitoloUscitaGestione();
		req.setRichiedente(getRichiedenteTest());
		req.setEnte(getEnteTest());
		req.setDataOra(new Date());
		req.setParametriPaginazione(getParametriPaginazioneTest(3, 0));
		RicercaSinteticaCapitoloUGest ricercaSinteticaCapitoloUGest = new RicercaSinteticaCapitoloUGest();
		/*ricercaSinteticaCapitoloUGest.setCodiceTitoloUscita("1");
		ricercaSinteticaCapitoloUGest.setCodiceMacroaggregato("1010000");*/
		ricercaSinteticaCapitoloUGest.setCodiceMissione("01");
		ricercaSinteticaCapitoloUGest.setCodiceProgramma("0102");
		ricercaSinteticaCapitoloUGest.setAnnoEsercizio(2015);
		req.setRicercaSinteticaCapitoloUGest(ricercaSinteticaCapitoloUGest);
		
		RicercaSinteticaCapitoloUscitaGestioneResponse res = service.ricercaSinteticaCapitoloUscitaGestione(req);

		ListaPaginata<CapitoloUscitaGestione> capitoli = res.getCapitoli();
		for (CapitoloUscitaGestione capitoloUscitaGestione : capitoli) {
			log.info(methodName, capitoloUscitaGestione.getUid());
		}
	}
	
	/**
	 * Verifica annullabilit&agrave; capitolo uscita gestione
	 */
	@Test
	public void verificaAnnullabilitaCapitoloUscitaGestione() {
		CapitoloUscitaGestione cug = new CapitoloUscitaGestione();
		cug.setAnnoCapitolo(2017);
		cug.setNumeroCapitolo(18052017);
		cug.setNumeroArticolo(0);
		cug.setNumeroUEB(1);
		cug.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.PROVVISORIO);
		
		// inizializza parametri della request
		VerificaAnnullabilitaCapitoloUscitaGestione req = new VerificaAnnullabilitaCapitoloUscitaGestione();
		req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 759, 15));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setCapitolo(cug);
		req.setDataOra(new Date());
		req.setBilancio(getBilancio(164, 2017));
		
		VerificaAnnullabilitaCapitoloUscitaGestioneResponse res = service.verificaAnnullabilitaCapitoloUscitaGestione(req);
		assertNotNull(res);
	}
}
