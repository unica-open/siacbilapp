/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;

/**
 * Key adapter for {@link ClassificatoreBilService#leggiClassificatoriByTipoElementoBil(LeggiClassificatoriByTipoElementoBil)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class LeggiClassificatoriByTipoElementoBilKeyAdapter extends BaseKeyAdapter<LeggiClassificatoriByTipoElementoBil> {

	@Override
	public String computeNotNullKey(LeggiClassificatoriByTipoElementoBil o) {
		return o.getIdEnteProprietario() + DEFAULT_SEPARATOR + computeNullableKey(o.getTipoElementoBilancio())
				+ DEFAULT_SEPARATOR + o.getAnno();
	}

}
