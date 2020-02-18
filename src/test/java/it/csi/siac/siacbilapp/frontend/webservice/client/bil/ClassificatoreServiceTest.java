/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;

/**
 * @author Alessandro Marchino
 *
 */
public class ClassificatoreServiceTest extends BaseProxyServiceTest<ClassificatoreService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/ClassificatoreService";
		// return "http://tst-www.ruparpiemonte.it/siacbilser/ClassificatoreService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void sAC() {
		LeggiStrutturaAmminstrativoContabile request = new LeggiStrutturaAmminstrativoContabile();
		request.setAnno(2015);
		request.setDataOra(new Date());
		request.setIdEnteProprietario(2);
		request.setRichiedente(getRichiedenteTest());
		request.getRichiedente().getOperatore().setCodiceFiscale("AAAAAA00A11E000M");
		
		request.setIdFamigliaTree(BilConstants.ID_FAMIGLIA_TREE_STRUTTURA_AMMINISTRATIVA_CONTABILE.getId().intValue());
		
		logRequest(request);
		
		LeggiStrutturaAmminstrativoContabileResponse response = service.leggiStrutturaAmminstrativoContabile(request);
		logResponse(response);
	}
	
}
