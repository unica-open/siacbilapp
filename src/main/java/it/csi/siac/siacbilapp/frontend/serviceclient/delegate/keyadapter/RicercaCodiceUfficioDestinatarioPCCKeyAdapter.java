/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCC;

/**
 * Key adapter for {@link DocumentoService#ricercaCodiceUfficioDestinatarioPCC(RicercaCodiceUfficioDestinatarioPCC)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class RicercaCodiceUfficioDestinatarioPCCKeyAdapter extends BaseKeyAdapter<RicercaCodiceUfficioDestinatarioPCC> {

	@Override
	public String computeNotNullKey(RicercaCodiceUfficioDestinatarioPCC req) {
		return computeEntitaKey(req.getRichiedente().getAccount().getEnte());
	}

}
