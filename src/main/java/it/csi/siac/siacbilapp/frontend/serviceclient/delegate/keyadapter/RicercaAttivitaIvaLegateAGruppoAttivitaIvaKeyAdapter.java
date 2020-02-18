/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaLegateAGruppoAttivitaIva;

/**
 * Key adapter for {@link DocumentoIvaService#ricercaAttivitaIvaLegateAGruppoAttivitaIva(RicercaAttivitaIvaLegateAGruppoAttivitaIva)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 */
public class RicercaAttivitaIvaLegateAGruppoAttivitaIvaKeyAdapter extends BaseKeyAdapter<RicercaAttivitaIvaLegateAGruppoAttivitaIva> {

	@Override
	public String computeNotNullKey(RicercaAttivitaIvaLegateAGruppoAttivitaIva req) {
		return computeEntitaKey(req.getGruppoAttivitaIva());
	}

}
