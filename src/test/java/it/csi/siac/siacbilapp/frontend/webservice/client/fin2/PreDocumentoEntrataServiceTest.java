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

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Test per i servizi del PreDocumento di entraa.
 * 
 * @author Marchino Alessandro
 *
 */
public class PreDocumentoEntrataServiceTest extends BaseProxyServiceTest<PreDocumentoEntrataService> {
	
	@Override
	protected String getEndpoint() {
//		return "http://dev-www.ruparpiemonte.it/siacbilser/PreDocumentoEntrataService";
//		return "http://coll-srv1.bilancio.csi.it/siacbilser/PreDocumentoEntrataService";
		return "http://127.0.0.1:8080/siacbilser/PreDocumentoEntrataService";
//		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/PreDocumentoEntrataService";
	}
	
	/**
	 * Inserimento pre documento di entrata
	 */
	@Test
	public void inseriscePreDocumentoEntrata() {
		final String methodName = "inseriscePreDocumentoEntrata";
		InseriscePreDocumentoEntrata req = new InseriscePreDocumentoEntrata();
		
		req.setRichiedente(getRichiedenteTest());
		req.setDataOra(new Date());
		req.setBilancio(getBilancio(16, 2015));
		req.setGestisciModificaImportoAccertamento(true);
		
		PreDocumentoEntrata preDocumentoEntrata = new PreDocumentoEntrata();
		req.setPreDocumentoEntrata(preDocumentoEntrata);
		
		preDocumentoEntrata.setDatiAnagraficiPreDocumento(new DatiAnagraficiPreDocumento());
		preDocumentoEntrata.setDataCompetenza(formatDate("25/07/2015"));
		preDocumentoEntrata.setDataDocumento(formatDate("25/07/2015"));
		preDocumentoEntrata.setDescrizione("___ASSOCIA_IMPUTAZIONI_CONTABILI_VARIATE___");
		preDocumentoEntrata.setEnte(req.getRichiedente().getAccount().getEnte());
		preDocumentoEntrata.setFlagManuale(true);
		preDocumentoEntrata.setImporto(new BigDecimal("0.01"));
		preDocumentoEntrata.setNote("");
		preDocumentoEntrata.setPeriodoCompetenza("201507");
		preDocumentoEntrata.setProvvisorioDiCassa(new ProvvisorioDiCassa());
		preDocumentoEntrata.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDocumentoEntrata.setCodiceIUV("");
		
//		Accertamento accertamento = new Accertamento();
//		preDocumentoEntrata.setAccertamento(accertamento);
//		accertamento.setUid(95541);
//		accertamento.setAnnoMovimento(2015);
//		accertamento.setNumero(new BigDecimal("156"));
		
		CausaleEntrata causaleEntrata = new CausaleEntrata();
		preDocumentoEntrata.setCausaleEntrata(causaleEntrata);
		causaleEntrata.setUid(270);
		
		for(int i = 0; i < 250; i++) {
			InseriscePreDocumentoEntrataResponse res = service.inseriscePreDocumentoEntrata(req);
			log.debug(methodName, "Iterazione " + (i + 1));
			assertNotNull(res);
			assertTrue(res.getErrori().isEmpty());
		}
	}
	
//	@Override
//	protected Collection<Class<? extends Advice>> getAdvices() {
//		return new ArrayList<Class<? extends Advice>>();
//	}
//	
//	@Override
//	protected void addLogHandler(SiacJaxWsPortAdvisedProxyFactoryBean proxyFactoryBean) {
//	
//	}
	
	
	/**
	 * Associazione delle imputazioni contabili con variazione
	 */
	@Test
	public void associaImputazioniContabiliVariate() {
		AssociaImputazioniContabiliVariatePreDocumentoEntrata request = new AssociaImputazioniContabiliVariatePreDocumentoEntrata();
		request.setRichiedente(getRichiedenteTest());
		request.setDataOra(new Date());
		
		Soggetto sog = new Soggetto();
		sog.setUid(0);
		
		Accertamento acc = new Accertamento();
		//ACCERTAMENTO CON DISPONIBILITA'
//		acc.setUid(68156);
//		acc.setAnnoMovimento(2015);
//		acc.setNumero( new BigDecimal("2"));
		//ACCERTAMENTO SENZA DISPONIBILITA'
		acc.setUid(95637);
		acc.setAnnoMovimento(2015);
		acc.setNumeroBigDecimal( new BigDecimal("185"));
		
		SubAccertamento subacc = new SubAccertamento();
		subacc.setUid(0);
		
		CapitoloEntrataGestione cap = new CapitoloEntrataGestione();
		cap.setUid(0);
		
		AttoAmministrativo aa = new AttoAmministrativo();
		aa.setUid(0);
		
		List<PreDocumentoEntrata> preDocs = new ArrayList<PreDocumentoEntrata>();
		
		int start = 433;
		int delta = 50;
		//fatti non foste a viver come bruti, ma per seguir virtute e canoscenza: 
		for (int i = start; i < start + delta; i++) {
			PreDocumentoEntrata preDoc = create(PreDocumentoEntrata.class, i);
			preDocs.add(preDoc);
		}
		
		request.setPreDocumentiEntrata(preDocs);
		request.setBilancio(getBilancio(16, 2015));
		
//		if(Boolean.TRUE.equals(getInviaTutti())) {
//			request.setRicercaSinteticaPreDocumentoEntrata(ricercaSinteticaPreDocumentoEntrata);
//		}
		
		request.setCapitoloEntrataGestione(cap);
		request.setAccertamento(acc);
		request.setSubAccertamento(subacc);
		request.setSoggetto(sog);
		request.setAttoAmministrativo(aa);
		request.setGestisciModificaImportoAccertamento(true);
			
		AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse response = service.associaImputazioniContabiliVariatePreDocumentoEntrata(request);
		assertNotNull(response);
		assertTrue(response.getErrori().isEmpty());
	}
}
