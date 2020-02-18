/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvviso;

/**
 * Key adapter for {@link DocumentoService#ricercaTipoAvviso(RicercaTipoAvviso)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class RicercaTipoAvvisoKeyAdapter extends BaseKeyAdapter<RicercaTipoAvviso> {

	@Override
	public String computeNotNullKey(RicercaTipoAvviso req) {
		return computeEntitaKey(req.getEnte());
	}

}
