/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfin2ser.model.StatoOperativoModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di test per i servizi relativi al soggetto Fin richiamati dal modulo Bil.
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 - 09/01/2014
 *
 */
public class SoggettoServiceTest extends BaseProxyServiceTest<SoggettoService> {
	
	@Override
	protected String getEndpoint() {
//		return "http://dev-www.ruparpiemonte.it/siacbilser/SoggettoService";
//		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/SoggettoService";
//	return "http://coll-srv1.bilancio.csi.it/siacbilser/SoggettoService";
		return "http://tst-srv-consip.bilancio.csi.it/siacbilser/SoggettoService";
	}
	
	/**
	 * Test liste soggetti.
	 */
	@Test
	public void soggetti() {
		ListeGestioneSoggetto req = new ListeGestioneSoggetto();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest(getEnteTest()));
		
		ListeGestioneSoggettoResponse res = service.listeGestioneSoggetto(req);
		assertNotNull(res);
		log.logXmlTypeObject(res, "Response");
	}
	
	/**
	 * Test Ricerca Soggetti
	 */
	@Test
	public void ricercaSoggetti() {
		String methodName = "testRicercaSoggetti";
		String denominazione =null;// "Demo";
		String codice = null; //"Demo 23";
		String codiceFiscale = null;//"AAAAAA00A11D000L";
		String partitaIva =null;
		String classificatore = null;
		String matricola = "3186";
		
		RicercaSoggetti request = new RicercaSoggetti();
		request.setRichiedente(getRichiedenteTest(getEnteTest()));
		//request.setRichiedente(getRichiedenteTest());
		//request.setEnte(getEnteTest());
		request.setDataOra(new Date());
		
		request.setParametroRicercaSoggetto(impostaRicerca(codice, matricola, codiceFiscale, partitaIva, denominazione, classificatore));
		request.setSorgenteDatiSoggetto(SorgenteDatiSoggetto.HR);
		
		log.logXmlTypeObject(request, "Request");
		
		RicercaSoggettiResponse response = service.ricercaSoggetti(request);

		log.logXmlTypeObject(response, "Response");
		
		//3186
		
		try {
			Assert.assertTrue(!response.isFallimento());
			if(response.getSoggetti() != null) {
				for(Soggetto s : response.getSoggetti()){
					log.debug(methodName, s);
				}
			}
		} catch(Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaPerClasse() {
		final String methodName = "testRicercaPerClasse";
		String[] classi = {"ASS", "PR", "RTI", "TAS"};
		for(String s : classi) {
			log.info(methodName, "Classe: " + s);
			RicercaSoggetti req = new RicercaSoggetti();
			req.setEnte(getEnteTest());
			req.setDataOra(new Date());
			req.setNumPagina(0);
			req.setNumRisultatiPerPagina(100);
			req.setRichiedente(getRichiedenteTest(getEnteTest()));
			req.setParametroRicercaSoggetto(creaParametroRicercaSoggetto(s));
			
			RicercaSoggettiResponse response = service.ricercaSoggetti(req);
			
			log.logXmlTypeObject(response, "Response " + s);
		}
		
	}
	
	/**
	 * Test Ricerca Soggetto per chiave
	 */
	@Test
	public void ricercaSoggettoPerChiave() {
		final String methodName = "ricercaSoggettoPerChiave";
		RicercaSoggettoPerChiave request = new RicercaSoggettoPerChiave();
//		request.setRichiedente(getRichiedenteTest());
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		request.setDataOra(new Date());
		
		ParametroRicercaSoggettoK parametroRicercaSoggetto = new ParametroRicercaSoggettoK();
		request.setParametroSoggettoK(parametroRicercaSoggetto);
		
		String[] codici = new String[] {"329516"};
		List<List<String>> data = new ArrayList<List<String>>();
		
		for(String codice : codici) {
			parametroRicercaSoggetto.setCodice(codice);
			RicercaSoggettoPerChiaveResponse response = service.ricercaSoggettoPerChiave(request);
			assertNotNull(response);
			
			List<String> inner = new ArrayList<String>();
			
			for(ModalitaPagamentoSoggetto mps : response.getListaModalitaPagamentoSoggetto()) {
				inner.add("SOGGETTO [" + codice + "] - MPS [" + mps.getUid() + "] - associato a: " + mps.getAssociatoA());
			}
			data.add(inner);
			
			List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = response.getListaModalitaPagamentoSoggetto();
			
			List<ModalitaPagamentoSoggetto> risultato = new ArrayList<ModalitaPagamentoSoggetto>();
			Date now = new Date();
			
			for(ModalitaPagamentoSoggetto mps : listaModalitaPagamentoSoggetto) {
				boolean statoValido = StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equalsIgnoreCase(mps.getCodiceStatoModalitaPagamento())
				|| (mps.getModalitaPagamentoSoggettoCessione2() != null
					&& StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equalsIgnoreCase(mps.getModalitaPagamentoSoggettoCessione2().getCodiceStatoModalitaPagamento()));
				if(statoValido && (mps.getDataScadenza() == null || !mps.getDataScadenza().before(now))) {
					risultato.add(mps);
				}
			}
			log.debug(methodName, "size modalita non filtrate: " + listaModalitaPagamentoSoggetto.size() + "size modalita filtrate: " + risultato.size());
//			for (ModalitaPagamentoSoggetto modalitaPagamentoSoggetto : risultato) {
//				log.debug(methodName, "$$$$$$$$$$$$$$$$$$$$$$$$$");
//				log.logXmlTypeObject(modalitaPagamentoSoggetto, "modalitaPagamentoSoggetto");
//			}
		}
		
		for(List<String> datum : data) {
			for(String row : datum) {
				log.debug(methodName, row);
			}
			log.debug(methodName, "");
		}
		
		
	}
	
	/**
	 * Test Ricerca Soggetto per chiave
	 */
	@Test
	public void testRicercaSoggettoPerChiaveFORN2() {
		String codice = "14588";
		
		RicercaSoggettoPerChiave request = new RicercaSoggettoPerChiave();
		request.setRichiedente(getRichiedenteTest("AAAAAA00A11C000K", 52, 2));
		request.setEnte(request.getRichiedente().getAccount().getEnte());
		
		request.setDataOra(new Date());
		
		ParametroRicercaSoggettoK parametroRicercaSoggetto = new ParametroRicercaSoggettoK();
		parametroRicercaSoggetto.setCodice(codice);
		request.setParametroSoggettoK(parametroRicercaSoggetto);
		
		RicercaSoggettoPerChiaveResponse response = service.ricercaSoggettoPerChiave(request);

		log.logXmlTypeObject(response, "Response");
	}
	
	/**
	 * Test testRicercaSedeSecondaria 
	 */
	@Test
	public void ricercaSedeSecondaria() {
		RicercaSedeSecondariaPerChiave request = new RicercaSedeSecondariaPerChiave();
		
		Soggetto s = creaSoggettoConChiave("1");
		
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest(getEnteTest()));
		request.setSoggetto(s);
		request.setSedeSecondariaSoggetto(getSedeSecondariaSoggetto(1));
		
		log.logXmlTypeObject(request, "Request");
		
		RicercaSedeSecondariaPerChiaveResponse response = service.ricercaSedeSecondariaPerChiave(request);
		log.logXmlTypeObject(response, "Response");
		assertFalse(response.hasErrori());
	}
	

	private SedeSecondariaSoggetto getSedeSecondariaSoggetto(int uid) {
		SedeSecondariaSoggetto sss = new SedeSecondariaSoggetto();
		sss.setUid(uid);
		return sss;
	}

	/**
	 * Test testRicercaModalitaPagamentoPerChiave 
	 */
	@Test
	public void ricercaModalitaPagamentoPerChiave() {
		RicercaModalitaPagamentoPerChiave request = new RicercaModalitaPagamentoPerChiave();
		
		request.setDataOra(new Date());
		request.setEnte(getEnteTest(15));
		request.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 71, 15));
		
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto("16");
		soggetto.setUid(62);
		request.setSoggetto(soggetto);
		
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = new ModalitaPagamentoSoggetto();
		modalitaPagamentoSoggetto.setCodiceSoggettoAssociato(Integer.valueOf(16));
		modalitaPagamentoSoggetto.setCodiceModalitaPagamento("3");
		request.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		
		logRequest(request);
		
		RicercaModalitaPagamentoPerChiaveResponse response = service.ricercaModalitaPagamentoPerChiave(request);
		logResponse(response);
	}
	
	/**
	 * Test
	 */
	@Test
	public void listeGestioneSoggetto() {
		ListeGestioneSoggetto request = new ListeGestioneSoggetto();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteTest(getEnteTest()));
		
		log.logXmlTypeObject(request, "Request");
		
		ListeGestioneSoggettoResponse response = service.listeGestioneSoggetto(request);
		log.logXmlTypeObject(response, "Response");
	}
		
	
	
	
	/**
	 * Imposta la ricerca sulla base dei dati passati
	 * @param codiceSoggetto il codice del soggetto
	 * @param matricola      la matricola del soggetto
	 * @param codiceFiscale  il codice fiscale
	 * @param partitaIva     la partita iva
	 * @param denominazione  la denominazione
	 * @param classificatore il classificatore
	 * 
	 * @return il parametro 
	 */
	private ParametroRicercaSoggetto impostaRicerca(String codiceSoggetto, String matricola, String codiceFiscale, 
													String partitaIva, String denominazione, String classificatore) {
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		parametroRicercaSoggetto.setCodiceSoggetto(codiceSoggetto);
		parametroRicercaSoggetto.setCodiceFiscale(codiceFiscale);
		parametroRicercaSoggetto.setDenominazione(denominazione);
		parametroRicercaSoggetto.setPartitaIva(partitaIva);
		parametroRicercaSoggetto.setMatricola(matricola);
		
		
		return parametroRicercaSoggetto;
	}
	
	/**
	 * @param classe la classe del soggetto
	 * @return il parametro di ricerca
	 */
	private ParametroRicercaSoggetto creaParametroRicercaSoggetto(String classe) {
		ParametroRicercaSoggetto res = new ParametroRicercaSoggetto();
		res.setClasse(classe);
		return res;
	}
	
	/**
	 * Crea un soggetto con data chiave univoca.
	 * 
	 * @param chiaveSoggetto la chiave del soggetto
	 * 
	 * @return il soggetto creato
	 */
	private Soggetto creaSoggettoConChiave(String chiaveSoggetto) {
		Soggetto s = new Soggetto();
		s.setCodiceSoggetto(chiaveSoggetto);
		return s;
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaSedeSecondariaPerChiave() {
		RicercaSedeSecondariaPerChiave request = new RicercaSedeSecondariaPerChiave();
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest("AAAAAA00A11C000K", 52, 2));
		
		SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
		sedeSecondariaSoggetto.setCodiceSedeSecondaria("myCodieceSede");
		request.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
		
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto("1800000");
		request.setSoggetto(soggetto);
		
		RicercaSedeSecondariaPerChiaveResponse response = service.ricercaSedeSecondariaPerChiave(request);
		logResponse(response);
	}
	
}
