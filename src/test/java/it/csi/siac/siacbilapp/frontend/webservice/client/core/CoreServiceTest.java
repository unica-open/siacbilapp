/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.core;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;

/**
 * Test per il CommonService.
 * 
 * @author Marchino Alessandro
 *
 */
public class CoreServiceTest extends BaseProxyServiceTest<CoreService> {
	
	@Override
	protected String getEndpoint() {
		//return "http://dev-www.ruparpiemonte.it/siaccorser/CoreService";
		return "http://tst-srv-consip.bilancio.csi.it/siaccorser/CoreService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void azioneRichiesta() {
		GetAzioneRichiesta req = new GetAzioneRichiesta();
		req.setRichiedente(getRichiedenteByProperties("consip", "edisu"));
		//req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 56, 13));
		//req.getRichiedente().getOperatore().setCodiceFiscale("AAAAAA00A11B000J");
		//req.getRichiedente().getOperatore().setCodiceFiscale("AAAAAA00A11B000J");
		
		req.setAzioneRichiesta(create(AzioneRichiesta.class, 66933317));
		
		GetAzioneRichiestaResponse res = service.getAzioneRichiesta(req);
		assertNotNull(res);
		
	}
	
}
