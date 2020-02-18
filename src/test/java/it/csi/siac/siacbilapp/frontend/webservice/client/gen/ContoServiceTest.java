/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.gen;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancio;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancioResponse;
import it.csi.siac.siacgenser.model.ClassePiano;

/**
 * @author Marchino Alessandro
 *
 */
public class ContoServiceTest extends BaseProxyServiceTest<ContoService> {
	
	@Override
	protected String getEndpoint() {
		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/ContoService";
	}
	
	/**
	 * Leggi tree codice bilancio
	 */
	@Test
	public void leggiTreeCodiceBilancio() {
		LeggiTreeCodiceBilancio req = new LeggiTreeCodiceBilancio();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnno(Integer.valueOf(2017));
		req.setClassePiano(create(ClassePiano.class, 289));
		
		LeggiTreeCodiceBilancioResponse res = service.leggiTreeCodiceBilancio(req);
		assertNotNull(res);
	}

}
