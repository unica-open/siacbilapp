/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import it.csi.siac.siacbilapp.BaseJUnit4TestCase;
import it.csi.siac.siacbilapp.frontend.serviceclient.serviceexecutor.KeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.serviceexecutor.ParallelServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.serviceexecutor.ServiceInvoker;
import it.csi.siac.siaccommonapp.util.proxy.SiacJaxWsPortAdvisedProxyFactoryBean;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * Classe di test per il {@link ParallelServiceExecutor}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 25/09/2015
 *
 */
public class ParallelServiceExecutorTest extends BaseJUnit4TestCase {
	
	private int[] uids = new int[] {1, 2, 4, 7, 9, 12, 15, 16, 18, 19, 20};
	
	/**
	 * Test senza executor.
	 * @throws Exception in caso di errore nel test
	 */
	@Test
	public void noExecutor() throws Exception {
		DocumentoSpesaService bs = createService();
		
		RicercaDettaglioDocumentoSpesa req = initRequest();
		DocumentoSpesa ds = new DocumentoSpesa();
		
		long initTime = System.currentTimeMillis();
		
		for(int uid  : uids) {
			ds.setUid(uid);
			req.setDocumentoSpesa(ds);
			
			// Cerco il bilancio
			RicercaDettaglioDocumentoSpesaResponse res = bs.ricercaDettaglioDocumentoSpesa(req);
			assertNotNull(res);
		}
		long endTime = System.currentTimeMillis();
		log.info("testNoExecutor", "Total Elapsed: " + (endTime - initTime) + " ms");
	}
	
	/**
	 * Test con executor.
	 * @throws Exception in caso di errore nel test
	 */
	@Test
	public void executor() throws Exception {
		final String methodName = "executor";
		ParallelServiceExecutor pse = new ParallelServiceExecutor();
		pse.postConstruct();
		
		DocumentoSpesaService bs = createService();
		
		RicercaDettaglioDocumentoSpesa req = initRequest();
		DocumentoSpesa ds = new DocumentoSpesa();
		
		long initTime = System.currentTimeMillis();
		
		for(final int uid  : uids) {
			ds.setUid(uid);
			req.setDocumentoSpesa(ds);
			
			pse.submitTask(new ServiceInvoker<RicercaDettaglioDocumentoSpesa, RicercaDettaglioDocumentoSpesaResponse, DocumentoSpesaService>() {
				@Override
				public RicercaDettaglioDocumentoSpesaResponse invoke(RicercaDettaglioDocumentoSpesa request, DocumentoSpesaService service) {
					return service.ricercaDettaglioDocumentoSpesa(request);
				}
			}, req, bs, new KeyAdapter<RicercaDettaglioDocumentoSpesa>() {
				@Override
				public String computeKey(RicercaDettaglioDocumentoSpesa request) {
					return request.getClass().getSimpleName() + "_" + request.getDocumentoSpesa().getUid();
				}
			});
		}
		
		Map<String, ServiceResponse> tasks = pse.getTasks();
		
		long endTime = System.currentTimeMillis();
		assertNotNull(tasks);
		log.info("testExecutor", "Total Elapsed: " + (endTime - initTime) + " ms");
		
		for(Entry<String, ServiceResponse> entry : tasks.entrySet()) {
			log.info(methodName, entry.getKey() + " ==> " + entry.getValue().getClass().getSimpleName());
		}
	}
	
	/**
	 * Inizializzazione della request
	 * @return
	 */
	private RicercaDettaglioDocumentoSpesa initRequest() {
		RicercaDettaglioDocumentoSpesa req = new RicercaDettaglioDocumentoSpesa();
		req.setDataOra(new Date());
		
		Richiedente richiedente = new Richiedente();
		Operatore operatore = new Operatore();
		operatore.setCodiceFiscale("RMNLSS_FE");
		richiedente.setOperatore(operatore);
		Account account = new Account();
		account.setUid(1);
		Ente ente = new Ente();
		ente.setUid(1);
		account.setEnte(ente);
		richiedente.setAccount(account);
		req.setRichiedente(richiedente);
		
		return req;
	}

	/**
	 * Creazione del servizio
	 * @return il servizio
	 * @throws MalformedURLException nel caso in cui l'URL non sia ben formato 
	 */
	private DocumentoSpesaService createService() throws MalformedURLException {
		SiacJaxWsPortAdvisedProxyFactoryBean fb = new SiacJaxWsPortAdvisedProxyFactoryBean();
		fb.setServiceInterface(DocumentoSpesaService.class);
		fb.setWsdlDocumentUrl(new URL("http://dev-www.ruparpiemonte.it/siacbilser/DocumentoSpesaService?wsdl"));
		fb.setNamespaceUri(FIN2SvcDictionary.NAMESPACE);
		fb.setServiceName("DocumentoSpesaService");
		fb.setEndpointAddress("http://dev-www.ruparpiemonte.it/siacbilser/DocumentoSpesaService");
		fb.setAdvices("it.csi.siac.siacbilapp.frontend.ui.util.advice.TimingAdvice");
		
		fb.afterPropertiesSet();
		return (DocumentoSpesaService) fb.getObject();
	}
	
	
}
