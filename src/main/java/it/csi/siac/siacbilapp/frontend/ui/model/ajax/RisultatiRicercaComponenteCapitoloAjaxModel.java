/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;



import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.anagcomp.ElementoComponenteCapitolo;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 15/set/2014
 *
 */
public class RisultatiRicercaComponenteCapitoloAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoComponenteCapitolo> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4029533272809633835L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaComponenteCapitoloAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Componente Capitolo - AJAX");
	}
	 
}
