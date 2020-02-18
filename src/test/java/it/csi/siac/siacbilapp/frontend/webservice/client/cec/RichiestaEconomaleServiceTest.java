/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.cec;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaRichiesteAnticipoMissioniNonErogate;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaRichiesteAnticipoMissioniNonErogateResponse;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * Classe di test per la cassa economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/gen/2015
 *
 */
public class RichiestaEconomaleServiceTest extends BaseProxyServiceTest<RichiestaEconomaleService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/RichiestaEconomaleService";
//		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/RichiestaEconomaleService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioRichiestaEconomale() {
		RicercaDettaglioRichiestaEconomale request = new RicercaDettaglioRichiestaEconomale();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		richiestaEconomale.setUid(60);
		request.setRichiestaEconomale(richiestaEconomale);
		
		RicercaDettaglioRichiestaEconomaleResponse response = service.ricercaDettaglioRichiestaEconomale(request);
		logResponse(response);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioRendicontoRichiesta() {
		RicercaDettaglioRendicontoRichiesta request = new RicercaDettaglioRendicontoRichiesta();
		request.setDataOra(new Date());
		//request.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 759, 15));
		request.setRichiedente(getRichiedenteTest());
		
		RendicontoRichiesta rendicontoRichiesta = new RendicontoRichiesta();
		rendicontoRichiesta.setUid(28);
		request.setRendicontoRichiesta(rendicontoRichiesta);
		
		RicercaDettaglioRendicontoRichiestaResponse response = service.ricercaDettaglioRendicontoRichiesta(request);
		logResponse(response);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaRichiesteAnticipoMissioniNonErogate() {
		RicercaRichiesteAnticipoMissioniNonErogate request = new RicercaRichiesteAnticipoMissioniNonErogate();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 759, 15));
		request.setCaricaDettaglioGiustificativi(true);
		
		RicercaRichiesteAnticipoMissioniNonErogateResponse response = service.ricercaRichiesteAnticipoMissioniNonErogate(request);
		logResponse(response);
	}
}
