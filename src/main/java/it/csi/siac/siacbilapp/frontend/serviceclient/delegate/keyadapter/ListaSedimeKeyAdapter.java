/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSedime;

/**
 * Key adapter for {@link GenericService#listaSedime(ListaSedime)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 */
public class ListaSedimeKeyAdapter extends BaseKeyAdapter<ListaSedime> {

	@Override
	protected String computeNotNullKey(ListaSedime req) {
		return computeNullableKey(req.getDescrizioneSedime());
	}

}
