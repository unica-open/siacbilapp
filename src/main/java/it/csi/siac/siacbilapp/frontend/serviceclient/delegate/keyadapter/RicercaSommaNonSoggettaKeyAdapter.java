/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggetta;

/**
 * Key adapter for {@link DocumentoService#ricercaSommaNonSoggetta(RicercaSommaNonSoggetta)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 17/06/2016
 */
public class RicercaSommaNonSoggettaKeyAdapter extends BaseKeyAdapter<RicercaSommaNonSoggetta> {

	@Override
	public String computeNotNullKey(RicercaSommaNonSoggetta req) {
		return computeEntitaKey(req.getTipoOnere());
	}

}
