/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action;


import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccommonapp.util.log.LogWebUtil;

public abstract class BaseActionHelper {

	protected transient LogWebUtil log = new LogWebUtil(this.getClass());

	protected GenericBilancioAction<? extends GenericBilancioModel> action;

	protected BaseActionHelper(GenericBilancioAction<? extends GenericBilancioModel> action) {
		this.action = action;
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	protected GenericBilancioAction<? extends GenericBilancioModel> getAction() {
		return action;
	}
}
