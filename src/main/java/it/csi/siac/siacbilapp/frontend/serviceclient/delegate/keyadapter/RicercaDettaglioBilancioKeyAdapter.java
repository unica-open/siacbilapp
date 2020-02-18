/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacbilser.frontend.webservice.BilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;

/**
 * Key adapter for {@link BilancioService#ricercaDettaglioBilancio(RicercaDettaglioBilancio)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 *
 */
public class RicercaDettaglioBilancioKeyAdapter extends BaseKeyAdapter<RicercaDettaglioBilancio> {

	@Override
	public String computeNotNullKey(RicercaDettaglioBilancio req) {
		return computeNullableKey(req.getChiaveBilancio());
	}

}
