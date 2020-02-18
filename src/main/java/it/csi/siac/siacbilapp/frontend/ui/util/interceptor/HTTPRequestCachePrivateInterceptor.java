/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
/**
 * This interceptor sets the the HTTP Header to work around IE SSL weirdness
 * 
 * @author Alessandro Marchino
 * @see HTTPRequestCachePrivateInterceptor by Eric Molitor <a href="mailto:eric@tuxbot.com">eric@tuxbot.com</a>
 */
public class HTTPRequestCachePrivateInterceptor extends AroundInterceptor {
 
    /** For serialization purpose */
	private static final long serialVersionUID = -8413034926167900226L;

	@Override
	protected void beforeIntercept(ActionInvocation actionInvocation) {
		// Checks whether the request was through a secure connection
		HttpServletRequest req = (HttpServletRequest) actionInvocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
		boolean fromSecure = req.isSecure();
		String userAgent = req.getHeader("user-agent");
		
		// see http://user-agent-string.info/list-of-ua/browser-detail?browser=IE
		boolean fromIE = userAgent != null && (userAgent.contains("Trident") || userAgent.contains("MSIE"));
		
		if(fromSecure  && fromIE) {
			// Evaluate whether to include
			HttpServletResponse res = (HttpServletResponse) actionInvocation.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
			res.setHeader("CACHE-CONTROL", "PRIVATE");
		}
	}

	@Override
	protected void afterInterceptor(ActionInvocation actionInvocation, String result) {
		/* Empty - does not do anything */
	}
}