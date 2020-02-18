/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfinser.frontend.webservice.CartaContabileService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiaveResponse;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;

/**
 * Test per il CartaContabileService.
 * 
 * @author Marchino Alessandro
 *
 */
public class CartaContabileServiceTest extends BaseProxyServiceTest<CartaContabileService> {
	
	@Override
	protected String getEndpoint() {
		return "http://10.136.6.151/siacbilser/CartaContabileService";
	}
	
	/**
	 * Ricerca della carta contabile per chiave
	 */
	@Test
	public void ricercaCartaContabilePerChiave() {
		RicercaCartaContabilePerChiave req = new RicercaCartaContabilePerChiave();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setNumPagina(1);
		req.setNumRisultatiPerPagina(10);
		req.setCercaMdpCessionePerChiaveModPag(true);
		
		req.setpRicercaCartaContabileK(new RicercaCartaContabileK());
		req.getpRicercaCartaContabileK().setBilancio(getBilancio(143, 2017));
		req.getpRicercaCartaContabileK().setCartaContabile(create(CartaContabile.class, 0));
		req.getpRicercaCartaContabileK().getCartaContabile().setNumero(Integer.valueOf(3));
		
		RicercaCartaContabilePerChiaveResponse res = service.ricercaCartaContabilePerChiave(req);
		assertNotNull(res);
	}
}
