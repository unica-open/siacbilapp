/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglio;

/**
 * Key adapter for {@link ClassificatoreBilService#leggiClassificatoriByIdFiglio(LeggiClassificatoriBilByIdFiglio)}.
 * 
 * @author Domenico
 * @version 1.0.0 - 17/06/2016
 *
 */
public class LeggiClassificatoriBilByIdFiglioKeyAdapter extends BaseKeyAdapter<LeggiClassificatoriBilByIdFiglio> {

	@Override
	public String computeNotNullKey(LeggiClassificatoriBilByIdFiglio req) {
		return req.getIdEnteProprietario() + DEFAULT_SEPARATOR + req.getIdFiglio() + DEFAULT_SEPARATOR + req.getAnno();
	}

}
