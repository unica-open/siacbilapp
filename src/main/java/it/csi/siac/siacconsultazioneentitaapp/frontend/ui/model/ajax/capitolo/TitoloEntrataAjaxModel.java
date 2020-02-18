/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.capitolo;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.TitoloEntrata;

/**
 * @author Elisa Chiari
 * @version 1.0.0 - 16/02/2016
 *
 */
public class TitoloEntrataAjaxModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4879487753102873256L;
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	
	/**
	 * costruttore
	 */
	public TitoloEntrataAjaxModel() {
		super();
		setTitolo("Ajax Model");
	}

	/**
	 * @return listaTitoloEntrata
	 */
	public List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata;
	}

}
