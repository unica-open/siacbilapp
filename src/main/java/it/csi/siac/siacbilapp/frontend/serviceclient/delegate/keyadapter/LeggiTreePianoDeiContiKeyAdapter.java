/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;

/**
 * Key adapter for {@link ClassificatoreBilService#leggiTreePianoDeiConti(LeggiTreePianoDeiConti)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class LeggiTreePianoDeiContiKeyAdapter extends BaseKeyAdapter<LeggiTreePianoDeiConti> {

	@Override
	public String computeNotNullKey(LeggiTreePianoDeiConti o) {
		return o.getIdEnteProprietario() + DEFAULT_SEPARATOR + o.getAnno() + DEFAULT_SEPARATOR + o.getIdCodificaPadre();
	}

}
