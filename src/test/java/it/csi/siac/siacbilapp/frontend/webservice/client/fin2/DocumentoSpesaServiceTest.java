/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin2;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;

/**
 * Test per i servizi del Documento.
 * 
 * @author Marchino Alessandro
 *
 */
public class DocumentoSpesaServiceTest extends BaseProxyServiceTest<DocumentoSpesaService> {
	
	@Override
	protected String getEndpoint() {
		//return "http://10.136.6.151/siacbilser/DocumentoSpesaService";
//		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/DocumentoSpesaService";
		return "http://tst-srv-consip.bilancio.csi.it/siacbilser/DocumentoSpesaService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglio() {
		RicercaDettaglioDocumentoSpesa request = new RicercaDettaglioDocumentoSpesa();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(1294);
		request.setDocumentoSpesa(documentoSpesa);
		
		RicercaDettaglioDocumentoSpesaResponse response = service.ricercaDettaglioDocumentoSpesa(request);
		assertNotNull(response);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaModulare() {
		final String methodName = "ricercaModulare";
		RicercaModulareDocumentoSpesa request = new RicercaModulareDocumentoSpesa();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(1013);
		request.setDocumentoSpesa(documentoSpesa);
		
		request.setDocumentoSpesaModelDetails(
//				DocumentoSpesaModelDetail.Attr,
//				DocumentoSpesaModelDetail.Soggetto,
//				DocumentoSpesaModelDetail.Classif,
				//DocumentoSpesaModelDetail.CodiceBollo,
//				DocumentoSpesaModelDetail.CodicePCC,
//				DocumentoSpesaModelDetail.CodiceUfficioDestinatarioPCC,
//				DocumentoSpesaModelDetail.CollegatoAdAllegatoAtto,
//				DocumentoSpesaModelDetail.DataInizioValiditaStato,
//				DocumentoSpesaModelDetail.FatturaFEL,
//				DocumentoSpesaModelDetail.ImportoDaPagareNonPagatoInCassaEconomale,
//				DocumentoSpesaModelDetail.RegistroComunicazioniPCC,
//				DocumentoSpesaModelDetail.RegistroUnico,
				//DocumentoSpesaModelDetail.Stato,
				DocumentoSpesaModelDetail.EsisteNCDCollegataADocumento);
		
		RicercaModulareDocumentoSpesaResponse response = service.ricercaModulareDocumentoSpesa(request);
		assertNotNull(response);
		log.info(methodName, "esiste NCD collegata a documento? " + response.getDocumento().getEsisteNCDCollegataADocumento());
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaQuoteByDocumento() {
		RicercaQuoteByDocumentoSpesa request = new RicercaQuoteByDocumentoSpesa();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(3);
		request.setDocumentoSpesa(documentoSpesa);
		
		RicercaQuoteByDocumentoSpesaResponse response = service.ricercaQuoteByDocumentoSpesa(request);
		logResponse(response);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaCollegati() {
		final String methodName = "testRicercaCollegati";
		RicercaDocumentiCollegatiByDocumentoSpesa req = new RicercaDocumentiCollegatiByDocumentoSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(25);
		req.setDocumentoSpesa(doc);
		
		RicercaDocumentiCollegatiByDocumentoSpesaResponse res = service.ricercaDocumentiCollegatiByDocumentoSpesa(req);
		log.logXmlTypeObject(res, "RESPONSE");
		log.debug(methodName, "Spese collegate: " + res.getDocumentiSpesaFiglio().size());
	}
	
	/**
	 * Test
	 */
	@Test
	public void aggiornamentoQuota() {
		RicercaDettaglioDocumentoSpesa request = new RicercaDettaglioDocumentoSpesa();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(25);
		request.setDocumentoSpesa(documentoSpesa);
		
		RicercaDettaglioDocumentoSpesaResponse response = service.ricercaDettaglioDocumentoSpesa(request);
		documentoSpesa = response.getDocumento();
		SubdocumentoSpesa subdocumentoSpesa = documentoSpesa.getListaSubdocumenti().get(0);
		
		SubImpegno si = new SubImpegno();
		si.setUid(5);
		
		subdocumentoSpesa.setSubImpegno(si);
		
		AggiornaQuotaDocumentoSpesa req = new AggiornaQuotaDocumentoSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		req.setSubdocumentoSpesa(subdocumentoSpesa);
		
		AggiornaQuotaDocumentoSpesaResponse res = service.aggiornaQuotaDocumentoSpesa(req);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaQuoteSpesa() {
		RicercaQuotaSpesa request = new RicercaQuotaSpesa();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		request.setAnnoDocumento(Integer.valueOf(2013));
		request.setNumeroDocumento("333");
		request.setEnte(getEnteTest());
		request.setParametriPaginazione(getParametriPaginazioneTest());
		
		
		// Il documento relativo alla quota deve essere in uno di questi stati: VALIDO, PARZIALMENTE LIQUIDATO, PARZIALMENTE EMESSO.
		request.setStatiOperativoDocumento(Arrays.asList(StatoOperativoDocumento.VALIDO, StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO,
				StatoOperativoDocumento.PARZIALMENTE_EMESSO));
		// Devono essere necessariamente collegate ad un movimento e il movimento deve appartenere allo stesso bilancio su cui si sta operando.
		request.setCollegatoAMovimentoDelloStessoBilancio(Boolean.TRUE);
		// Non devono associate ad un provvedimento o ad un elenco di documenti.
		request.setAssociatoAProvvedimentoOAdElenco(Boolean.FALSE);
		// Devono avere importoDaPagare o importoDaIncassare diverso da zero.
		request.setImportoDaPagareZero(Boolean.FALSE);
		// Se la quota e' di spesa ed e' rilevante IVA (flagrilevanteIVA uguale a true) deve avere il numero di registrazione IVA valorizzato
		// altrimenti non si puo' visualizzare nell'elenco. Non e' necessario verificare se l'Ente gestisce oppure no l'IVA perche' il flag
		// puo' essere valorizzato a true solamente se l'Ente permette la gestione IVA.
		request.setRilevatiIvaConRegistrazioneONonRilevantiIva(Boolean.TRUE);
		
		RicercaQuotaSpesaResponse response = service.ricercaQuotaSpesa(request);
		log.logXmlTypeObject(response, "RESPONSE");
		assertFalse(response.hasErrori());
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioQuotaSpesa() {
		RicercaDettaglioQuotaSpesa request = new RicercaDettaglioQuotaSpesa();
		request.setRichiedente(getRichiedenteTest());
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(1887);
		request.setSubdocumentoSpesa(subdocumentoSpesa);
		RicercaDettaglioQuotaSpesaResponse response = service.ricercaDettaglioQuotaSpesa(request);
		log.logXmlTypeObject(response, "RESPONSE");
		assertFalse(response.hasErrori());
	}
	
	
	/**
	 * Test
	 */
	@Test
	public void aggiornamentoQuotaAhmad() {
		RicercaDettaglioDocumentoSpesa request = new RicercaDettaglioDocumentoSpesa();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(456);
		request.setDocumentoSpesa(documentoSpesa);
		
		RicercaDettaglioDocumentoSpesaResponse response = service.ricercaDettaglioDocumentoSpesa(request);
		documentoSpesa = response.getDocumento();
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(542);
		SubImpegno si = new SubImpegno();
		si.setUid(62381);
		subdocumentoSpesa.setEnte(getEnteTest());
		Impegno impegno = new Impegno();
		impegno.setAnnoMovimento(2015);
		impegno.setNumero(new BigDecimal("2227"));
		subdocumentoSpesa.setImpegno(impegno);
		//subdocumentoSpesa.setSubImpegno(si);
		subdocumentoSpesa.setDocumento(documentoSpesa);
		subdocumentoSpesa.setNumero(Integer.valueOf(2));
		VoceMutuo voceMutuo = new VoceMutuo();
		voceMutuo.setNumeroMutuo("4");
		subdocumentoSpesa.setVoceMutuo(voceMutuo);
		AggiornaQuotaDocumentoSpesa req = new AggiornaQuotaDocumentoSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		req.setBilancio(getBilancio(6, 2015));
		req.setSubdocumentoSpesa(subdocumentoSpesa);
		
	//	log.logXmlTypeObject(req, "REQUEST");

		AggiornaQuotaDocumentoSpesaResponse res = service.aggiornaQuotaDocumentoSpesa(req);
		assertNotNull(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSinteticaQuoteByDocumentoSpesa() {
		RicercaSinteticaModulareQuoteByDocumentoSpesa req = new RicercaSinteticaModulareQuoteByDocumentoSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(1013);
		req.setDocumentoSpesa(documentoSpesa);
		RicercaSinteticaModulareQuoteByDocumentoSpesaResponse res = service.ricercaSinteticaModulareQuoteByDocumentoSpesa(req);
		logResponse(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSinteticaDocumentoSpesa() {
		RicercaSinteticaDocumentoSpesa req = new RicercaSinteticaDocumentoSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 349, 5));
		req.setParametriPaginazione(getParametriPaginazioneTest(10, 1));
		req.setCollegatoCEC(Boolean.TRUE);
		req.setContabilizzaGenPcc(Boolean.TRUE);
		
		DocumentoSpesa documento = new DocumentoSpesa();
		documento.setEnte(req.getRichiedente().getAccount().getEnte());
		
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setUid(13);
		documento.setTipoDocumento(tipoDocumento);
		req.setDocumentoSpesa(documento);
		
		RicercaSinteticaDocumentoSpesaResponse res = service.ricercaSinteticaDocumentoSpesa(req);
		assertNotNull(res);
	}
	
	/**
	 * Ricerca onere by documento spesa
	 */
	@Test
	public void ricercaOnereByDocumentoSpesa() {
		final String methodName = "ricercaOnereByDocumentoSpesa";
		RicercaOnereByDocumentoSpesa req = new RicercaOnereByDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(27961);
		req.setDocumentoSpesa(documentoSpesa);
		
		RicercaOnereByDocumentoSpesaResponse response = service.ricercaOnereByDocumentoSpesa(req);
		for (DettaglioOnere de : response.getListaDettagliOnere()) {
			log.info(methodName, "importo a carico soggetto: " + de.getImportoCaricoSoggetto());
			log.info(methodName, "importo imponibile " + de.getImportoImponibile());
		}
	}
}
