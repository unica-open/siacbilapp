/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.servicecache;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CacheStatistics;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.servicecache.ServiceCacheUtilityModel;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Utilities for interacting with the cache.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ServiceCacheUtilityAction extends GenericBilancioAction<ServiceCacheUtilityModel> {
	
	/** For serialization purposes */
	private static final long serialVersionUID = 5655781353659148483L;
	/** The password to use, BASE64-encoded */
	private static final String BASE64_PASSWORD = "Q0FJUw==";
	
	/** The executor instance */
	@Autowired private transient CachedServiceExecutor cachedServiceExecutor;
	
	@Override
	public void prepare() {
		// Initializes only the model, ignores every other action
		initModel();
	}
	
	/**
	 * Cleans the cache.
	 * @return a String representing the invocation status
	 */
	public String clear() {
		cachedServiceExecutor.clearCache();
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validation for method {@link #clear()}.
	 */
	public void validateClear() {
		checkCondition(StringUtils.isNotBlank(model.getPassword()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("password"), true);
		checkCondition(BASE64_PASSWORD.equals(model.getPassword()), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'utente non e' autorizzato ad eseguire l'operazione"));
	}

	/**
	 * Retrieves the informations regarding the cache.
	 * @return a String representing the invocation status
	 */
	public String info() {
		CacheStatistics cacheStatistics = Boolean.TRUE.equals(model.getAccurate()) ? cachedServiceExecutor.computeStatistics() : cachedServiceExecutor.computeProbableStatistics();
		model.computeStatistics(cacheStatistics);
		return SUCCESS;
	}
	
	/**
	 * Retrieves the keys of the cached data
	 * @return a String representing the invocation status
	 */
	public String keys() {
		Set<String> keys = Boolean.TRUE.equals(model.getAccurate()) ? cachedServiceExecutor.obtainCachedKeys() : cachedServiceExecutor.obtainProbableCachedKeys();
		model.setKeys(keys);
		return SUCCESS;
	}
	
	/**
	 * Clears the given key from the cache
	 * @return a String representing the invocation status
	 */
	public String clearKey() {
		boolean cleared = cachedServiceExecutor.clearKey(model.getKey());
		if(!cleared) {
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("la chiave specificata (\"" + model.getKey() + "\") non e' piu' presente in cache"));
			return INPUT;
		}
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validation for method {@link #clearKey()}.
	 */
	public void validateClearKey() {
		checkNotNullNorEmpty(model.getPassword(), "password", true);
		checkCondition(BASE64_PASSWORD.equals(model.getPassword()), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'utente non e' autorizzato ad eseguire l'operazione"));
		checkNotNullNorEmpty(model.getKey(), "key");
	}
	
	/**
	 * Result for the statistics
	 * @author Marchino Alessandro
	 */
	public static class ServiceCacheStatisticsResult extends CustomJSONResult {
		/** For serialization purpose */
		private static final long serialVersionUID = 8980259153810566195L;
		/** Properties to include */
		private static final String INCLUDE_PROPERTIES = "statistics";

		/** Empty default constructor */
		public ServiceCacheStatisticsResult() {
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
	/**
	 * Result for the keys
	 * @author Marchino Alessandro
	 */
	public static class ServiceCacheKeysResult extends CustomJSONResult {
		/** For serialization purpose */
		private static final long serialVersionUID = 8980259153810566195L;
		/** Properties to include */
		private static final String INCLUDE_PROPERTIES = "keys.*";

		/** Empty default constructor */
		public ServiceCacheKeysResult() {
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
}
