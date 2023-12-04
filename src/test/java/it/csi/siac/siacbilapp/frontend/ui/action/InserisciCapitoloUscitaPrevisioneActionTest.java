/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilapp.BaseJUnit4SpringTestCase;
import it.csi.siac.siacbilapp.frontend.ui.action.capuscprev.InserisciCapitoloUscitaPrevisioneAction;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.InserisciCapitoloUscitaPrevisioneModel;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Classe di test per la Action CapitoloUscitaPrevisioneAction.
 * 
 * @author Alessandro Marchino
 *
 */
public class InserisciCapitoloUscitaPrevisioneActionTest extends BaseJUnit4SpringTestCase {
	
	private LogUtil logger = new LogUtil(getClass());
	
	private InserisciCapitoloUscitaPrevisioneAction action;
	private InserisciCapitoloUscitaPrevisioneModel  model;
	
	/**
	 * Inizializzazione di Spring ed autowiring delle dipendenze
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		action = applicationContext.getBean(InserisciCapitoloUscitaPrevisioneAction.class);
		try {
			action.prepare();
		} catch (Exception e) {
			logger.debug("init", "Errore nella preparazione della Action");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test per il metodo {@link InserisciCapitoloUscitaPrevisioneAction#inserisceCDU()}.
	 */
	@Test
	public void inserisciTest() {
		final String methodName = "inserisciTest";
		logger.debug(methodName, "Test per il metodo 'inserisci'");
		
		logger.debug(methodName, "Imposto i dati dalle liste del model per popolare i campi singoli");
		try {
			model.setMissione(			model.getListaMissione()			.get(0));
			model.setTipoFinanziamento(	model.getListaTipoFinanziamento()	.get(0));
			model.setTipoFondo(			model.getListaTipoFondo()			.get(0));
			model.setTitoloSpesa(		model.getListaTitoloSpesa()			.get(0));
			
			model.setClassificatoreGenerico1(model.getListaClassificatoreGenerico1()  .get(0));
			model.setClassificatoreGenerico2(model.getListaClassificatoreGenerico2()  .get(0));
			model.setClassificatoreGenerico3(model.getListaClassificatoreGenerico3()  .get(0));
			model.setClassificatoreGenerico4(model.getListaClassificatoreGenerico4()  .get(0));
			model.setClassificatoreGenerico5(model.getListaClassificatoreGenerico5()  .get(0));
			model.setClassificatoreGenerico6(model.getListaClassificatoreGenerico6()  .get(0));
			model.setClassificatoreGenerico7(model.getListaClassificatoreGenerico7()  .get(0));
			model.setClassificatoreGenerico8(model.getListaClassificatoreGenerico8()  .get(0));
			model.setClassificatoreGenerico9(model.getListaClassificatoreGenerico9()  .get(0));
			model.setClassificatoreGenerico10(model.getListaClassificatoreGenerico10().get(0));
		} catch(IndexOutOfBoundsException ex) {
			ex.printStackTrace();
			fail("Errore: " + ex.getMessage());
		}
		
		logger.debug(methodName, "Prima di action.inserisci()");
		String result = action.inserisceCDU();
		logger.debug(methodName, "Dopo di action.inserisci(): risultato " + result);
		
		assertEquals("Non ho ottenuto SUCCESS", result, "success");
	}
	
}