/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin2;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfin2ser.frontend.webservice.StampaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoChiusura;
import it.csi.siac.siacfin2ser.model.TipoStampa;

/**
 * Test per i servizi della Stampa IVA.
 * 
 * @author Marchino Alessandro
 *
 */
public class StampaIvaServiceTest extends BaseProxyServiceTest<StampaIvaService> {
	
	@Override
	protected String getEndpoint() {
		return "http://127.0.0.1:8080/siacbilser/StampaIvaService";
	}
	
	/**
	 * Stampa del Registro IVA
	 */
	@Test
	public void stampaRegistroIva() {
		StampaRegistroIva req = new StampaRegistroIva();
		
		req.setDataOra(new Date());
		req.setBilancio(getBilancio(188, 2018));
		req.setRichiedente(getRichiedenteByProperties("consip", "coal"));
		req.setDocumentiPagati(Boolean.TRUE);
		req.setDocumentiIncassati(Boolean.FALSE);
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setPeriodo(Periodo.GENNAIO);
		req.setTipoStampa(TipoStampa.BOZZA);
		req.setAnnoBilancio(Integer.valueOf(req.getBilancio().getAnno()));
		
		req.setRegistroIva(create(RegistroIva.class, 151));
		req.getRegistroIva().setGruppoAttivitaIva(create(GruppoAttivitaIva.class, 345));
		req.getRegistroIva().getGruppoAttivitaIva().setTipoChiusura(TipoChiusura.MENSILE);
		
		StampaRegistroIvaResponse res = service.stampaRegistroIva(req);
		assertNotNull(res);
	}
	
}
