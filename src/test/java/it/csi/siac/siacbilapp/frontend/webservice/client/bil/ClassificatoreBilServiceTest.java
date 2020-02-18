/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.codifica.ElementoCodificaFactory;
import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiopeResponse;

/**
 * @author Marchino Alessandro
 *
 */
public class ClassificatoreBilServiceTest extends BaseProxyServiceTest<ClassificatoreBilService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/ClassificatoreBilService";
		// return "http://tst-www.ruparpiemonte.it/siacbilser/ClassificatoreBilService";
	}
	
	/**
	 * Test.
	 */
	@Test
	public void pianoDeiConti() {
		LeggiTreePianoDeiConti request = new LeggiTreePianoDeiConti();
		request.setAnno(2013);
		request.setDataOra(new Date());
		request.setIdCodificaPadre(119);
		request.setIdEnteProprietario(1);
		
		request.setRichiedente(getRichiedenteTest());
		
		LeggiTreePianoDeiContiResponse response = service.leggiTreePianoDeiConti(request);
		log.debug("testPianoDeiConti", ToStringBuilder.reflectionToString(response, ToStringStyle.MULTI_LINE_STYLE));
		log.debug("testPianoDeiConti", ElementoCodificaFactory.getInstances(response.getTreeElementoPianoDeiConti()));
	}
	
	/**
	 * Test.
	 */
	@Test
	public void siopeSpesa() {
		LeggiTreeSiope request = new LeggiTreeSiope();
		request.setAnno(2013);
		request.setDataOra(new Date());
		request.setIdCodificaPadre(7433);
		request.setIdEnteProprietario(1);
		
		request.setRichiedente(getRichiedenteTest());
		
		LeggiTreeSiopeResponse res = service.leggiTreeSiopeSpesa(request);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
	/**
	 * Test
	 */
	@Test
	public void missione() {
		LeggiClassificatoriByTipoElementoBil request = new LeggiClassificatoriByTipoElementoBil();
		request.setRichiedente(getRichiedenteTest());
		request.setDataOra(new Date());
		
		request.setAnno(2013);
		request.setIdEnteProprietario(1);
		request.setTipoElementoBilancio(BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE.getConstant());
		
		LeggiClassificatoriByTipoElementoBilResponse response = service.leggiClassificatoriByTipoElementoBil(request);
		
		log.debug("testMissione", ToStringBuilder.reflectionToString(response, ToStringStyle.MULTI_LINE_STYLE));
		log.debug("testMissione", "Missioni trovate: " + response.getClassificatoriMissione().size());
	}
	
	/**
	 * Test
	 */
	@Test
	public void titoloSpesa() {
		LeggiClassificatoriByTipoElementoBil request = new LeggiClassificatoriByTipoElementoBil();
		request.setRichiedente(getRichiedenteTest());
		request.setDataOra(new Date());
		
		request.setAnno(2013);
		request.setIdEnteProprietario(1);
		request.setTipoElementoBilancio(BilConstants.CODICE_TITOLO_SPESA.getConstant());
		
		LeggiClassificatoriByTipoElementoBilResponse response = service.leggiClassificatoriByTipoElementoBil(request);
		log.debug("testTitoloSpesa", ToStringBuilder.reflectionToString(response, ToStringStyle.MULTI_LINE_STYLE));
		log.debug("testTitoloSpesa", "Titoli di spesa trovati: " + response.getClassificatoriMissione().size());
	}
	
	/**
	 * Test
	 */
	@Test
	public void classificatoreGenerico() {
		LeggiClassificatoriGenericiByTipoElementoBil request = new LeggiClassificatoriGenericiByTipoElementoBil();
		request.setAnno(2014);
		request.setDataOra(new Date());
		request.setIdEnteProprietario(1);
		request.setRichiedente(getRichiedenteTest());
		request.setTipoElementoBilancio(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
		
		LeggiClassificatoriGenericiByTipoElementoBilResponse response = 
				service.leggiClassificatoriGenericiByTipoElementoBil(request);
		log.debug("testClassificatoreGenerico", "Classificatori Generici trovati: " + response.getClassificatoriGenerici1().size());
		log.logXmlTypeObject(response, "RESPONSE");
	}
	
	/**
	 * Test
	 */
	@Test
	public void byPadre() {
		LeggiClassificatoriBilByIdPadre request = new LeggiClassificatoriBilByIdPadre();
		request.setAnno(2013);
		request.setDataOra(new Date());
		request.setIdEnteProprietario(1);
		request.setIdPadre(16);
		request.setRichiedente(getRichiedenteTest());
		
		LeggiClassificatoriBilByIdPadreResponse response = service.leggiClassificatoriByIdPadre(request);
		log.debug("testByPadre", ToStringBuilder.reflectionToString(response, ToStringStyle.MULTI_LINE_STYLE));
	}
	
	/**
	 * Test
	 */
	@Test
	public void tipoFinanziamento() {
		final String methodName = "tipoFinanziamento";
		LeggiClassificatoriGenericiByTipoElementoBil request = new LeggiClassificatoriGenericiByTipoElementoBil();
		request.setAnno(2013);
		request.setDataOra(new Date());
		request.setIdEnteProprietario(1);
		request.setRichiedente(getRichiedenteTest());
		request.setTipoElementoBilancio(BilConstants.CODICE_CAPITOLO_ENTRATA_PREVISIONE.getConstant());
		
		LeggiClassificatoriGenericiByTipoElementoBilResponse response = 
				service.leggiClassificatoriGenericiByTipoElementoBil(request);
		log.info(methodName, ToStringBuilder.reflectionToString(response, ToStringStyle.MULTI_LINE_STYLE));
		assertTrue(response.getClassificatoriTipoFinanziamento().size() > 0);
	}
	
	/**
	 * Test
	 */
	@Test
	public void leggiClassificatoriGenericiByTipoElementoBil() {
		LeggiClassificatoriGenericiByTipoElementoBil request = new LeggiClassificatoriGenericiByTipoElementoBil();
		request.setAnno(2015);
		request.setDataOra(new Date());
		request.setIdEnteProprietario(1);
		request.setRichiedente(getRichiedenteTest());
		request.setTipoElementoBilancio(BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE.getConstant());
		
		LeggiClassificatoriGenericiByTipoElementoBilResponse response = service.leggiClassificatoriGenericiByTipoElementoBil(request);
		logResponse(response);
	}
	
}
