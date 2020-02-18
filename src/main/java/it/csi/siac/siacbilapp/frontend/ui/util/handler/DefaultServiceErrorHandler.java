/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.handler;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Handler di default per gli errori
 * @author Marchino Alessandro
 *
 */
public class DefaultServiceErrorHandler implements ServiceErrorHandler {

	@Override
	public <RES extends ServiceResponse, MOD extends GenericBilancioModel, ACT extends GenericBilancioAction<MOD>> void handleError(ACT action, RES response) {
		action.getModel().addErrori(response.getErrori());
		for(Errore err : response.getErrori()) {
			action.addActionError(err.getTesto());
		}
	}

}
