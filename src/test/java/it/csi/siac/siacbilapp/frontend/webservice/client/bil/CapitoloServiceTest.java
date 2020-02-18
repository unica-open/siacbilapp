/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * Classe di test per i serviz&icirc; deployati del Progetto.
 * 
 * @author Marchino Alessandro
 *
 */
public class CapitoloServiceTest extends BaseProxyServiceTest<CapitoloService> {
	
	@Override
	protected String getEndpoint() {
		//return "http://consip-www.ruparpiemonte.it/siacbilser/CapitoloService";
		return "http://tst-srv-consip.bilancio.csi.it/siacbilser/CapitoloService";
	}
	
	
	/**
	 * Test
	 */
	@Test
	public void controllaClassificatoriModificabiliCapitolo() {
		ControllaClassificatoriModificabiliCapitolo request = new ControllaClassificatoriModificabiliCapitolo();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		request.setEnte(request.getRichiedente().getAccount().getEnte());
		//request.setAnnoBilancio(Integer.valueOf(2018));
		
		request.setNumeroCapitolo(Integer.valueOf(111111117));
		request.setNumeroArticolo(Integer.valueOf(88));
 		request.setNumeroUEB(1);
		//111111117
//		request.setNumeroCapitolo(156);
//		request.setNumeroArticolo(1);
//		request.setNumeroUEB(1);
		request.setTipoCapitolo(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE);
		
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(134);
		bilancio.setAnno(2018);
		request.setBilancio(bilancio);
		log.logXmlTypeObject(request, "REQUEST");
		
		ControllaClassificatoriModificabiliCapitoloResponse response = service.controllaClassificatoriModificabiliCapitolo(request);
		log.logXmlTypeObject(response, "RESPONSE");
		
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaVariazioniCapitoloPerAggiornamentoCapitolo() {
		RicercaVariazioniCapitoloPerAggiornamentoCapitolo request = new RicercaVariazioniCapitoloPerAggiornamentoCapitolo();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest());
		request.setUidCapitolo(Integer.valueOf(30994));
		
		RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse response = service.ricercaVariazioniCapitoloPerAggiornamentoCapitolo(request);
		logResponse(response);
	}
	
//	public static void main(String[] args) {
//		Object[] ob2 = new Object[] {12, "elemCode-12", "elemCode2-12", "elemCode3-12", "a", 13, 14, "b", 46, new BigDecimal("15")};
//		Object[] ob3 = new Object[] {12, "elemCode-12", "elemCode2-12", "elemCode3-12", "a", 15, 16, "b", 46, new BigDecimal("15")};
//		Object[] ob4 = new Object[] {1456, "elemCode-1456", "elemCode2-1456", "elemCode3-1456", "a", 15, 16, "b", 46, new BigDecimal("15")};
//		Map<Capitolo<?,?>, List<SubdocumentoSpesa>> mappa = 
//				new TreeMap<Capitolo<?,?>, List<SubdocumentoSpesa>>(ComparatorEntitaByUid<Entita>);
//	}
	
}
