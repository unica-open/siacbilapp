/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.OttieniNavigazioneTipoEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;

/**
 * @author Elisa Chiari
 * @version 1.0.0 - 17/02/2016
 *
 */
public class OttieniFigliEntitaConsultabileAjaxModel extends GenericBilancioModel {

	/**Per la serializzazione*/
	private static final long serialVersionUID = 7216817949897427680L;
	//private Integer idPadre;
	//private String etichettaEntita;
	//private EntitaConsultabili tipoEntitaConsultabilePadre;
	//private String etichettaEntitaPadre;
	//private String nomeClasseEntita;
	private TipoEntitaConsultabile tipoEntitaConsultabile;
	private boolean isParent = true;

	
	//private List<String> listaEntitaConsultabiliFiglie = new ArrayList<String>();
	private List<TipoEntitaConsultabile> listaFigliEntitaConsultabile = new ArrayList<TipoEntitaConsultabile>();
	
	/** Costruttore*/
	public OttieniFigliEntitaConsultabileAjaxModel() {
		super();
		setTitolo("Ajax Model");
	}

	/**
	 * @return listaEntitaSelezionabili
	 */
	public List<TipoEntitaConsultabile> getListaFigliEntitaConsultabile() {
		return listaFigliEntitaConsultabile;
	}

	/**
	 * @param listaFigliEntitaConsultabile the listaFigliEntitaConsultabile to set
	 */
	public void setListaFigliEntitaConsultabile(List<TipoEntitaConsultabile> listaFigliEntitaConsultabile) {
		this.listaFigliEntitaConsultabile = listaFigliEntitaConsultabile;
	}
	
	/**
	 * @return the tipoEntitaConsultabile
	 */
	public TipoEntitaConsultabile getTipoEntitaConsultabile() {
		return tipoEntitaConsultabile;
	}

	/**
	 * @param tipoEntitaConsultabile the tipoEntitaConsultabile to set
	 */
	public void setTipoEntitaConsultabile(TipoEntitaConsultabile tipoEntitaConsultabile) {
		this.tipoEntitaConsultabile = tipoEntitaConsultabile;
	}

	/**
	 * @return the parent
	 */
	public boolean isParent() {
		return isParent;
	}

	/**
	 * @param isParent the parent to set
	 */
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	
	/**
	 * @return  la request per la ricerca delle entita figlie
	 */
	public OttieniNavigazioneTipoEntitaConsultabile creaRequestOttieniFigliEntitaConsultabile(){
		OttieniNavigazioneTipoEntitaConsultabile request = creaRequest(OttieniNavigazioneTipoEntitaConsultabile.class);
		
		request.setTipoEntitaConsultabile(tipoEntitaConsultabile);	
		return request;
	
	}

}
