/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.core;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;

/**
 * Classe di test per le Azioni Richieste ottenute via WS.
 * 
 * @author Alessandro Marchino
 *
 */
public class CoreServiceAzioneRichiestaTest extends BaseProxyServiceTest<CoreService> {
	
	@Override
	protected String getEndpoint() {
		return "http://10.136.6.151:10080/siaccorser/CoreService";
	}
	
	/**
	 * Test per l'ottenimento dell'Azione Richiesta.
	 */
	@Test
	public void getAzioneRichiesta() {
		final String methodName = "testGetAzioneRichiesta";
		log.debugStart(methodName, "");
		log.debug(methodName, "Creazione richiedente ed ente");
		Richiedente richiedente = getRichiedenteByProperties("forn2", "regp");
		Ente ente = new Ente();
		ente.setUid(1);
		
		log.debug(methodName, "Creazione azione richiesta");
		AzioneRichiesta azioneRichiesta = getAzioneRichiestaTest();
		
		log.debug(methodName, "Creazione request");
		GetAzioneRichiesta request = new GetAzioneRichiesta();
		request.setAzioneRichiesta(azioneRichiesta);
		request.setRichiedente(richiedente);
		request.setDataOra(new Date());
		log.debug(methodName, "Invocazione del WS");
		GetAzioneRichiestaResponse response = service.getAzioneRichiesta(request);
		log.debug(methodName, "WS invocato");
		
		String risultato = ToStringBuilder.reflectionToString(response, ToStringStyle.MULTI_LINE_STYLE);
		
		checkGestioneLivelli(response.getAzioneRichiesta());
		log.debug(methodName, "Risultato ottenuto : " + risultato);
		assertNotNull("Response nulla dal WebService", response);
		assertEquals("La chiamata al WS si e' risolta in un fallimento", Esito.SUCCESSO, response.getEsito());
		log.debugEnd(methodName, "");
	}
	
	private void checkGestioneLivelli(AzioneRichiesta azioneRichiesta) {
		final String methodName = "checkGestioneLivelli";
		Map<TipologiaGestioneLivelli, String> gestioneLivelli = azioneRichiesta.getAccount().getEnte().getGestioneLivelli();
		for (TipologiaGestioneLivelli tgl : gestioneLivelli.keySet()) {
			log.info(methodName, tgl.name() + " : "  + gestioneLivelli.get(tgl));
		}
	}
	
	/**
	 * Crea un'Azione Richiesta di test.
	 * 
	 * @return l'Azione Richiesta creata 
	 */
	private AzioneRichiesta getAzioneRichiestaTest() {
		AzioneRichiesta ar = new AzioneRichiesta();
		ar.setUid(66165773);
		return ar;
	}
	
}
