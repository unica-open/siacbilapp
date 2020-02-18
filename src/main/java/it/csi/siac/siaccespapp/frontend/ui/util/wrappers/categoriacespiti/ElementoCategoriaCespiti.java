/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.util.wrappers.categoriacespiti;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccespser.model.CategoriaCespiti;

/**
 * The Class ElementoCategoriaCespiti.
 * @author elisa
 * @version 1.0.0 - 12-06-2018
 */
public class ElementoCategoriaCespiti implements Serializable, ModelWrapper {

	/** Per la serializzazione*/
	private static final long serialVersionUID = 6534716332399101142L;
	//FIELDS
	private CategoriaCespiti categoriaCespiti;
	private String azioni;
	
	
	/**
	 * Instantiates a new elemento categoria cespiti.
	 *
	 * @param categoriaCespiti the categoria cespiti
	 */
	public ElementoCategoriaCespiti(CategoriaCespiti categoriaCespiti) {
		this.categoriaCespiti = categoriaCespiti;
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
		return categoriaCespiti.getUid();
	}
	
	/**
	 * Gets the codice categoria.
	 *
	 * @return the codice categoria
	 */
	public String getCodiceCategoria() {
		return categoriaCespiti.getCodice();
	}
	
	/**
	 * Gets the stato categoria.
	 *
	 * @return the stato categoria
	 */
	public String getStatoCategoria() {
		return isAnnullato() ? "Annullato" : "Valido";
	}
	
	/**
	 * Gets the descrizione categoria.
	 *
	 * @return the descrizione categoria
	 */
	public String getDescrizioneCategoria() {
		return categoriaCespiti.getDescrizione();
	}
	
	/**
	 * Gets the aliquota categoria.
	 *
	 * @return the aliquota categoria
	 */
	public String getAliquotaCategoria() {
		return FormatUtils.formatCurrency(categoriaCespiti.getAliquotaAnnua());
	}
	
	/**
	 * Gets the tipo calcolo categoria.
	 *
	 * @return the tipo calcolo categoria
	 */
	public String getTipoCalcoloCategoria() {
		return categoriaCespiti.getCategoriaCalcoloTipoCespite() != null? categoriaCespiti.getCategoriaCalcoloTipoCespite().getDescrizione() : "";
	}
	
	/**
	 * Checks if is annullato.
	 *
	 * @return true, if is annullato
	 */
	public boolean isAnnullato() {
		return Boolean.TRUE.equals(categoriaCespiti.getAnnullato());
	}

}
