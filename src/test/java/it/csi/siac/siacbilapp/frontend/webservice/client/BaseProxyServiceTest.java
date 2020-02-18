/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.handler.Handler;

import org.aopalliance.aop.Advice;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.springframework.core.GenericTypeResolver;

import it.csi.siac.siacbilapp.BaseJUnit4TestCase;
import it.csi.siac.siacbilapp.frontend.ui.util.advice.TimingAdvice;
import it.csi.siac.siacbilapp.frontend.ui.util.proxy.LoggingHandler;
import it.csi.siac.siacbilapp.frontend.ui.util.proxy.SiacHandlerResolver;
import it.csi.siac.siacbilapp.frontend.ui.util.proxy.SiacJaxWsPortAdvisedProxyFactoryBean;

/**
 * Classe base di test per i serviz&icirc;.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/03/2014
 * @param <S> la tipizzazione del servizio
 *
 */
public abstract class BaseProxyServiceTest <S> extends BaseJUnit4TestCase {
	
	/** Il servizio */
	protected S service;
	private Class<S> serviceInterface;
	
	/**
	 * Setup del client per il WS del COR.
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		createServiceProxy();
	}
	
	
	/**
	 * Creazione del proxy del servizio
	 * 
	 * @throws MalformedURLException in caso di URL non correttamente formattato
	 */
	@SuppressWarnings("unchecked")
	protected void createServiceProxy() throws MalformedURLException {
		this.serviceInterface = resolveServiceInterface();
		String endpoint = getEndpoint();
		String namespaceUri = getNamespaceUri();
		String getServiceName = getServiceName();
		
		SiacJaxWsPortAdvisedProxyFactoryBean proxyFactoryBean = initProxyFactory();
		proxyFactoryBean.setServiceInterface(serviceInterface);
		proxyFactoryBean.setWsdlDocumentUrl(new URL(endpoint + "?wsdl"));
		proxyFactoryBean.setNamespaceUri(namespaceUri);
		proxyFactoryBean.setServiceName(getServiceName);
		proxyFactoryBean.setEndpointAddress(endpoint);
		initAdvices(proxyFactoryBean);
		addLogHandler(proxyFactoryBean);
		
		// Inizializzazione
		proxyFactoryBean.afterPropertiesSet();
		this.service = (S) proxyFactoryBean.getObject();
		
		if(this.service == null) {
			throw new IllegalStateException("Il servizio non e' stato creato correttamente");
		}
	}
	
	/**
	 * Aggiunge l'handler di logging per le richieste in- e out-bound.
	 * @param proxyFactoryBean la proxy factory bean
	 */
	protected void addLogHandler(SiacJaxWsPortAdvisedProxyFactoryBean proxyFactoryBean) {
		SiacHandlerResolver hr = new SiacHandlerResolver();
		@SuppressWarnings("rawtypes")
		List<Handler> handlers = new ArrayList<Handler>();
		handlers.add(new LoggingHandler());
		hr.setHandlerList(handlers);
		proxyFactoryBean.setHandlerResolver(hr);
	}

	/**
	 * Inizializzazione degli advice
	 * @param proxyFactoryBean la factory
	 */
	private void initAdvices(SiacJaxWsPortAdvisedProxyFactoryBean proxyFactoryBean) {
		Iterable<Class<? extends Advice>> advices = getAdvices();
		Collection<String> adviceStrings = new ArrayList<String>();
		for(Class<? extends Advice> clazz : advices) {
			adviceStrings.add(clazz.getName());
		}
		proxyFactoryBean.setAdvices(StringUtils.join(adviceStrings, ","));
	}
	
	/**
	 * Gli advice da utilizzare
	 * @return gli advice
	 */
	protected Collection<Class<? extends Advice>> getAdvices() {
		Collection<Class<? extends Advice>> res = new ArrayList<Class<? extends Advice>>();
		//res.add(JAXBLoggingAdvice.class);
		res.add(TimingAdvice.class);
		return res;
	}

	/**
	 * Inizializzazione della factory.
	 * 
	 * @return la factory creata
	 */
	protected SiacJaxWsPortAdvisedProxyFactoryBean initProxyFactory() {
		return new SiacJaxWsPortAdvisedProxyFactoryBean();
	}
	
	/**
	 * Risolve l'interfaccia del servizio
	 * 
	 * @return la classe rappresentante l'interfaccia
	 */
	@SuppressWarnings("unchecked")
	protected Class<S> resolveServiceInterface() {
		Class<?>[] genericTypeArguments = GenericTypeResolver.resolveTypeArguments(this.getClass(), BaseProxyServiceTest.class);
		return (Class<S>) genericTypeArguments[0];
	}
	
	/**
	 * Ottiene il nome del servizio.
	 * 
	 * @return il nome del servizio
	 */
	protected String getServiceName() {
		return this.serviceInterface.getSimpleName();
	}
	
	/**
	 * Ottiene l'URI del namespace del servizio.
	 * 
	 * @return l'URI del servizio
	 */
	protected String getNamespaceUri() {
		WebService webService = this.serviceInterface.getAnnotation(WebService.class);
		if(webService == null) {
			throw new IllegalStateException("No annotation fonund for namespace URI");
		}
		return webService.targetNamespace();
	}
	
	/**
	 * Ottiene l'endpoint del servizio.
	 * 
	 * @return l'endpoint del servizio
	 */
	protected abstract String getEndpoint();
	
}
