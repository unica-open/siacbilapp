/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBollo;

/**
 * Key adapter for {@link DocumentoService#ricercaCodiceBollo(RicercaCodiceBollo)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class RicercaCodiceBolloKeyAdapter extends BaseKeyAdapter<RicercaCodiceBollo> {

	@Override
	public String computeNotNullKey(RicercaCodiceBollo req) {
		return computeEntitaKey(req.getEnte());
	}

}
