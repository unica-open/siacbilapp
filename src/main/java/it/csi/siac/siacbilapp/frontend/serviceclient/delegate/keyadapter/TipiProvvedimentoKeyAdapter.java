/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;

/**
 * Key adapter for {@link ProvvedimentoService#getTipiProvvedimento(TipiProvvedimento)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class TipiProvvedimentoKeyAdapter extends BaseKeyAdapter<TipiProvvedimento> {

	@Override
	public String computeNotNullKey(TipiProvvedimento req) {
		return computeEntitaKey(req.getEnte());
	}

}
