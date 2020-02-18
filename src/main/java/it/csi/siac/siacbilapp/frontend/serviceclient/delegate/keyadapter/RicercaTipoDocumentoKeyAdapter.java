/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;

/**
 * Key adapter for {@link DocumentoService#ricercaTipoDocumento(RicercaTipoDocumento)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 *
 */
public class RicercaTipoDocumentoKeyAdapter extends BaseKeyAdapter<RicercaTipoDocumento> {

	@Override
	public String computeNotNullKey(RicercaTipoDocumento req) {
		return computeEntitaKey(req.getEnte()) + "_" + computeNullableKey(req.getTipoFamDoc());
	}
	
}
