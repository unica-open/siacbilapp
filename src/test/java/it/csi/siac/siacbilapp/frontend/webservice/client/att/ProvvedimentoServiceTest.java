/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.att;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Test per il CommonService.
 * 
 * @author Marchino Alessandro
 *
 */
public class ProvvedimentoServiceTest extends BaseProxyServiceTest<ProvvedimentoService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/ProvvedimentoService";
		//return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/ProvvedimentoService";
	}

	/**
	 * Test
	 */
	@Test
	public void ricercaProvvedimento() {
		RicercaProvvedimento req = new RicercaProvvedimento();
		req.setDataOra(new Date());
		/*req.setRichiedente(getRichiedenteTest("AAAAAA00A11L000T", 51, 5));*/
		req.setRichiedente(getRichiedenteTest());
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		RicercaAtti ra = new RicercaAtti();
		req.setRicercaAtti(ra);
		//ra.setAnnoAtto(2016);
		//ra.setNumeroAtto(3);
		
		TipoAtto tipoAtto = new TipoAtto();
		tipoAtto.setUid(350);
		ra.setTipoAtto(tipoAtto);
		
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
		strutturaAmministrativoContabile.setUid(62958250);
		ra.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
		
		RicercaProvvedimentoResponse res = service.ricercaProvvedimento(req);
		assertNotNull(res);
		
		/*
		 * RicercaAtti utility = new RicercaAtti();
		
		// Controllo che il provvedimento sia stato inizializzato
		if(getAttoAmministrativo() != null) {
			// L'anno e' obbligatorio, dunque lo injetto sempre
			utility.setAnnoAtto(getAttoAmministrativo().getAnno());
			
			int numeroAtto = getAttoAmministrativo().getNumero();
			String note = getAttoAmministrativo().getNote();
			String oggetto = getAttoAmministrativo().getOggetto();
			
			// Injetto il numero dell'atto se e' stato inizializzato
			if(numeroAtto != 0) {
				utility.setNumeroAtto(numeroAtto);
			}
			
			// Injetto le note se sono state inizializzate
			if(StringUtils.isNotBlank(note)) {
				utility.setNote(note);
			}
			
			// Injetto l'oggetto se e' stato inizializzato
			if(StringUtils.isNotBlank(oggetto)) {
				utility.setOggetto(oggetto);
			}
		}
		
		
		// Injetto il tipo di atto se e solo se e' stato inizializzato
		if(getTipoAtto() != null && getTipoAtto().getUid() != 0) {
			utility.setTipoAtto(getTipoAtto());
		}
		
		// Injetto la struttura amministrativa contabile se e solo se e' stata inizializzata
		if(getStrutturaAmministrativoContabileAttoAmministrativo() != null && getStrutturaAmministrativoContabileAttoAmministrativo().getUid() != 0) {
			utility.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabileAttoAmministrativo());
		}
		
		return utility;
		 */
	}
	
	/**
	 * Test
	 */
	@Test
	public void getTipiProvvedimento() {
		TipiProvvedimento request = new TipiProvvedimento();
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest());
		
		TipiProvvedimentoResponse response = service.getTipiProvvedimento(request);
		logResponse(response);
	}

}
