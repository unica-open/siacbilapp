/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.ProgettoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgettoResponse;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siacbilser.model.TipoAmbito;

/**
 * Classe di test per i serviz&icirc; deployati del Progetto.
 * 
 * @author Marchino Alessandro
 *
 */
public class ProgettoServiceTest extends BaseProxyServiceTest<ProgettoService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/ProgettoService";
		// return "http://tst-www.ruparpiemonte.it/siacbilser/ProgettoService";
	}
	
	/**
	 * Test.
	 */
	@Test
	public void ricerca() {
		final String methodName = "ricerca";
		RicercaDettaglioProgetto req = new RicercaDettaglioProgetto();
		
		req.setRichiedente(getRichiedenteTest());
		req.setChiaveProgetto(Integer.valueOf(1));
				
		RicercaDettaglioProgettoResponse res = service.ricercaDettaglioProgetto(req);
		
		assertNotNull(res);
		assertNotNull(res.getProgetto());
		
		log.info(methodName, "#Cronoprogrammi: " + res.getProgetto().getCronoprogrammi().size());
		for(Cronoprogramma c : res.getProgetto().getCronoprogrammi()) {
			log.info(methodName, "Cronoprogramma " + c.getUid() + " - #Entrata: " + c.getCapitoliEntrata().size()
				+ " | #Uscita: " + c.getCapitoliUscita().size());
		}
	}
	
	/**
	 * Test
	 */
	@Test
	public void aggiornamento() {
		final String methodName = "aggiornamento";
		AggiornaAnagraficaProgetto req = new AggiornaAnagraficaProgetto();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest());
		
		Progetto progetto = new Progetto();
		progetto.setUid(28);
		progetto.setEnte(getEnteTest());
		progetto.setCodice("A101M4");
		progetto.setDescrizione("Progetto di test ahmad");
		progetto.setRilevanteFPV(Boolean.TRUE);
		progetto.setNote("Note per il progetto di test");
		progetto.setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		progetto.setAttoAmministrativo(getAttoAmministrativoTest());
		progetto.setTipoAmbito(getTipoAmbitoTest());
		progetto.setValoreComplessivo(new BigDecimal("350000"));
		
		req.setProgetto(progetto);
		
		AggiornaAnagraficaProgettoResponse res = service.aggiornaAnagraficaProgetto(req);
		
		assertNotNull(res);
		assertNotNull(res.getProgetto());
		
		log.info(methodName, "uid progetto: " + res.getProgetto().getUid());
	}
	
	/**
	 * Ottiene un Atto Amministrativo di test.
	 * 
	 * @return l'atto
	 */
	private AttoAmministrativo getAttoAmministrativoTest() {
		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
		attoAmministrativo.setUid(34); // Cfr. siac_t_atto_amm, DB
		return attoAmministrativo;
	}
	
	/**
	 * Ottiene un Tipo Ambito di test.
	 * 
	 * @return il tipo
	 */
	private TipoAmbito getTipoAmbitoTest() {
		TipoAmbito tipoAmbito = new TipoAmbito();
		tipoAmbito.setUid(41138); // Cfr. siac_t_class, DB
		return tipoAmbito;
	}
	
}
