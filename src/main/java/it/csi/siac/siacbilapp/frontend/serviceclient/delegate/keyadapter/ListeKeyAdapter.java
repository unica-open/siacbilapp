/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import java.util.Arrays;

import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;

/**
 * Key adapter for {@link GenericService#liste(Liste)}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 */
public class ListeKeyAdapter extends BaseKeyAdapter<Liste> {

	@Override
	protected String computeNotNullKey(Liste req) {
		return computeEntitaKey(req.getEnte()) + computeTipiKey(req.getTipi()) + computeEntitaKey(req.getBilancio());
	}
	
	/**
	 * Computes the key for the array of tipiLista.
	 * 
	 * @param tipiListas the tipiLista whose key is to be computed
	 * 
	 * @return the computed key
	 */
	private String computeTipiKey(TipiLista[] tipiListas) {
		StringBuilder sb = new StringBuilder(DEFAULT_SEPARATOR);
		if(tipiListas == null) {
			sb.append(DEFAULT_SEPARATOR)
				.append(DEFAULT_NULL_VALUE);
		} else {
			// Sorts to prevent different cached values for different parameters order
			Arrays.sort(tipiListas);
			for(TipiLista tipiLista : tipiListas) {
				sb.append(DEFAULT_SEPARATOR)
					.append(computeNullableKey(tipiLista));
			}
		}
		return sb.toString();
	}
	
}
