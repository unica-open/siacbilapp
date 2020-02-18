/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;
import java.util.EnumSet;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;

/**
 * Classe di test per il Capitolo Uscita Previsione tramite proxy.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 06/10/2015
 *
 */
public class CapitoloUscitaPrevisioneProxyServiceTest extends BaseProxyServiceTest<CapitoloUscitaPrevisioneService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/CapitoloUscitaPrevisioneService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSinteticaPerGliAnni() {
		RicercaSinteticaCapitoloUscitaPrevisione req = new RicercaSinteticaCapitoloUscitaPrevisione();
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteTest());
		req.setDataOra(new Date());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		RicercaSinteticaCapitoloUPrev util = new RicercaSinteticaCapitoloUPrev();
		util.setAnnoEsercizio(Integer.valueOf(2015));
		util.setAnnoCapitolo(Integer.valueOf(2015));
		//util.setDescrizioneCapitolo("t");
		
		req.setRicercaSinteticaCapitoloUPrev(util);
		req.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse res = service.ricercaSinteticaCapitoloUscitaPrevisione(req);
		assertNotNull(res);
	}
	
}
