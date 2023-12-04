/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceCommissioneDocumento;

/**
 * Key adapter for {@link DocumentoService#ricercaCommissioneDocumento(RicercaCodiceCommissioneDocumento)}.
 * 
 */
public class RicercaCodiceCommissioneDocumentoKeyAdapter extends BaseKeyAdapter<RicercaCodiceCommissioneDocumento> {

	@Override
	public String computeNotNullKey(RicercaCodiceCommissioneDocumento req) {
		return computeEntitaKey(req.getEnte());
	}

}
