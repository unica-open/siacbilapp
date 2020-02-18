/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.provvedimento;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;

/**
 * @author elisa Chiari
 * @version 1.0.0 - 16/02/2016
 *
 */
public class TipoAttoAjaxModel extends GenericBilancioModel {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -3836302347992957436L;
	
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	
	/** Costruttore */
	public TipoAttoAjaxModel() {
		super();
		setTitolo("Ajax Model");
	}

	/**
	 * @return the listaTipoAtto
	 */
	public List<TipoAtto> getListaTipoAtto() {
		return listaTipoAtto;
	}

	/**
	 * @param listaTipoAtto the listaTipoAtto to set
	 */
	public void setListaTipoAtto(List<TipoAtto> listaTipoAtto) {
		this.listaTipoAtto = listaTipoAtto;
	}
	

}
