/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.fin;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

/**
 * Classe di test per i servizi relativi al movimento di gestione
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 26/11/2014
 *
 */
public class MovimentoGestioneServiceTest extends BaseProxyServiceTest<MovimentoGestioneService> {
	
	@Override
	protected String getEndpoint() {
		//return "http://coll-srv-contabilia.bilancio.csi.it/siacbilser/MovimentoGestioneService";
		return "http://10.136.6.151/siacbilser/MovimentoGestioneService";
	}
	/**
	 * Test per RicercaImpegnoPerChiave
	 */
	@Test
	public void ricercaImpegnoPerChiave() {
		final String methodName = "ricercaImpegnoPerChiave";
		RicercaImpegnoPerChiave req = new RicercaImpegnoPerChiave();
		
		//req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 410, 2));
		req.setRichiedente(getRichiedenteTest());
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		RicercaImpegnoK pRicercaImpegnoK = new RicercaImpegnoK();
		req.setpRicercaImpegnoK(pRicercaImpegnoK);
		
		pRicercaImpegnoK.setAnnoEsercizio(2015);
		pRicercaImpegnoK.setAnnoImpegno(2015);
		//pRicercaImpegnoK.setNumeroImpegno(new BigDecimal("2300"));
		pRicercaImpegnoK.setNumeroImpegno(new BigDecimal("635"));
		
		RicercaImpegnoPerChiaveResponse response = service.ricercaImpegnoPerChiave(req);
		log.info(methodName, "Impegno.getCodSiope()==null?" + (response.getImpegno()!=null && response.getImpegno().getCodSiope() != null? response.getImpegno().getCodSiope() : "false"));
	}
	
	/**
	 * Ricerca sintetica impegni/subimpegni
	 */
	@Test
	public void ricercaSinteticaImpegniSubImpegni() {
		RicercaSinteticaImpegniSubImpegni request = new RicercaSinteticaImpegniSubImpegni();
		ParametroRicercaImpSub pr = new ParametroRicercaImpSub();
		pr.setAnnoEsercizio(Integer.valueOf(2017));
		
		/* ***PROVVEDIMENTO*** */
		pr.setAnnoProvvedimento(Integer.valueOf(2017));
		pr.setNumeroProvvedimento(Integer.valueOf(11));
		//pr.setTipoProvvedimento(impostaEntitaFacoltativa(attoAmministrativo.getTipoAtto()));
		
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		request.setEnte(request.getRichiedente().getAccount().getEnte());
		request.setParametroRicercaImpSub(pr);
		request.setNumPagina(1);
		
		RicercaSinteticaImpegniSubimpegniResponse response = service.ricercaSinteticaImpegniSubimpegni(request);
		assertNotNull(response);
	}
	
	/**
	 * Ricerca impegni e subimpegni
	 */
	@Test
	public void ricercaImpegniSubImpegni() {
		RicercaImpegniSubImpegni req = new RicercaImpegniSubImpegni();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setNumPagina(1);
		req.setNumRisultatiPerPagina(10);
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		ParametroRicercaImpSub parametroRicercaImpSub = new ParametroRicercaImpSub();
		parametroRicercaImpSub.setAnnoEsercizio(2017);
		parametroRicercaImpSub.setAnnoImpegno(2017);
		parametroRicercaImpSub.setIsRicercaDaImpegno(Boolean.TRUE);
		parametroRicercaImpSub.setNumeroCapitolo(101310);
		parametroRicercaImpSub.setNumeroArticolo(0);
		parametroRicercaImpSub.setNumeroUEB(1);
		req.setParametroRicercaImpSub(parametroRicercaImpSub);
		
		RicercaImpegniSubimpegniResponse res = service.ricercaImpegniSubimpegni(req);
		assertNotNull(res);
	}
	
	/**
	 * Test per RicercaImpegnoPerChiaveOttimizzato
	 */
	@Test
	public void ricercaImpegnoPerChiaveOttimizzato() {
		RicercaImpegnoPerChiaveOttimizzato req = new RicercaImpegnoPerChiaveOttimizzato();
		
		req.setRichiedente(getRichiedenteByProperties("forn1", "cmto"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setDataOra(new Date());
		
		req.setpRicercaImpegnoK(new RicercaImpegnoK());
		req.getpRicercaImpegnoK().setAnnoEsercizio(2018);
		req.getpRicercaImpegnoK().setAnnoImpegno(2018);
		req.getpRicercaImpegnoK().setNumeroImpegno(new BigDecimal("1629528"));
		
		req.setCaricaSub(false);
		req.setDatiOpzionaliElencoSubTuttiConSoloGliIds(new DatiOpzionaliElencoSubTuttiConSoloGliIds());
		req.getDatiOpzionaliElencoSubTuttiConSoloGliIds().setCaricaElencoModificheMovGest(true);
		req.getDatiOpzionaliElencoSubTuttiConSoloGliIds().setCaricaMutui(true);
		req.getDatiOpzionaliElencoSubTuttiConSoloGliIds().setCaricaVociMutuo(true);
		req.getDatiOpzionaliElencoSubTuttiConSoloGliIds().setCaricaCig(true);
		req.getDatiOpzionaliElencoSubTuttiConSoloGliIds().setCaricaCup(true);
		req.getDatiOpzionaliElencoSubTuttiConSoloGliIds().setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		req.getDatiOpzionaliElencoSubTuttiConSoloGliIds().setCaricaDisponibilePagare(true);
		req.getDatiOpzionaliElencoSubTuttiConSoloGliIds().setCaricaDisponibileFinanziare(true);
		
		RicercaImpegnoPerChiaveOttimizzatoResponse response =  service.ricercaImpegnoPerChiaveOttimizzato(req);
		assertNotNull(response);
	}
	
	/**
	 * Test per RicercaImpegnoPerChiaveOttimizzato
	 */
	@Test
	public void ricercaAccertamentoPerChiaveOttimizzato() {
		RicercaAccertamentoPerChiaveOttimizzato req = new RicercaAccertamentoPerChiaveOttimizzato();
		
		//req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 410, 2));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setDataOra(new Date());
		req.setEscludiSubAnnullati(true);
		req.setSubPaginati(true);
		req.setCaricaSub(true);
		
		
		req.setNumPagina(1);
		req.setNumRisultatiPerPagina(5);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
//		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCig(true);
//		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCup(true);
		//
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaMutui(true);
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaVociMutuo(true);
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		//tutto il resto e' false se non lo setto
		req.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
	
		
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		
		req.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		
		
		pRicercaAccertamentoK.setAnnoEsercizio(2017);
		// ************* IMPEGNO CON 13 SUB  DI CUI UNO ANNULLATO(N.2) E UN SUB CON MUTUO (N.14)
		pRicercaAccertamentoK.setAnnoAccertamento(2017);
		pRicercaAccertamentoK.setNumeroAccertamento(new BigDecimal("888"));
//		pRicercaAccertamentoK.setNumeroSubDaCercare(new BigDecimal("1"));
		
		pRicercaAccertamentoK.setNumeroSubDaCercare(new BigDecimal("1"));
		//pRicercaAccertamentoK.setNumeroSubDaCercare(BigDecimal.ZERO);
		
		// ************** IMPEGNO CON VOCE MUTUO
		//pRicercaAccertamentoK.setAnnoImpegno(2014);
		//pRicercaAccertamentoK.setNumeroImpegno(new BigDecimal("2227"));
		
		// ************* IMPEGNO CON CIG E CUP
		//pRicercaAccertamentoK.setAnnoImpegno(2015);
		//pRicercaAccertamentoK.setNumeroImpegno(new BigDecimal("166"));
		
		//pRicercaAccertamentoK.setCaricaDatiUlteriori(Boolean.FALSE);
		//pRicercaAccertamentoK.setCaricaSediEModalitaPagamento(Boolean.FALSE);
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse res = service.ricercaAccertamentoPerChiaveOttimizzato(req);
		log.logXmlTypeObject(res.getAccertamento().getCapitoloEntrataGestione(), "XXXX -> CAPITOLO");
		assertNotNull(res);
	}
	/**
	 * Test
	 * **/
	@Test
	public void ricercaAccertamentoPerChiaveOttimizzatoFORN2() {
		RicercaAccertamentoPerChiaveOttimizzato req = new RicercaAccertamentoPerChiaveOttimizzato();
		
		req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M",748,4));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		req.setEscludiSubAnnullati(true);
		req.setSubPaginati(true);
		req.setCaricaSub(true);
		
		
		req.setNumPagina(1);
		req.setNumRisultatiPerPagina(5);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		//tutto il resto e' false se non lo setto
		req.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
	
		
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		
		req.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		
		
		pRicercaAccertamentoK.setAnnoEsercizio(2017);
		// ************* IMPEGNO CON 13 SUB  DI CUI UNO ANNULLATO(N.2) E UN SUB CON MUTUO (N.14)
		pRicercaAccertamentoK.setAnnoAccertamento(2017);
		pRicercaAccertamentoK.setNumeroAccertamento(new BigDecimal("14"));
		//pRicercaAccertamentoK.setNumeroSubDaCercare(new BigDecimal("1"));
		
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse res = service.ricercaAccertamentoPerChiaveOttimizzato(req);
		assertNotNull(res);
	}
	
	/**
	 * Test per RicercaImpegnoPerChiaveOttimizzato
	 */
	@Test
	public void testRicercaAccertamentoPerChiaveOttimizzatoFORN2() {
		RicercaAccertamentoPerChiaveOttimizzato req = new RicercaAccertamentoPerChiaveOttimizzato();
		
		req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M",163,29));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		req.setEscludiSubAnnullati(true);
		req.setSubPaginati(true);
		req.setCaricaSub(false);
		
		
		req.setNumPagina(1);
		req.setNumRisultatiPerPagina(5);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		//tutto il resto e' false se non lo setto
		req.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
	
		
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		
		req.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		
		
		pRicercaAccertamentoK.setAnnoEsercizio(2017);
		// ************* IMPEGNO CON 13 SUB  DI CUI UNO ANNULLATO(N.2) E UN SUB CON MUTUO (N.14)
		pRicercaAccertamentoK.setAnnoAccertamento(2016);
		pRicercaAccertamentoK.setNumeroAccertamento(new BigDecimal(53));
		RicercaAccertamentoPerChiaveOttimizzatoResponse res = service.ricercaAccertamentoPerChiaveOttimizzato(req);
		assertNotNull(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaAccertamentoPerChiave() {
		
		RicercaAccertamentoPerChiave request = new RicercaAccertamentoPerChiave();
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
//		request.setEnte(getEnteTest(15));
		request.setRichiedente(getRichiedenteTest());
//		request.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 71, 15));
		RicercaAccertamentoK rak = new RicercaAccertamentoK();
		rak.setAnnoEsercizio(Integer.valueOf(2015));
		rak.setAnnoAccertamento(Integer.valueOf(2015));
		rak.setNumeroAccertamento(new BigDecimal("4"));
		request.setpRicercaAccertamentoK(rak);
		
		service.ricercaAccertamentoPerChiave(request);
	}

}
