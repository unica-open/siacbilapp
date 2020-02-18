/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.serviceexecutor;

import it.csi.siac.siaccorser.model.ServiceRequest;

/**
 * The key generation-adapter for service invocation.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/07/2015
 *
 * @param <REQ> the request type
 */
public interface KeyAdapter<REQ extends ServiceRequest> {
	
	/**
	 * Computes the key relative to the request to be invoked so as to allow the request to be later retrieved.
	 * 
	 * @param request the request whose kay is to be computed
	 * 
	 * @return the computed key
	 */
	String computeKey(REQ request);
	
}
