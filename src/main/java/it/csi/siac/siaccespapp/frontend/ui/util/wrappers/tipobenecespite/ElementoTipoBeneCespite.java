/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.util.wrappers.tipobenecespite;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.TipoBeneCespite;

/**
 * The Class ElementoTipoBeneCespite.
 * @author elisa
 * @version 1.0.0 - 12-06-2018
 */
public class ElementoTipoBeneCespite implements Serializable, ModelWrapper {

	/** Per la serializzazione*/
	private static final long serialVersionUID = 6534716332399101142L;
	//FIELDS
	private TipoBeneCespite tipoBeneCespite;
	private String azioni;
	
	
	/**
	 * Instantiates a new elemento categoria cespiti.
	 *
	 * @param tipoBeneCespite the tipo bene cespite
	 */
	public ElementoTipoBeneCespite(TipoBeneCespite tipoBeneCespite) {
		this.tipoBeneCespite = tipoBeneCespite;
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	@Override
	public int getUid() {
		return tipoBeneCespite.getUid();
	}
	
	/**
	 * Gets the codice categoria.
	 *
	 * @return the codice categoria
	 */
	public String getCodice() {
		return tipoBeneCespite.getCodice();
	}
	
	/**
	 * Gets the stato.
	 *
	 * @return the stato
	 */
	public String getStato() {
	    return Boolean.TRUE.equals(tipoBeneCespite.getAnnullato()) ? "Annullato" : "Valido";
	}
	
	/**
	 * Gets the descrizione categoria.
	 *
	 * @return the descrizione categoria
	 */
	public String getDescrizione() {
		return tipoBeneCespite.getDescrizione();
	}
	
	/**
	/**
	 * Gets the tipo calcolo categoria.
	 *
	 * @return the tipo calcolo categoria
	 */
	public String getCategoria() {
		CategoriaCespiti categoriaCespiti = tipoBeneCespite.getCategoriaCespiti();
		if(categoriaCespiti == null) {
			return "";
		}

		return categoriaCespiti.getCodice()  + " - "  + categoriaCespiti.getDescrizione();
	}
	
        /**
         * Gets the stato categoria.
         *
         * @return the stato categoria
         */
        public String getStatoCategoria() {
        	return tipoBeneCespite.getCategoriaCespiti() == null ? "" : 
        	    Boolean.TRUE.equals(tipoBeneCespite.getCategoriaCespiti().getAnnullato()) ? "Annullato" : "Valido";
        }
	
	/**
	 * Checks if is tipo bene annullato.
	 *
	 * @return true, if is tipo bene annullato
	 */
	public boolean isTipoBeneAnnullato() {
		return Boolean.TRUE.equals(tipoBeneCespite.getAnnullato()); 
	}

}
