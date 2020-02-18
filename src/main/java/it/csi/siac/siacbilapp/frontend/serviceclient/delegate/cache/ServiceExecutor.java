/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache;

import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Interface for the execution of a generic service.
 * 
 * @author Domenico
 * @version 1.0.0 - 30/09/2014
 *
 * @param <REQ> the request type
 * @param <RES> the response type
 */
public interface ServiceExecutor<REQ extends ServiceRequest, RES extends ServiceResponse> {
	
	/**
	 * Executes the service.
	 * 
	 * @param req the request for the service
	 * 
	 * @return the response from the service
	 */
	RES executeService(REQ req);

}
