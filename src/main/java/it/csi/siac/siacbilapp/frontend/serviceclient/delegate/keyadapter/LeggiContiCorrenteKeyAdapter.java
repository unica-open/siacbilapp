/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrente;

/**
 * Key adapter for {@link PreDocumentoEntrataService#leggiContiCorrente(LeggiContiCorrente)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class LeggiContiCorrenteKeyAdapter extends BaseKeyAdapter<LeggiContiCorrente> {

	@Override
	public String computeNotNullKey(LeggiContiCorrente req) {
		return computeEntitaKey(req.getEnte());
	}

}
