/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;

/**
 * Key adapter for {@link DocumentoService#ricercaCausale770(RicercaCausale770)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class RicercaCausale770KeyAdapter extends BaseKeyAdapter<RicercaCausale770> {

	@Override
	public String computeNotNullKey(RicercaCausale770 o) {
		return computeEntitaKey(o.getEnte()) + DEFAULT_SEPARATOR + computeEntitaKey(o.getTipoOnere());
	}

}
