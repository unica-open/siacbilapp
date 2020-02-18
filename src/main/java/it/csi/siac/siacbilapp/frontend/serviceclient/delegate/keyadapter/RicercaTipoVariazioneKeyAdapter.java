/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazione;

/**
 * Key adapter for {@link VariazioneDiBilancioService#ricercaTipoVariazione(RicercaTipoVariazione)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class RicercaTipoVariazioneKeyAdapter extends BaseKeyAdapter<RicercaTipoVariazione> {

	@Override
	public String computeNotNullKey(RicercaTipoVariazione req) {
		return computeEntitaKey(req.getEnte());
	}

}
