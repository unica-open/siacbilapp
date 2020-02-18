/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.serviceexecutor;

import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Invoker for a service. Used to wrap the service invocation for use in a generic callable.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/07/2015
 *
 * @param <REQ> the request type
 * @param <RES> the response type
 * @param <SER> the service type
 */
public interface ServiceInvoker<REQ extends ServiceRequest, RES extends ServiceResponse, SER> {
	
	/**
	 * Invokes the the given service via a given request.
	 * 
	 * @param request the request to invoke
	 * @param service the service to call
	 * 
	 * @return the response of the service invocation
	 */
	RES invoke(REQ request, SER service);
	
}
