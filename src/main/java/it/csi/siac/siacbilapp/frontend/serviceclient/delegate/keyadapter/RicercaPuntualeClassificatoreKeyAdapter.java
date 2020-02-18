/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeClassificatore;

/**
 * Key adapter for {@link ClassificatoreBilService#ricercaPuntualeClassificatore(RicercaPuntualeClassificatore)}
 * and {@link ClassificatoreBilService#ricercaPuntualeClassificatore(RicercaPuntualeClassificatore)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class RicercaPuntualeClassificatoreKeyAdapter extends BaseKeyAdapter<RicercaPuntualeClassificatore> {

	@Override
	public String computeNotNullKey(RicercaPuntualeClassificatore o) {
		return o.getUid() + DEFAULT_SEPARATOR + o.getCodice() + DEFAULT_SEPARATOR
				+ o.getTipologiaClassificatore() + DEFAULT_SEPARATOR
				+ (o.getBilancio() != null ? Integer.toString(o.getBilancio().getAnno()) : DEFAULT_NULL_VALUE);
	}

}
