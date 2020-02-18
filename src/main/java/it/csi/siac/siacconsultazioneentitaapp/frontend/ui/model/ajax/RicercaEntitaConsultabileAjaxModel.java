/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaFigliEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;

/**
 * Classe di model per la ricerca del capitolo
 * @author Elisa Chiari
 * @version 1.0.0 - 23/02/2016
 */
public class RicercaEntitaConsultabileAjaxModel extends RicercaEntitaConsultabileBaseAjaxModel {

	/**
	 * Pewr la serializzazione
	 */
	private static final long serialVersionUID = 8750250966267108474L;

	private TipoEntitaConsultabile tipoEntitaConsultabilePadre;
	private TipoEntitaConsultabile tipoEntitaConsultabile;
	private Integer uidEntitaConsultabilePadre;
	// SIAC-6193: filtri generici
	private List<String> listaFiltroGenerico = new ArrayList<String>();
	private boolean requestImporto;
	
	private List<EntitaConsultabile> elencoEntitaConsultabili = new ArrayList<EntitaConsultabile>();
	private BigDecimal importoEntitaConsultabili;
	
	/**Costruttore*/
	public RicercaEntitaConsultabileAjaxModel() {
		super();
		setTitolo("Ajax model");
	}

	/**
	 * @return the tipoEntitaConsultabilePadre
	 */
	public TipoEntitaConsultabile getTipoEntitaConsultabilePadre() {
		return tipoEntitaConsultabilePadre;
	}

	/**
	 * @param tipoEntitaConsultabilePadre the tipoEntitaConsultabilePadre to set
	 */
	public void setTipoEntitaConsultabilePadre(TipoEntitaConsultabile tipoEntitaConsultabilePadre) {
		this.tipoEntitaConsultabilePadre = tipoEntitaConsultabilePadre;
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
	 * @return the uidentitaPadre
	 */
	public Integer getUidEntitaConsultabilePadre() {
		return uidEntitaConsultabilePadre;
	}

	/**
	 * @param uidEntitaConsultabilePadre the uidentitaPadre to set
	 */
	public void setUidEntitaConsultabilePadre(Integer uidEntitaConsultabilePadre) {
		this.uidEntitaConsultabilePadre = uidEntitaConsultabilePadre;
	}

	/**
	 * @return the listaFiltroGenerico
	 */
	public List<String> getListaFiltroGenerico() {
		return this.listaFiltroGenerico;
	}

	/**
	 * @param listaFiltroGenerico the listaFiltroGenerico to set
	 */
	public void setListaFiltroGenerico(List<String> listaFiltroGenerico) {
		this.listaFiltroGenerico = listaFiltroGenerico;
	}

	/**
	 * @return the requestImporto
	 */
	public boolean isRequestImporto() {
		return this.requestImporto;
	}

	/**
	 * @param requestImporto the requestImporto to set
	 */
	public void setRequestImporto(boolean requestImporto) {
		this.requestImporto = requestImporto;
	}

	/**
	 * @return the elencoEntitaConsultabili
	 */
	public List<EntitaConsultabile> getElencoEntitaConsultabili() {
		return elencoEntitaConsultabili;
	}

	/**
	 * @param elencoEntitaConsultabili the elencoEntitaConsultabili to set 
	 */
	public void setElencoEntitaConsultabili(List<EntitaConsultabile> elencoEntitaConsultabili) {
		this.elencoEntitaConsultabili = elencoEntitaConsultabili != null ? elencoEntitaConsultabili : new ArrayList<EntitaConsultabile>();
	}
	
	/**
	 * @return the importoEntitaConsultabili
	 */
	public BigDecimal getImportoEntitaConsultabili() {
		return this.importoEntitaConsultabili;
	}

	/**
	 * @param importoEntitaConsultabili the importoEntitaConsultabili to set
	 */
	public void setImportoEntitaConsultabili(BigDecimal importoEntitaConsultabili) {
		this.importoEntitaConsultabili = importoEntitaConsultabili;
	}

	/**
	 * Crea una request per il servizio di RicercaFigliEntitaConsultabile.
	 * @return la request
	 */
	public RicercaFigliEntitaConsultabile creaRequestRicercaFigliEntitaConsultabili(){
		RicercaFigliEntitaConsultabile req = creaRequest(RicercaFigliEntitaConsultabile.class);
		req.setEnte(getEnte());
		req.setAnnoEsercizio(getAnnoEsercizioInt());
		
		EntitaConsultabile entitaConsultabilePadre = new EntitaConsultabile();
		entitaConsultabilePadre.setTipoEntitaConsultabile(getTipoEntitaConsultabilePadre());
		entitaConsultabilePadre.setUid(uidEntitaConsultabilePadre);
		req.setEntitaPadre(entitaConsultabilePadre);
		
		// SIAC-6193: wrap to object list
		req.setListaParametriGenerici(new ArrayList<Object>(getListaFiltroGenerico()));
		
		req.setEntitaDaCercare(getTipoEntitaConsultabile());
		req.setParametriPaginazione(creaParametriPaginazione());
		req.setRequestImporto(isRequestImporto());

		return req;
	}

}
