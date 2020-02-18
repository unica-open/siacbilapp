/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.cec;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccecser.frontend.webservice.StampaCassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaStampeCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaStampeCassaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StampaRendiconto;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoDocumento;

/**
 * Classe di test per la stampa della cassa economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/05/2015
 *
 */
public class StampaCassaEconomaleServiceTest extends BaseProxyServiceTest<StampaCassaEconomaleService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/StampaCassaEconomaleService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaStampeCassaEconomale() {
		RicercaStampeCassaEconomale request = new RicercaStampeCassaEconomale();
		
		request.setDataOra(new Date());
		request.setParametriPaginazione(getParametriPaginazioneTest());
		
		request.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 71, 15));
		
		StampeCassaFile stampa = new StampeCassaFile();
		
		CassaEconomale cassaEconomale = new CassaEconomale();
		cassaEconomale.setUid(13);
		stampa.setCassaEconomale(cassaEconomale);
		
		stampa.setTipoDocumento(TipoDocumento.RENDICONTO);
		stampa.setBilancio(getBilancio(46, 2015));
		
		StampaRendiconto stampaRendiconto = new StampaRendiconto();
		stampaRendiconto.setNumeroRendiconto(Integer.valueOf(1));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2015);
		cal.set(Calendar.MONTH, Calendar.MAY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		stampaRendiconto.setPeriodoDataInizio(cal.getTime());
		
		cal.set(Calendar.DAY_OF_MONTH, 3);
		stampaRendiconto.setPeriodoDataFine(cal.getTime());
		
		stampa.setStampaRendiconto(stampaRendiconto);
		
		request.setStampa(stampa);
		
		logRequest(request);
		RicercaStampeCassaEconomaleResponse response = service.ricercaStampeCassaEconomale(request);
		logResponse(response);
	}
	
}
