/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;
import java.util.EnumSet;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;


/**
 * @author Marchino Alessandro
 *
 */
public class CapitoloEntrataGestioneServiceTest extends BaseProxyServiceTest<CapitoloEntrataGestioneService> {

	@Override
	protected String getEndpoint() {
		return endpointProperties.getProperty("forn2") + "/siacbilser/CapitoloEntrataGestioneService";
	}
	
	/**
	 * Test di ricerca sintetica per il capitolo di entrata gestione
	 */
	@Test
	public void ricercaSinteticaCapitoloEntrataGestione() {
		RicercaSinteticaCapitoloEntrataGestione req = new RicercaSinteticaCapitoloEntrataGestione();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		RicercaSinteticaCapitoloEGest ricercaSinteticaCapitoloEntrata = new RicercaSinteticaCapitoloEGest();
		ricercaSinteticaCapitoloEntrata.setAnnoEsercizio(Integer.valueOf(2015));
		ricercaSinteticaCapitoloEntrata.setNumeroCapitolo(Integer.valueOf(106));
		ricercaSinteticaCapitoloEntrata.setNumeroArticolo(Integer.valueOf(0));
		
		ricercaSinteticaCapitoloEntrata.setNumeroUEB(Integer.valueOf(1));
		ricercaSinteticaCapitoloEntrata.setAnnoCapitolo(Integer.valueOf(2015));
		ricercaSinteticaCapitoloEntrata.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO);
		req.setRicercaSinteticaCapitoloEntrata(ricercaSinteticaCapitoloEntrata);
		
		RicercaSinteticaCapitoloEntrataGestioneResponse res = service.ricercaSinteticaCapitoloEntrataGestione(req);
		assertNotNull(res);
	}
	
	/**
	 * Ricerca sintetica per il capitolo di entrata gestione
	 */
	@Test
	public void ricercaSinteticaCapitoloEntrataGestione2(){
		final String methodName = "ricercaSinteticaCapitoloEntrataGestione2";
		RicercaSinteticaCapitoloEntrataGestione req = new RicercaSinteticaCapitoloEntrataGestione();
		req.setRichiedente(getRichiedenteTest());
		req.setEnte(getEnteTest());
		req.setDataOra(new Date());
		req.setParametriPaginazione(getParametriPaginazioneTest(3, 0));
		RicercaSinteticaCapitoloEGest ricercaSinteticaCapitoloEGest = new RicercaSinteticaCapitoloEGest();
		ricercaSinteticaCapitoloEGest.setAnnoEsercizio(2015);
		ricercaSinteticaCapitoloEGest.setCodiceTitoloEntrata("1");
		ricercaSinteticaCapitoloEGest.setCodiceTipologia("1030100");
		req.setRicercaSinteticaCapitoloEntrata(ricercaSinteticaCapitoloEGest);
		
		RicercaSinteticaCapitoloEntrataGestioneResponse res = service.ricercaSinteticaCapitoloEntrataGestione(req);

		ListaPaginata<CapitoloEntrataGestione> capitoli = res.getCapitoli();
		for (CapitoloEntrataGestione capitoloEntrataGestione : capitoli) {
			log.info(methodName, capitoloEntrataGestione.getUid());
		}
	}
	
	/**
	 * Ricerca dettaglio capitolo entrata gestione
	 */
	@Test
	public void ricercaDettaglioCapitoloEntrataGestione() {
		RicercaDettaglioCapitoloEntrataGestione req = new RicercaDettaglioCapitoloEntrataGestione();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("forn2", "coal"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCapitoloEnum.class));
		
		RicercaDettaglioCapitoloEGest ricercaDettaglioCapitoloEGest = new RicercaDettaglioCapitoloEGest();
		req.setRicercaDettaglioCapitoloEGest(ricercaDettaglioCapitoloEGest);
		ricercaDettaglioCapitoloEGest.setChiaveCapitolo(48755);
		
		RicercaDettaglioCapitoloEntrataGestioneResponse res = service.ricercaDettaglioCapitoloEntrataGestione(req);
		assertNotNull(res);
	}
}
