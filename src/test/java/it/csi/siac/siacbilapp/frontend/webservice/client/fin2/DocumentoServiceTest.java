/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin2;

import java.lang.reflect.Method;
import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770Response;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBollo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBolloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Test per i servizi del Documento.
 * 
 * @author Marchino Alessandro
 *
 */
public class DocumentoServiceTest extends BaseProxyServiceTest<DocumentoService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/DocumentoService";
	}
	
	/**
	 * Test per la ricerca dei Codici Bollo
	 */
	@Test
	public void ricercaCodiceBollo() {
		RicercaCodiceBollo request = new RicercaCodiceBollo();
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest());
		
		RicercaCodiceBolloResponse response = service.ricercaCodiceBollo(request);
		log.logXmlTypeObject(response, "Response");
	}
	
	/**
	 * Test per la ricerca dei Codici Bollo
	 */
	@Test
	public void ricercaCodiceUfficioDestinatarioPCC() {
		RicercaCodiceUfficioDestinatarioPCC request = new RicercaCodiceUfficioDestinatarioPCC();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		
		RicercaCodiceUfficioDestinatarioPCCResponse response = service.ricercaCodiceUfficioDestinatarioPCC(request);
		log.logXmlTypeObject(response, "Response");
	}
	
	/**
	 * Test
	 */
	@Test
	public void tipoDocumento() {
		RicercaTipoDocumento request = new RicercaTipoDocumento();
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest(getEnteTest()));
		request.setTipoFamDoc(TipoFamigliaDocumento.SPESA);
//		request.setFlagSubordinato(Boolean.FALSE);
//		request.setFlagRegolarizzazione(Boolean.FALSE);
		logRequest(request);
		
		RicercaTipoDocumentoResponse response = service.ricercaTipoDocumento(request);
		logResponse(response);
		for(TipoDocumento td : response.getElencoTipiDocumento()) {
			System.out.printf("%4s (%-40s) FAM: %-20s RIT: %1s NCD: %1s PEN: %1s COLL: %1s IVA: %1s SUB: %1s REG: %1s GEN: %1s PCC: %1s TOTALE VERE: %d -- GRUPPO: %s%n", 
				td.getCodice(),
				td.getDescrizione(),
				td.getTipoFamigliaDocumento(),
				booleanToString(td.getFlagRitenute()),
				booleanToString(td.getFlagNotaCredito()),
				booleanToString(td.getFlagPenaleAltro()),
				booleanToString(td.getFlagSpesaCollegata()),
				booleanToString(td.getFlagIVA()),
				booleanToString(td.getFlagSubordinato()),
				booleanToString(td.getFlagRegolarizzazione()),
				booleanToString(td.getFlagAttivaGEN()),
				booleanToString(td.getFlagComunicaPCC()),
				countTrue(td),
				td.getCodiceGruppo());
		}
	}
	
	/**
	 * Test
	 */
	@Test
	public void noteTesoriere() {
		RicercaNoteTesoriere request = new RicercaNoteTesoriere();
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest());
		
		RicercaNoteTesoriereResponse response = service.ricercaNoteTesoriere(request);
		log.logXmlTypeObject(response, "RESPONSE");
		assertFalse(response.getElencoNoteTesoriere().isEmpty());
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaTipoOnere() {
		RicercaTipoOnere request = new RicercaTipoOnere();
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest());
		
		NaturaOnere naturaOnere = new NaturaOnere();
		naturaOnere.setUid(1);
		request.setNaturaOnere(naturaOnere);
		
		RicercaTipoOnereResponse response = service.ricercaTipoOnere(request);
		log.logXmlTypeObject(response, "RESPONSE");
	}
	

	/**
	 * Test
	 */
	@Test
	public void ricercaCausale770() {
		RicercaCausale770 request = new RicercaCausale770();
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest());
		
		TipoOnere tipoOnere = new TipoOnere();
		tipoOnere.setUid(4);
		request.setTipoOnere(tipoOnere);
		
		RicercaCausale770Response response = service.ricercaCausale770(request);
		log.logXmlTypeObject(response, "RESPONSE");
	}
	
	
	
	
	
	
	
	
	
	
	
	private String booleanToString(Boolean b) {
		return b == null ? "N" : Boolean.TRUE.equals(b) ? "T" : "F";
	}
	
	private Integer countTrue(TipoDocumento td) {
		int count = 0;
		for(Method m : td.getClass().getDeclaredMethods()) {
			if(Boolean.class.equals(m.getReturnType())) {
				try {
					Boolean res = (Boolean) m.invoke(td);
					if(Boolean.TRUE.equals(res)) {
						count++;
					}
				} catch (Exception e) {
					// Non incremento
				}
			}
		}
		return Integer.valueOf(count);
	}
	
}
