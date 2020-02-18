# Configurations
All configuration MUST be set in the `buildfiles/<env>.properties` file used for compilation
- current.env = the currently executing environment
- nome.ambiente = the name of the environment
- datasource.jndi-url = no more used. May be left to blank or to a default value
- messageSources.cacheSeconds = no more used. May be left to -1
- remincl.resource.provider = URL to the remote resources
- remincl.cache.time = caching time for the remote resources (default: 8 hours)
- portal.home = portal home for the logout
- sso.filter.name = Name for the SSO filter
- sso.filter.url.pattern = URL pattern for the SSO filter
    (specify a non-existing extension to prevent SSO checks)
- sso.loginHandler = fully-qualified class name for the SSO handler
- urlServerParacadute = URL for SSOBart implementation (unused)
- edu.yale.its.tp.cas.client.filter.serverName = SSOBart server (unused)
- edu.yale.its.tp.cas.client.filter.validateUrl = SSOBart validation url (unused)
- ssobart.java.naming.provider.url = SSOBart naming provider (unused)
- ssobart.multi = SSOBart parameter (unused)
- endpoint.url.service.core = Endpoint for the COR backend service
- endpoint.url.service.att = Endpoint for the ATT backend service
- endpoint.url.service.bil = Endpoint for the BIL backend service
- endpoint.url.service.cec = Endpoint for the CEC backend service
- endpoint.url.service.fin2 = Endpoint for the FIN2 backend service
- endpoint.url.service.fin = Endpoint for the FIN backend service
- endpoint.url.service.gen = Endpoint for the GEN backend service
- endpoint.url.service.fel = Endpoint for the FEL backend service
- endpoint.url.service.integ = Endpoint for the INTEG backend service
- remappedException = Exception which will be remapped to a default error page
    if thrown (specify a non-throwable exception to prevent
    the use of the default fallback)
- jquery.version = specify the jQuery version:
  - jquery = jQuery minified (production-ready)
  - jquery.nomin = jQuery non-minified (for development)
- datatables.version = specify the DataTables version
  - .min = DataTables minified (production-ready)
  - `` (empty) = DataTables non-minified (for development)
- defaultCacheRollingPolicy = the default value for the front-end-specific
    cache rolling policy. May be:
  - ALWAYS = Consider the item as always stale
  - NEVER = Consider the item as never stale
  - HIT_10 = Consider the item as stale if it was hit 10 times
  - HIT_100 = Consider the item as stale if it was hit 100 times
  - HIT_1000 = Consider the item as stale if it was hit 1000 times
  - DAILY = Consider the item as stale if a day has past since the caching
  - WEEKLY = Consider the item as stale if a week has past since the caching
  - MONTHLY = Consider the item as stale if a month has past since the caching
  - YEARLY = Consider the item as stale if a year has past since the caching
- jspath = the path for the local JavaScript files (for proxying support)
- jspathexternal = the path for the external JavaScript files (for proxying support)
- service.proxy.advices = comma-separed list of fully-qualified class names
    for the advices to add to the service proxies
- service.proxy.soapLog = whether to log the SOAP requests and responses
    at the proxy level
- javax.xml.ws.client.connectionTimeout = connection timeout for the JAX-WS runtime
- javax.xml.ws.client.receiveTimeout = receive timeout for the JAX-WS runtime
