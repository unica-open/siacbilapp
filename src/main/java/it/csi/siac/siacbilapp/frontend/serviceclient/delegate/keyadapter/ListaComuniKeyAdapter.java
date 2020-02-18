/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;

/**
 * Key adapter for {@link GenericService#cercaComuni(ListaComuni)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 */
public class ListaComuniKeyAdapter extends BaseKeyAdapter<ListaComuni> {

	@Override
	protected String computeNotNullKey(ListaComuni req) {
		return computeNullableKey(req.getDescrizioneComune()) + DEFAULT_SEPARATOR + computeNullableKey(req.getIdStato());
	}

}
