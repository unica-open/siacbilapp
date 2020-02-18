/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesa;

/**
 * Key adapter for {@link PreDocumentoSpesaService#leggiTipiCausaleSpesa(LeggiTipiCausaleSpesa)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class LeggiTipiCausaleSpesaKeyAdapter extends BaseKeyAdapter<LeggiTipiCausaleSpesa> {

	@Override
	public String computeNotNullKey(LeggiTipiCausaleSpesa req) {
		return computeEntitaKey(req.getEnte());
	}

}
