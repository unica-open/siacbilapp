/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrata;

/**
 * Key adapter for {@link PreDocumentoEntrataService#leggiTipiCausaleEntrata(LeggiTipiCausaleEntrata)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class LeggiTipiCausaleEntrataKeyAdapter extends BaseKeyAdapter<LeggiTipiCausaleEntrata> {

	@Override
	public String computeNotNullKey(LeggiTipiCausaleEntrata req) {
		return computeEntitaKey(req.getEnte());
	}

}
