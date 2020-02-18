/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;

/**
 * Key adapter for {@link ClassificatoreBilService#leggiClassificatoriByIdPadre(LeggiClassificatoriBilByIdPadre)}.
 * 
 * @author Domenico
 * @version 1.0.0 - 30/09/2014
 *
 */
public class LeggiClassificatoriBilByIdPadreKeyAdapter extends BaseKeyAdapter<LeggiClassificatoriBilByIdPadre> {

	@Override
	public String computeNotNullKey(LeggiClassificatoriBilByIdPadre req) {
		return req.getIdEnteProprietario() + DEFAULT_SEPARATOR + req.getIdPadre() + DEFAULT_SEPARATOR + req.getAnno();
	}

}
