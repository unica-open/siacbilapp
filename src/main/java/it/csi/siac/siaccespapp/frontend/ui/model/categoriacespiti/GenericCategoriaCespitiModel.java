/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siaccespapp.frontend.ui.model.categoriacespiti;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespiti;
import it.csi.siac.siaccespser.model.CategoriaCalcoloTipoCespite;
import it.csi.siac.siaccespser.model.CategoriaCespiti;

/**
 * The Class GenericTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class GenericCategoriaCespitiModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3771106531639742450L;
	
	private List<CategoriaCalcoloTipoCespite> listaTipoCalcolo = new ArrayList<CategoriaCalcoloTipoCespite>();
	private CategoriaCespiti categoriaCespiti;
	private int uidCategoriaCespiti;
	/**
	 * @return the listaTipoCalcolo
	 */
	public List<CategoriaCalcoloTipoCespite> getListaTipoCalcolo() {
		return listaTipoCalcolo;
	}
	/**
	 * @param listaTipoCalcolo the listaTipoCalcolo to set
	 */
	public void setListaTipoCalcolo(List<CategoriaCalcoloTipoCespite> listaTipoCalcolo) {
		this.listaTipoCalcolo = listaTipoCalcolo;
	}
	/**
	 * @return the categoriaCespite
	 */
	public CategoriaCespiti getCategoriaCespiti() {
		return categoriaCespiti;
	}
	
	/**
	 * Sets the categoria cespiti.
	 *
	 * @param categoriaCespiti the new categoria cespiti
	 */
	public void setCategoriaCespiti(CategoriaCespiti categoriaCespiti) {
		this.categoriaCespiti = categoriaCespiti;
	}	
	
	/**
	 * @return the uidCategoriaCespiti
	 */
	public int getUidCategoriaCespiti() {
		return uidCategoriaCespiti;
	}
	/**
	 * @param uidCategoriaCespiti the uidCategoriaCespiti to set
	 */
	public void setUidCategoriaCespiti(int uidCategoriaCespiti) {
		this.uidCategoriaCespiti = uidCategoriaCespiti;
	}
	
	/**
	 * Crea request ricerca dettaglio categoria cespiti.
	 *
	 * @return the ricerca dettaglio categoria cespiti
	 */
	public RicercaDettaglioCategoriaCespiti creaRequestRicercaDettaglioCategoriaCespiti() {
		RicercaDettaglioCategoriaCespiti req = creaRequest(RicercaDettaglioCategoriaCespiti.class);
		CategoriaCespiti catCesp = new CategoriaCespiti();
		catCesp.setUid(getUidCategoriaCespiti());
		req.setCategoriaCespiti(catCesp);
		return req;
	}
	

}
