/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di model per la ricerca degli eventi a partire dal tipo di evento.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 13/05/2015
 *
 */
public class RicercaTipoEventoByEntrataSpesaModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8492265956886380603L;
	
	private boolean spesa = false;
	private boolean entrata = false;
	private TipoCausale tipoCausale;
	private List<TipoEvento> listaTipoEvento = new ArrayList<TipoEvento>();
	// Cespiti
	private boolean filtroTipiEventoCogeInv = false;

	/** Costruttore vuoto di default */
	public RicercaTipoEventoByEntrataSpesaModel() {
		setTitolo("Ricerca Tipo Evento da entrata - spesa");
	}

	
	/**
	 * @return the spesa
	 */
	public boolean isSpesa() {
		return spesa;
	}

	/**
	 * @param spesa the spesa to set
	 */
	public void setSpesa(boolean spesa) {
		this.spesa = spesa;
	}

	/**
	 * @return the entrata
	 */
	public boolean isEntrata() {
		return entrata;
	}

	/**
	 * @param entrata the entrata to set
	 */
	public void setEntrata(boolean entrata) {
		this.entrata = entrata;
	}

	/**
	 * @return the tipoCausale
	 */
	public TipoCausale getTipoCausale() {
		return tipoCausale;
	}


	/**
	 * @param tipoCausale the tipoCausale to set
	 */
	public void setTipoCausale(TipoCausale tipoCausale) {
		this.tipoCausale = tipoCausale;
	}


	/**
	 * @return the listaTipoEvento
	 */
	public List<TipoEvento> getListaTipoEvento() {
		return listaTipoEvento;
	}

	/**
	 * @param listaTipoEvento the listaTipoEvento to set
	 */
	public void setListaTipoEvento(List<TipoEvento> listaTipoEvento) {
		this.listaTipoEvento = listaTipoEvento;
	}


	/**
	 * @return the filtroTipiEventoCogeInv
	 */
	public boolean isFiltroTipiEventoCogeInv() {
		return this.filtroTipiEventoCogeInv;
	}


	/**
	 * @param filtroTipiEventoCogeInv the filtroTipiEventoCogeInv to set
	 */
	public void setFiltroTipiEventoCogeInv(boolean filtroTipiEventoCogeInv) {
		this.filtroTipiEventoCogeInv = filtroTipiEventoCogeInv;
	}
	
}

