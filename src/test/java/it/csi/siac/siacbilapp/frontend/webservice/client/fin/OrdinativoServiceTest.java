/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinser.frontend.webservice.OrdinativoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;

/**
 * Classe di test per i servizi dell'ordinativo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/giu/2015
 *
 */
public class OrdinativoServiceTest extends BaseProxyServiceTest<OrdinativoService> {
	
	@Override
	protected String getEndpoint() {
		return "http://10.136.6.151/siacbilser/OrdinativoService";
	}
	
	/**
	 * Test.
	 */
	@Test
	public void ricercaOrdinativoPagamentoPerChiave() {
		RicercaOrdinativoPagamentoPerChiave request = new RicercaOrdinativoPagamentoPerChiave();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		request.setEnte(request.getRichiedente().getAccount().getEnte());
		
		RicercaOrdinativoPagamentoK pRicercaOrdinativoPagamentoK = new RicercaOrdinativoPagamentoK();
		OrdinativoPagamento ordinativoPagamento = new OrdinativoPagamento();
		ordinativoPagamento.setNumero(Integer.valueOf(89));
		ordinativoPagamento.setAnno(Integer.valueOf(2015));
		pRicercaOrdinativoPagamentoK.setOrdinativoPagamento(ordinativoPagamento);
		
		int anno = 2015;
		Bilancio bilancio = getBilancio(16,2015);
		bilancio.setAnno(anno);
		pRicercaOrdinativoPagamentoK.setBilancio(bilancio);
		
		request.setpRicercaOrdinativoPagamentoK(pRicercaOrdinativoPagamentoK);
		
		RicercaOrdinativoPagamentoPerChiaveResponse response = service.ricercaOrdinativoPagamentoPerChiave(request);
		assertNotNull(response);
	}
	
	/**
	 * Test.
	 */
	@Test
	public void ricercaOrdinativoIncassoPerChiave() {
		RicercaOrdinativoIncassoPerChiave request = new RicercaOrdinativoIncassoPerChiave();
		
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		//request.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 71, 15));
		request.setRichiedente(getRichiedenteTest());
		
		RicercaOrdinativoIncassoK pRicercaOrdinativoIncassoK = new RicercaOrdinativoIncassoK();
		OrdinativoIncasso ordinativoIncasso = new OrdinativoIncasso();
		ordinativoIncasso.setNumero(Integer.valueOf(84));
		ordinativoIncasso.setAnno(Integer.valueOf(2015));
		pRicercaOrdinativoIncassoK.setOrdinativoIncasso(ordinativoIncasso);
		
		Bilancio bilancio = getBilancio(6, 2015);
		pRicercaOrdinativoIncassoK.setBilancio(bilancio);
		
		request.setpRicercaOrdinativoIncassoK(pRicercaOrdinativoIncassoK);
		
		
		RicercaOrdinativoIncassoPerChiaveResponse response = service.ricercaOrdinativoIncassoPerChiave(request);
		logResponse(response);
	}
	
	/**
	 * Test
	 */
	@Test
	public void annullaOrdinativo() {
		AnnullaOrdinativoIncasso request = new AnnullaOrdinativoIncasso();
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest());
		request.setBilancio(getBilancio(6, 2015));
		OrdinativoIncasso ordinativoIncassoDaAnnullare = new OrdinativoIncasso();
		ordinativoIncassoDaAnnullare.setAnno(Integer.valueOf(2015));
		ordinativoIncassoDaAnnullare.setNumero(Integer.valueOf(84));
		request.setOrdinativoIncassoDaAnnullare(ordinativoIncassoDaAnnullare);
//		RicercaOrdinativoIncassoPerChiave request = new RicercaOrdinativoIncassoPerChiave();
//		
//		request.setDataOra(new Date());
//		request.setEnte(getEnteTest());
//		request.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 71, 15));
//		
//		RicercaOrdinativoIncassoK pRicercaOrdinativoIncassoK = new RicercaOrdinativoIncassoK();
//		OrdinativoIncasso ordinativoIncasso = new OrdinativoIncasso();
//		ordinativoIncasso.setNumero(Integer.valueOf(22));
//		ordinativoIncasso.setAnno(Integer.valueOf(2015));
//		pRicercaOrdinativoIncassoK.setOrdinativoIncasso(ordinativoIncasso);
//		
//		int anno = 2015;
//		Bilancio bilancio = getBilancioTest(anno);
//		pRicercaOrdinativoIncassoK.setBilancio(bilancio);
//		
//		request.setpRicercaOrdinativoIncassoK(pRicercaOrdinativoIncassoK);
//		
//		
//		RicercaOrdinativoIncassoPerChiaveResponse response = service.ricercaOrdinativoIncassoPerChiave(request);
//		logResponse(response);
		
		AnnullaOrdinativoIncassoResponse response = service.annullaOrdinativoIncasso(request);
		assertNotNull(response);
	}
	
	
	
	
}
