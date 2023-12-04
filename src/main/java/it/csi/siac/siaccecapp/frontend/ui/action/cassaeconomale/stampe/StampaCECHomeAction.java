/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.stampe;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.stampe.StampaCECHomeModel;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StampaCECHomeAction extends GenericStampaCECAction<StampaCECHomeModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -723059454737222875L;

	
}


