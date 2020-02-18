/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.gen;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Test per i servizi dell'Allegato Atto.
 * 
 * @author Marchino Alessandro
 *
 */
public class CausaleServiceTest extends BaseProxyServiceTest<CausaleService> {
	
	@Override
	protected String getEndpoint() {
//		return "http://dev-www.ruparpiemonte.it/siacbilser/CausaleService";
		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/CausaleService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioCausale() {
		RicercaDettaglioCausale req = new RicercaDettaglioCausale();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest("AAAAAA00A11C000K", 52, 2));
		req.setBilancio(getBilancio(165, 2017));
		
		CausaleEP causaleEP = new CausaleEP();
		causaleEP.setUid(943383);
		req.setCausaleEP(causaleEP);
		
		RicercaDettaglioCausaleResponse res = service.ricercaDettaglioCausale(req);
		assertTrue(res.getErrori().isEmpty());
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSinteticaModulareCausale() {
		RicercaSinteticaModulareCausale req = new RicercaSinteticaModulareCausale();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		req.setBilancio(getBilancio(6, 2015));
		req.setCausaleEPModelDetails();
		
		CausaleEP cep = new CausaleEP();
		cep.setAmbito(Ambito.AMBITO_FIN);
		cep.setStatoOperativoCausaleEP(StatoOperativoCausaleEP.VALIDO);
		cep.setTipoCausale(TipoCausale.Integrata);
		
		ElementoPianoDeiConti elementoPianoDeiConti = new ElementoPianoDeiConti();
		elementoPianoDeiConti.setUid(125628);
		cep.setElementoPianoDeiConti(elementoPianoDeiConti);
		
		Evento evento = new Evento();
		evento.setUid(885);
		cep.addEvento(evento);
		
		req.setCausaleEP(cep);
		
		TipoEvento tipoEvento = new TipoEvento();
		tipoEvento.setUid(58);
		req.setTipoEvento(tipoEvento);
		
		RicercaSinteticaModulareCausaleResponse res = service.ricercaSinteticaModulareCausale(req);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
}
