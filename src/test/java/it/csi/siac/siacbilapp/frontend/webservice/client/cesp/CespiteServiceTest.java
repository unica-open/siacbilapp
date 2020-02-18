/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.cesp;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespiteModelDetail;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Test per i servizi del Cespite.
 * 
 * @author Marchino Alessandro
 *
 */
public class CespiteServiceTest extends BaseProxyServiceTest<CespiteService> {
	
	@Override
	protected String getEndpoint() {
		return "http://127.0.0.1:8080/siacbilser/CespiteService";
//		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/CespiteService";
	}
	
	/**
	 * Ricerca sintetica del cespite
	 */
	@Test
	public void ricercaSinteticaCespite() {
		RicercaSinteticaCespite req = new RicercaSinteticaCespite();
		req.setDataOra(new Date());
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		req.setCespite(create(Cespite.class, 0));
		req.getCespite().setFlgDonazioneRinvenimento(Boolean.FALSE);
		
		req.setModelDetails(CespiteModelDetail.TipoBeneCespiteModelDetail, TipoBeneCespiteModelDetail.Annullato);
		
		RicercaSinteticaCespiteResponse res = service.ricercaSinteticaCespite(req);
		assertNotNull(res);
	}
	
	/**
	 * Ricerca sintetica variazione cespite
	 */
	@Test
	public void ricercaSinteticaVariazioneCespite() {
		RicercaSinteticaVariazioneCespite req = new RicercaSinteticaVariazioneCespite();
		req.setDataOra(new Date());
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		req.setVariazioneCespite(create(VariazioneCespite.class, 0));
		req.getVariazioneCespite().setCespite(create(Cespite.class, 16));
		
		req.setModelDetails(VariazioneCespiteModelDetail.StatoVariazioneCespite);
		
		RicercaSinteticaVariazioneCespiteResponse res = service.ricercaSinteticaVariazioneCespite(req);
		assertNotNull(res);
	}
	
	/**
	 * Ricerca sintetica registro A cespite
	 */
	@Test
	public void ricercaSinteticaRegistroACespite() {
		RicercaSinteticaRegistroACespite req = new RicercaSinteticaRegistroACespite();
		req.setDataOra(new Date());
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		req.setBilancio(getBilancio(131, 2017));
		req.setPrimaNota(create(PrimaNota.class, 0));
		req.getPrimaNota().setStatoOperativoPrimaNota(StatoOperativoPrimaNota.DEFINITIVO);
		req.getPrimaNota().setTipoCausale(TipoCausale.Libera);
		
		req.setModelDetails(
				PrimaNotaModelDetail.StatoOperativo,
				PrimaNotaModelDetail.ContoInventario,
				PrimaNotaModelDetail.PrimaNotaInventario,				
				PrimaNotaModelDetail.MovimentiEp);
		
		RicercaSinteticaRegistroACespiteResponse res = service.ricercaSinteticaRegistroACespite(req);
		assertNotNull(res);
	}
	

}
