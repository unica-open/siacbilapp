/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacbilser.frontend.webservice.ProgettoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;

/**
 * Key adapter for {@link ProgettoService#ricercaTipiAmbito(RicercaTipiAmbito)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 */
public class RicercaTipiAmbitoKeyAdapter extends BaseKeyAdapter<RicercaTipiAmbito> {

	@Override
	protected String computeNotNullKey(RicercaTipiAmbito req) {
		return computeEntitaKey(req.getEnte()) + DEFAULT_SEPARATOR + computeNullableKey(req.getAnno());
	}

}
