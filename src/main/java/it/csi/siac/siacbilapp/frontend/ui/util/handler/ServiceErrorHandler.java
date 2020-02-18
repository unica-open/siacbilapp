/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.handler;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Handler per gli errori del servizio
 * @author Marchino Alessandro
 *
 */
public abstract interface ServiceErrorHandler {

	/**
	 * Handling degli errori
	 * @param <RES> la tipizzazione della response
	 * @param <MOD> la tipizzazione del model
	 * @param <ACT> la tipizzazione della action
	 * @param action la action
	 * @param response la response
	 */
	<RES extends ServiceResponse, MOD extends GenericBilancioModel, ACT extends GenericBilancioAction<MOD>> void handleError(ACT action, RES response);

}
