/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;

/**
 * Key adapter for {@link DocumentoIvaService#ricercaAttivitaIva(RicercaAttivitaIva)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 *
 */
public class RicercaAttivitaIvaKeyAdapter extends BaseKeyAdapter<RicercaAttivitaIva> {

	@Override
	public String computeNotNullKey(RicercaAttivitaIva req) {
		return computeEntitaKey(req.getEnte()) + DEFAULT_SEPARATOR + computeEntitaKey(req.getAttivitaIva());
	}

}
