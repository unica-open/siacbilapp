/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * Selettore per la causale EP per il modulo GSA.
 * 
 * @author Marchino Alessandro
 * @author Valentina
 * @version 1.0.0 - 16/10/2015
 *
 */
public class CausaleEPGSASelector extends CausaleEPSelector {
	
	/**
	 * Costruttore.
	 */
	public CausaleEPGSASelector() {
		// Costruttore vuoto di default
	}

	/**
	 * Definire i criteri di assegnazione della causale per GSA.
	 */
	@Override
	public CausaleEP selezionaCausaleEP(Collection<CausaleEP> causali) {
		// TODO: quando ci sara' l'analisi
		final String methodName = "findCausaleEP";
		List<CausaleEP> listaCausaleEP = new ArrayList<CausaleEP>(causali);
		
		// Se ho un'unica causale, la prendo
		if(listaCausaleEP.size() == 1) {
			log.debug(methodName, "Un'unica causale era presente. La seleziono subito");
			return listaCausaleEP.get(0);
		}
		
		// Lotto N: presento la causale di default
		return selezionaCausaleDefault(listaCausaleEP);
	}


}
