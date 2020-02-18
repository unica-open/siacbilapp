/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnere;

/**
 * Key adapter for {@link DocumentoService#ricercaTipoOnere(RicercaTipoOnere)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class RicercaTipoOnereKeyAdapter extends BaseKeyAdapter<RicercaTipoOnere> {

	@Override
	public String computeNotNullKey(RicercaTipoOnere o) {
		return computeEntitaKey(o.getEnte()) + DEFAULT_SEPARATOR + computeEntitaKey(o.getNaturaOnere());
	}

}
