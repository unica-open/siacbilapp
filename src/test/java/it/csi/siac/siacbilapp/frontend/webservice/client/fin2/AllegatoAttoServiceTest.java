/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegato;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenchiPerAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenchiPerAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchi;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Test per i servizi dell'Allegato Atto.
 * 
 * @author Marchino Alessandro
 *
 */
public class AllegatoAttoServiceTest extends BaseProxyServiceTest<AllegatoAttoService> {
	
	@Override
	protected String getEndpoint() {
		return "http://tst-srv-consip.bilancio.csi.it/siacbilser/AllegatoAttoService";
	}
	
	@Override
	protected String getNamespaceUri() {
		return FIN2SvcDictionary.NAMESPACE;
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaElenco() {
		RicercaElenco req = new RicercaElenco();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		elencoDocumentiAllegato.setNumero(Integer.valueOf(2014));
		req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		RicercaElencoResponse res = service.ricercaElenco(req);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
	/**
	 * Ricerca di dettaglio dell'elenco
	 */
	@Test
	public void ricercaDettaglioElenco() {
		RicercaDettaglioElenco req = new RicercaDettaglioElenco();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setElencoDocumentiAllegato(create(ElencoDocumentiAllegato.class, 10122));
		
		RicercaDettaglioElencoResponse res = service.ricercaDettaglioElenco(req);
		assertNotNull(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void inserisceElencoConDocumentiConQuote() {
		InserisceElenco req = new InserisceElenco();
		InserisceElencoResponse res = service.inserisceElencoConDocumentiConQuote(req);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
	/**
	 * Test
	 */
	@Test
	public void annullaAllegatoAtto() {
		AnnullaAllegatoAtto req = new AnnullaAllegatoAtto();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setEnte(getEnteTest());
		allegatoAtto.setUid(38);
		req.setAllegatoAtto(allegatoAtto);
		
		AnnullaAllegatoAttoResponse res = service.annullaAllegatoAtto(req);
		logResponse(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void rifiutaElenchi() {
		RifiutaElenchi req = new RifiutaElenchi();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(41);
		allegatoAtto.setStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.RIFIUTATO);
		allegatoAtto.setEnte(getEnteTest());
		req.setAllegatoAtto(allegatoAtto);
		
		AsyncServiceRequestWrapper<RifiutaElenchi> asyncReq = wrapRequestToAsync(req);
		
		AsyncServiceResponse res = service.rifiutaElenchiAsync(asyncReq);
		logResponse(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void completaAllegatoAtto() {
		CompletaAllegatoAtto req = new CompletaAllegatoAtto();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		
		req.setBilancio(getBilancio(6, 2015));
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(39);
		allegatoAtto.setEnte(getEnteTest());
		req.setAllegatoAtto(allegatoAtto);
		
		AsyncServiceRequestWrapper<CompletaAllegatoAtto> asyncReq = wrapRequestToAsync(req);
		
		AsyncServiceResponse res = service.completaAllegatoAttoAsync(asyncReq);
		logResponse(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaElenchiPerAllegatoAtto() {
		RicercaElenchiPerAllegatoAtto request = new RicercaElenchiPerAllegatoAtto();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(154);
		request.setAllegatoAtto(allegatoAtto);
		
		RicercaElenchiPerAllegatoAttoResponse response = service.ricercaElenchiPerAllegatoAtto(request);
		logResponse(response);
	}
	
	/**
	 * RicercaDettaglioQuoteElenco
	 */
	@Test
	public void ricercaDettaglioQuoteElenco() {
		final String methodName = "ricercaDettaglioQuoteElenco";
		RicercaSinteticaQuoteElenco req = new RicercaSinteticaQuoteElenco();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		elencoDocumentiAllegato.setUid(355);
		req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		
		RicercaSinteticaQuoteElencoResponse res = service.ricercaSinteticaQuoteElenco(req);
		assertNotNull(res);
		
		List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> list = new ArrayList<ElementoElencoDocumentiAllegato<?,?,?,?,?>>();
		List<DatiSoggettoAllegato> dsa = new ArrayList<DatiSoggettoAllegato>();
		for(Subdocumento<?, ?> subdoc : res.getSubdocumenti()) {
			ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> eeda = ElementoElencoDocumentiAllegatoFactory.getInstance(subdoc, dsa, elencoDocumentiAllegato, false);
			list.add(eeda);
		}
		
		for(ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> eeda : list) {
			log.debug(methodName, eeda.getUidDocumento() + " => " + eeda.getNote() + " --- " + eeda.getDettagliNote());
		}
		
	}
	
	/**
	 * Crea elenchi per test di emissione.
	 */
	@Test
	public void creaElenchiPerTestDiEmissione(){
		
		//for(int i = 0; i<3; i++){
			InserisceElenco req = new InserisceElenco();
			req.setDataOra(new Date());
			req.setRichiedente(getRichiedenteTest());
			req.setBilancio(getBilancio(16, 2015));		
			
			ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
			List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?,?>>();
			
			Soggetto sog = new Soggetto();
			sog.setUid(14);
			sog.setCodiceSoggetto("");
			
			Impegno impegno = new Impegno();
			impegno.setUid(95593);
			impegno.setSoggetto(sog);
			
			ModalitaPagamentoSoggetto mdp = new ModalitaPagamentoSoggetto();
			mdp.setUid(27);
			
			for(int j = 0; j<20; j++ ){
				SubdocumentoSpesa subdocumento = new SubdocumentoSpesa();
				subdocumento.setImporto(new BigDecimal("0.01"));
				subdocumento.setImpegno(impegno);
				subdocumento.setModalitaPagamentoSoggetto(mdp);
				subdocumenti.add(subdocumento);
			}
			elencoDocumentiAllegato.setSubdocumenti(subdocumenti);
			elencoDocumentiAllegato.setAnno(Integer.valueOf(req.getBilancio().getAnno()));
			
			//elencoDocumentiAllegato.setSubdocumenti(new ArrayList<Subdocumento<?,?>>());
			
			AllegatoAtto al = new AllegatoAtto();
			al.setUid(283);
			
			elencoDocumentiAllegato.setAllegatoAtto(al);
			elencoDocumentiAllegato.setEnte(req.getRichiedente().getAccount().getEnte());
			elencoDocumentiAllegato.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
			
			req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
			InserisceElencoResponse re = service.inserisceElencoConDocumentiConQuote(req);
			assertNotNull(re);
			assertTrue(re.getErrori().isEmpty());
		//}
		
	}
	/**
	 * Completamento dell'allegato atto in sincrono
	 */
	@Test
	public void completaAllegatoAttoSync() {
		CompletaAllegatoAtto reqCAA = new CompletaAllegatoAtto();
		reqCAA.setDataOra(new Date());
		reqCAA.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqCAA.setBilancio(getBilancio(132, 2018));
		AllegatoAtto allegato = new AllegatoAtto();
		allegato.setUid(11636);
		reqCAA.setAllegatoAtto(allegato);		
		CompletaAllegatoAttoResponse res = service.completaAllegatoAtto(reqCAA);
		assertNotNull(res);
	}
	
	/**
	 * Convalida dell'allegato atto
	 */
	@Test
	public void convalidaAllegatoAtto(){
		RicercaElenchiPerAllegatoAtto request = new RicercaElenchiPerAllegatoAtto();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(266);
		request.setAllegatoAtto(allegatoAtto);
		
		RicercaElenchiPerAllegatoAttoResponse response = service.ricercaElenchiPerAllegatoAtto(request);
		assertNotNull(response);
		assertTrue(response.getErrori().isEmpty());
		
		allegatoAtto.setElenchiDocumentiAllegato(response.getElenchiDocumentiAllegato());
		allegatoAtto.setEnte(request.getRichiedente().getAccount().getEnte());
		
		ConvalidaAllegatoAttoPerElenchi reqC = new ConvalidaAllegatoAttoPerElenchi();
		reqC.setDataOra(new Date());
		reqC.setAllegatoAtto(allegatoAtto);
		reqC.setFlagConvalidaManuale(Boolean.TRUE);
		ConvalidaAllegatoAttoPerElenchiResponse respC = service.convalidaAllegatoAttoPerElenchi(reqC);
		assertNotNull(respC);
	}
}
