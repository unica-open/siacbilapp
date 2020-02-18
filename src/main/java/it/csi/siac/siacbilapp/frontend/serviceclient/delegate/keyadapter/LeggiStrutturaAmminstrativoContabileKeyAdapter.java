/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;

/**
 * Key adapter for {@link ClassificatoreService#leggiStrutturaAmminstrativoContabile(LeggiStrutturaAmminstrativoContabile)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 */
public class LeggiStrutturaAmminstrativoContabileKeyAdapter extends BaseKeyAdapter<LeggiStrutturaAmminstrativoContabile> {

	@Override
	protected String computeNotNullKey(LeggiStrutturaAmminstrativoContabile req) {
		return req.getIdEnteProprietario() + "_" + req.getAnno();
	}

}
