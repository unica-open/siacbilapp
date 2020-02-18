/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.EmissioneOrdinativiService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElenco;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Test per i servizi dell'Allegato Atto.
 * 
 * @author Marchino Alessandro
 *
 */
public class EmissioneOrdinativiServiceTest extends BaseProxyServiceTest<EmissioneOrdinativiService> {
	
	@Override
	protected String getEndpoint() {
		return "http://127.0.0.1:8080/siacbilser/EmissioneOrdinativiService";
		//return "http://dev-www.ruparpiemonte.it/siacbilser/EmissioneOrdinativiService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void emetteOrdinativiDiPagamentoDaElenco() {
		EmetteOrdinativiDiPagamentoDaElenco req = new EmetteOrdinativiDiPagamentoDaElenco();
		req.setBilancio(getBilancio(6, 2015));
		req.setFlagConvalidaManuale(Boolean.FALSE);
		req.setRichiedente(getRichiedenteTest());
		
		ContoTesoreria contoTesoreria = new ContoTesoreria();
		contoTesoreria.setUid(5);
		req.setContoTesoreria(contoTesoreria);
		
		List<ElencoDocumentiAllegato> elenchi = new ArrayList<ElencoDocumentiAllegato>();
		int[] uids = new int[] {138};
		
		for(int uid : uids) {
			ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
			eda.setUid(uid);
			elenchi.add(eda);
		}
		req.setElenchi(elenchi);
		
		AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco> reqWrapper = wrapRequestToAsync(req);
		AsyncServiceResponse res = service.emetteOrdinativiDiPagamentoDaElenco(reqWrapper);
		assertNotNull(res);
	}
	
	
	/**
	 * Test
	 */
	@Test
	public void emetteOrdinativiDiPagamentoDaElenco2() {
		
//		int[] uidsElenchi = new int[] {
//				415
//		};
		
		int[] uidsSubdoc = new int[] {
				911
		};
		
		//EmetteOrdinativiDiPagamentoDaElenco req = creaRequestPerEmissioneElenchi(uidsElenchi);
		
		EmetteOrdinativiDiPagamentoDaElenco req = creaRequestPerEmissioneQuote(uidsSubdoc);
		
		//AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco> reqWrapper = wrapRequestToAsync(req);
		AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco> reqWrapper = wrapRequestToAsync(req);
		reqWrapper.getAzioneRichiesta().getAzione().setUid(4615);
		reqWrapper.getAzioneRichiesta().setUid(66039037);
		AsyncServiceResponse res = service.emetteOrdinativiDiPagamentoDaElenco(reqWrapper);
//		EmetteOrdinativiDiPagamentoDaElencoResponse res = service.emetteOrdinativiDiPagamentoDaElencoSync(req);
		assertNotNull(res);
	}
	
	
	/**
	 * Crea request per emissione quote.
	 *
	 * @param uids the uids
	 * @return the emette ordinativi di pagamento da elenco
	 */
	public EmetteOrdinativiDiPagamentoDaElenco creaRequestPerEmissioneQuote(int[] uids){
		EmetteOrdinativiDiPagamentoDaElenco req = popolaCampiComuniReq();
		
	
		List<SubdocumentoSpesa> subdocumenti = new ArrayList<SubdocumentoSpesa>();
//		int[] uids = new int[] {
//				390
//		};
		
		for(int uid : uids) {
			SubdocumentoSpesa subdoc = new SubdocumentoSpesa();
			subdoc.setUid(uid);
			subdocumenti.add(subdoc);
		}
		req.setSubdocumenti(subdocumenti);
		return req;
	}
	
	
	/**
	 * Crea request per emissione elenchi.
	 *
	 * @param uids the uids
	 * @return the emette ordinativi di pagamento da elenco
	 */
	public EmetteOrdinativiDiPagamentoDaElenco creaRequestPerEmissioneElenchi(int[] uids){
		EmetteOrdinativiDiPagamentoDaElenco req = popolaCampiComuniReq();
		
	
		List<ElencoDocumentiAllegato> elenchi = new ArrayList<ElencoDocumentiAllegato>();
//		int[] uids = new int[] {
//				390
//		};
		
		for(int uid : uids) {
			ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
			eda.setUid(uid);
			elenchi.add(eda);
		}
		req.setElenchi(elenchi);
		return req;
	}


	/**
	 * Popola campi comuni req.
	 *
	 * @return the emette ordinativi di pagamento da elenco
	 */
	private EmetteOrdinativiDiPagamentoDaElenco popolaCampiComuniReq() {
		EmetteOrdinativiDiPagamentoDaElenco req = new EmetteOrdinativiDiPagamentoDaElenco();
		req.setDataOra(new Date());
		req.setBilancio(getBilancio(16, 2015));
		req.setFlagConvalidaManuale(Boolean.TRUE);
		req.setRichiedente(getRichiedenteTest());
		return req;
	}
	
		
}
