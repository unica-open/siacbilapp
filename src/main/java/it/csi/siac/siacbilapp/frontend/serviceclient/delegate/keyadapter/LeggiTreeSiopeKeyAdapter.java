/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;

/**
 * Key adapter for {@link ClassificatoreBilService#leggiTreeSiopeSpesa(LeggiTreeSiope)}
 * and {@link ClassificatoreBilService#leggiTreeSiopeEntrata(LeggiTreeSiope)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class LeggiTreeSiopeKeyAdapter extends BaseKeyAdapter<LeggiTreeSiope> {

	@Override
	public String computeNotNullKey(LeggiTreeSiope o) {
		return o.getIdEnteProprietario() + DEFAULT_SEPARATOR + o.getAnno() + DEFAULT_SEPARATOR + o.getIdCodificaPadre();
	}

}
