/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.capitolo;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.TitoloSpesa;

/**
 * @author Elisa Chiari
 * @version 1.0.0 - 16/02/2016
 *
 */
public class TitoloSpesaAjaxModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4118691035904820839L;
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	/**
	 * costruttore
	 */
	public TitoloSpesaAjaxModel() {
		super();
		setTitolo("Ajax Model");
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa;
	}

}
