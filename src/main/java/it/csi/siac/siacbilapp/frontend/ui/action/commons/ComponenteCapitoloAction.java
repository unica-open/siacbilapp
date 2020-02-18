/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.commons;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.ComponenteCapitoloModel;
import it.csi.siac.siacbilser.model.DecodificaTipoComponenteImportiCapitoloConMacrotipo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;

public abstract class ComponenteCapitoloAction<M extends ComponenteCapitoloModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1L;

	protected <T extends DecodificaTipoComponenteImportiCapitoloConMacrotipo> List<T> filterByMacrotipo(T[] values, MacrotipoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo) {
		List<T> result = new ArrayList<T>();
		if(macrotipoComponenteImportiCapitolo == null) {
			return result;
		}
		for(T value : values) {
			if(macrotipoComponenteImportiCapitolo.equals(value.getMacrotipoComponenteImportiCapitolo())) {
				result.add(value);
			}
		}
		return result;
	}
}
