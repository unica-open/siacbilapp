/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;

/**
 * Key adapter for {@link DocumentoService#ricercaCodicePCC(RicercaCodicePCC)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class RicercaCodicePCCKeyAdapter extends BaseKeyAdapter<RicercaCodicePCC> {

	@Override
	public String computeNotNullKey(RicercaCodicePCC req) {
		return computeEntitaKey(req.getRichiedente().getAccount().getEnte());
	}

}
