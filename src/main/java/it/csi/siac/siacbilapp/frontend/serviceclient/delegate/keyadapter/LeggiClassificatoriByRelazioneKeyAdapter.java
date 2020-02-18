/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;

/**
 * Key adapter for {@link ClassificatoreBilService#leggiClassificatoriByRelazione(LeggiClassificatoriByRelazione)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 *
 */
public class LeggiClassificatoriByRelazioneKeyAdapter extends BaseKeyAdapter<LeggiClassificatoriByRelazione> {

	@Override
	public String computeNotNullKey(LeggiClassificatoriByRelazione req) {
		return computeNullableKeys(req.getEnte().getUid(), req.getIdClassif(), req.getAnno());
	}
	
}
