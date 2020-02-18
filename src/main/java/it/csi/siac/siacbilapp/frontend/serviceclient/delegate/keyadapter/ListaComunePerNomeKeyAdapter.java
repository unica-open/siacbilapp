/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNome;

/**
 * Key adapter for {@link GenericService#findComunePerNome(ListaComunePerNome)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 */
public class ListaComunePerNomeKeyAdapter extends BaseKeyAdapter<ListaComunePerNome> {

	@Override
	protected String computeNotNullKey(ListaComunePerNome req) {
		return computeNullableKey(req.getNomeComune()) + DEFAULT_SEPARATOR + computeNullableKey(req.getCodiceNazione())
			+ DEFAULT_SEPARATOR + req.isRicercaPuntuale();
	}

}
