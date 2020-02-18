/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.cec;

import java.util.Date;
import java.util.EnumSet;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.model.ImportiCassaEconomaleEnum;
import it.csi.siac.siaccecser.frontend.webservice.CassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;

/**
 * Classe di test per la cassa economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/gen/2015
 *
 */
public class CassaEconomaleServiceTest extends BaseProxyServiceTest<CassaEconomaleService> {
	
	@Override
	protected String getEndpoint() {
		//return "http://dev-www.ruparpiemonte.it/siacbilser/CassaEconomaleService";
		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/CassaEconomaleService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSinteticaCassaEconomale() {
		RicercaSinteticaCassaEconomale request = new RicercaSinteticaCassaEconomale();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		request.setBilancio(getBilancio(6, 2015));
		
		RicercaSinteticaCassaEconomaleResponse response = service.ricercaSinteticaCassaEconomale(request);
		logResponse(response);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioCassaEconomale() {
		RicercaDettaglioCassaEconomale request = new RicercaDettaglioCassaEconomale();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 759, 15));
		//request.setRichiedente(getRichiedenteTest());
		
		CassaEconomale cassaEconomale = new CassaEconomale();
		cassaEconomale.setUid(34);
		request.setCassaEconomale(cassaEconomale);
		
		RicercaDettaglioCassaEconomaleResponse response = service.ricercaDettaglioCassaEconomale(request);
		logResponse(response);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSinteticaTipoOperazioneDiCassa() {
		RicercaSinteticaTipoOperazioneDiCassa request = new RicercaSinteticaTipoOperazioneDiCassa();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		request.setParametriPaginazione(getParametriPaginazioneTest());
		TipoOperazioneCassa tipoOperazioneCassa = new TipoOperazioneCassa();
		tipoOperazioneCassa.setCodice("COD");
		request.setTipoOperazioneCassa(tipoOperazioneCassa);
		
		RicercaSinteticaTipoOperazioneDiCassaResponse response = service.ricercaSinteticaTipoOperazioneDiCassa(request);
		logResponse(response);
		
	}
	
	/**
	 * Test
	 */
	@Test
	public void calcolaDisponibilitaCassaEconomale() {
		CalcolaDisponibilitaCassaEconomale request = new CalcolaDisponibilitaCassaEconomale();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		CassaEconomale cassaEconomale = new CassaEconomale();
		cassaEconomale.setUid(2);
		request.setCassaEconomale(cassaEconomale);
		
		request.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCassaEconomaleEnum.class));
		
		CalcolaDisponibilitaCassaEconomaleResponse response = service.calcolaDisponibilitaCassaEconomale(request);
		logResponse(response);
	}
	
}
