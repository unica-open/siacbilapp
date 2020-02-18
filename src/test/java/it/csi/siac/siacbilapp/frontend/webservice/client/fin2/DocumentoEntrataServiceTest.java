/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin2;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;

/**
 * Test per i servizi del Documento.
 * 
 * @author Marchino Alessandro
 *
 */
public class DocumentoEntrataServiceTest extends BaseProxyServiceTest<DocumentoEntrataService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/DocumentoEntrataService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaDettaglioDocumentoEntrata() {
		RicercaDettaglioDocumentoEntrata request = new RicercaDettaglioDocumentoEntrata();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(563);
		request.setDocumentoEntrata(documentoEntrata);
		
		RicercaDettaglioDocumentoEntrataResponse response = service.ricercaDettaglioDocumentoEntrata(request);
		logResponse(response);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaQuoteEntrata() {
		RicercaQuotaEntrata request = new RicercaQuotaEntrata();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		request.setAnnoDocumento(Integer.valueOf(2014));
		request.setNumeroDocumento("1");
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
		request.setImportoDaIncassareZero(Boolean.FALSE);
		
		RicercaQuotaEntrataResponse response = service.ricercaQuotaEntrata(request);
		log.logXmlTypeObject(response, "RESPONSE");
		assertFalse(response.hasErrori());
	}
	
}
