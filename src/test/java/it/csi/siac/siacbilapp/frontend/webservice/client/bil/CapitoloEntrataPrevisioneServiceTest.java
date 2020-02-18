/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;


/**
 * @author Marchino Alessandro
 *
 */
public class CapitoloEntrataPrevisioneServiceTest extends BaseProxyServiceTest<CapitoloEntrataPrevisioneService> {

	@Override
	protected String getEndpoint() {
//		return "http://dev-www.ruparpiemonte.it/siacbilser/CapitoloEntrataPrevisioneService";
		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/CapitoloEntrataPrevisioneService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglio() {
		Richiedente richiedente = getRichiedenteTest();
		Ente ente = getEnteTest();
		
		RicercaDettaglioCapitoloEPrev criteriRicerca = new RicercaDettaglioCapitoloEPrev();

		criteriRicerca.setChiaveCapitolo(12022);
		RicercaDettaglioCapitoloEntrataPrevisione req = new RicercaDettaglioCapitoloEntrataPrevisione();
		req.setRichiedente(richiedente);
		req.setEnte(ente);
		req.setRicercaDettaglioCapitoloEPrev(criteriRicerca);
		RicercaDettaglioCapitoloEntrataPrevisioneResponse res = service.ricercaDettaglioCapitoloEntrataPrevisione(req);
		logResponse(res);
	}
	
	/**
	 * Ricerca sintetica del capitolo di entrata previsione
	 */
	@Test
	public void ricercaSinteticaCapitoloEntrataPrevisione() {
		RicercaSinteticaCapitoloEntrataPrevisione req = new RicercaSinteticaCapitoloEntrataPrevisione();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 163, 29));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
		
		RicercaSinteticaCapitoloEPrev rscep = new RicercaSinteticaCapitoloEPrev();
		rscep.setAnnoCapitolo(Integer.valueOf(2017));
		rscep.setAnnoEsercizio(Integer.valueOf(2017));
		rscep.setNumeroCapitolo(Integer.valueOf(302030015));
		rscep.setNumeroUEB(Integer.valueOf(1));
		rscep.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO);
		req.setRicercaSinteticaCapitoloEPrev(rscep);
		
		RicercaSinteticaCapitoloEntrataPrevisioneResponse res = service.ricercaSinteticaCapitoloEntrataPrevisione(req);
		assertNotNull(res);
		
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSinteticaPerGliAnni() {
		RicercaSinteticaCapitoloEntrataPrevisione req = new RicercaSinteticaCapitoloEntrataPrevisione();
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteTest());
		req.setDataOra(new Date());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		RicercaSinteticaCapitoloEPrev util = new RicercaSinteticaCapitoloEPrev();
		util.setAnnoEsercizio(Integer.valueOf(2013));
		util.setDescrizioneCapitolo("t");
		
		req.setRicercaSinteticaCapitoloEPrev(util);
		
		RicercaSinteticaCapitoloEntrataPrevisioneResponse res = service.ricercaSinteticaCapitoloEntrataPrevisione(req);
		assertNotNull(res);
		for (CapitoloEntrataPrevisione cup : res.getCapitoli()) {
			List<ImportiCapitoloEP> list = cup.getListaImportiCapitolo();
			StringBuilder capitolo = new StringBuilder();
			capitolo.append("CAPITOLO ")
				.append(cup.getNumeroCapitolo()).append("/")
				.append(cup.getNumeroArticolo()).append("/")
				.append(cup.getNumeroUEB())
				.append(" | IMPORTI PER ANNO:");
			for(ImportiCapitoloEP icep : list) {
				capitolo.append(" ~ ").append(icep.getAnnoCompetenza())
					.append(": ").append(icep.getStanziamento());
			}
			log.debug("testRicercaSinteticaPerGliAnni", capitolo.toString());
		}
		
	}
	
	/**
	 * Test di ricerca puntuale
	 */
	@Test
	public void ricercaPuntuale() {
		RicercaPuntualeCapitoloEntrataPrevisione req = new RicercaPuntualeCapitoloEntrataPrevisione();
		
		req.setDataOra(new Date());
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteTest());
		
		RicercaPuntualeCapitoloEPrev ricercaPuntualeCapitoloEPrev = new RicercaPuntualeCapitoloEPrev();
		req.setRicercaPuntualeCapitoloEPrev(ricercaPuntualeCapitoloEPrev);
		
		ricercaPuntualeCapitoloEPrev.setAnnoEsercizio(2015);
		ricercaPuntualeCapitoloEPrev.setAnnoCapitolo(2015);
		ricercaPuntualeCapitoloEPrev.setNumeroCapitolo(105);
		ricercaPuntualeCapitoloEPrev.setNumeroArticolo(0);
		ricercaPuntualeCapitoloEPrev.setNumeroUEB(100130000);
		ricercaPuntualeCapitoloEPrev.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		
		RicercaPuntualeCapitoloEntrataPrevisioneResponse res = service.ricercaPuntualeCapitoloEntrataPrevisione(req);
		assertNotNull(res);
	}
}
