/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;

/**
 * Key adapter for {@link SoggettoService#listeGestioneSoggetto(ListeGestioneSoggetto)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 */
public class ListeGestioneSoggettoKeyAdapter extends BaseKeyAdapter<ListeGestioneSoggetto> {

	@Override
	protected String computeNotNullKey(ListeGestioneSoggetto req) {
		return computeNullableKey(req.getDescrizioneComune()) + DEFAULT_SEPARATOR + computeNullableKey(req.getIdAmbito());
	}

}
