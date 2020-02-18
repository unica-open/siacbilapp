/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenerico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Key adapter for {@link ClassificatoreBilService#ricercaTipoClassificatore(RicercaTipoClassificatore)}
 * and {@link ClassificatoreBilService#ricercaTipoClassificatore(RicercaTipoClassificatore)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/08/2017
 */
public class RicercaTipoClassificatoreGenericoKeyAdapter extends BaseKeyAdapter<RicercaTipoClassificatoreGenerico> {

	@Override
	public String computeNotNullKey(RicercaTipoClassificatoreGenerico o) {
		StringBuilder sb = new StringBuilder();
		sb.append(o.getRichiedente().getAccount().getEnte()).append(DEFAULT_SEPARATOR)
			.append(o.getAnno()).append(DEFAULT_SEPARATOR)
			.append(o.getTipoElementoBilancio()).append(DEFAULT_SEPARATOR);
		
		for(TipologiaClassificatore tc : o.getTipologieClassificatore()) {
			sb.append(tc.name()).append("|");
		}
		
		return sb.toString();
	}

}
