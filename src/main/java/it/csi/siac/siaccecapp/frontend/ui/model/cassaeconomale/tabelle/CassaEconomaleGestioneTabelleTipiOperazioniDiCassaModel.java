/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.tabelle;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.CassaEconomaleBaseModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipologiaOperazioneCassa;

/**
 * Classe di model per la gestione della tabella dei tipi operazioni di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/12/2014
 *
 */
public class CassaEconomaleGestioneTabelleTipiOperazioniDiCassaModel extends CassaEconomaleBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5274486885722666518L;
	
	private TipoOperazioneCassa tipoOperazioneCassa;
	private TipoOperazioneCassa tipoOperazioneCassaRicerca;
	private Boolean inserimentoAbilitato;
	private Boolean cassaInStatoValido;
	private OperazioneTipiOperazioneDiCassa operazioneTipiOperazioneDiCassa;
	
	// Lotto M
	private List<TipologiaOperazioneCassa> listaTipologiaOperazioneCassa = new ArrayList<TipologiaOperazioneCassa>();
	
	/** Costruttore vuoto di default */
	public CassaEconomaleGestioneTabelleTipiOperazioniDiCassaModel() {
		setTitolo("Tabella tipi operazioni di cassa");
	}

	/**
	 * @return the tipoOperazioneCassa
	 */
	public TipoOperazioneCassa getTipoOperazioneCassa() {
		return tipoOperazioneCassa;
	}

	/**
	 * @param tipoOperazioneCassa the tipoOperazioneCassa to set
	 */
	public void setTipoOperazioneCassa(TipoOperazioneCassa tipoOperazioneCassa) {
		this.tipoOperazioneCassa = tipoOperazioneCassa;
	}

	/**
	 * @return the tipoOperazioneCassaRicerca
	 */
	public TipoOperazioneCassa getTipoOperazioneCassaRicerca() {
		return tipoOperazioneCassaRicerca;
	}

	/**
	 * @param tipoOperazioneCassaRicerca the tipoOperazioneCassaRicerca to set
	 */
	public void setTipoOperazioneCassaRicerca(TipoOperazioneCassa tipoOperazioneCassaRicerca) {
		this.tipoOperazioneCassaRicerca = tipoOperazioneCassaRicerca;
	}

	/**
	 * @return the inserimentoAbilitato
	 */
	public Boolean getInserimentoAbilitato() {
		return inserimentoAbilitato;
	}

	/**
	 * @param inserimentoAbilitato the inserimentoAbilitato to set
	 */
	public void setInserimentoAbilitato(Boolean inserimentoAbilitato) {
		this.inserimentoAbilitato = inserimentoAbilitato;
	}
	
	/**
	 * @return the cassaInStatoValido
	 */
	public Boolean getCassaInStatoValido() {
		return cassaInStatoValido;
	}

	/**
	 * @param cassaInStatoValido the cassaInStatoValido to set
	 */
	public void setCassaInStatoValido(Boolean cassaInStatoValido) {
		this.cassaInStatoValido = cassaInStatoValido;
	}

	/**
	 * @return the operazioneTipiOperazioneDiCassa
	 */
	public OperazioneTipiOperazioneDiCassa getOperazioneTipiOperazioneDiCassa() {
		return operazioneTipiOperazioneDiCassa;
	}

	/**
	 * @param operazioneTipiOperazioneDiCassa the operazioneTipiOperazioneDiCassa to set
	 */
	public void setOperazioneTipiOperazioneDiCassa(OperazioneTipiOperazioneDiCassa operazioneTipiOperazioneDiCassa) {
		this.operazioneTipiOperazioneDiCassa = operazioneTipiOperazioneDiCassa;
	}
	
	/**
	 * @return the listaTipologiaOperazioneCassa
	 */
	public List<TipologiaOperazioneCassa> getListaTipologiaOperazioneCassa() {
		return listaTipologiaOperazioneCassa;
	}

	/**
	 * @param listaTipologiaOperazioneCassa the listaTipologiaOperazioneCassa to set
	 */
	public void setListaTipologiaOperazioneCassa(List<TipologiaOperazioneCassa> listaTipologiaOperazioneCassa) {
		this.listaTipologiaOperazioneCassa = listaTipologiaOperazioneCassa != null ? listaTipologiaOperazioneCassa : new ArrayList<TipologiaOperazioneCassa>();
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaTipoOperazioneDiCassa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaTipoOperazioneDiCassa creaRequestRicercaSinteticaTipoOperazioneDiCassa() {
		RicercaSinteticaTipoOperazioneDiCassa request = creaRequest(RicercaSinteticaTipoOperazioneDiCassa.class);
		
		getTipoOperazioneCassaRicerca().setEnte(getEnte());
		getTipoOperazioneCassaRicerca().setCassaEconomale(getCassaEconomale());
		
		request.setTipoOperazioneCassa(getTipoOperazioneCassaRicerca());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceTipoOperazioneDiCassa}.
	 * 
	 * @return la request creata
	 */
	public InserisceTipoOperazioneDiCassa creaRequestInserisceTipoOperazioneDiCassa() {
		InserisceTipoOperazioneDiCassa request = creaRequest(InserisceTipoOperazioneDiCassa.class);
		
		getTipoOperazioneCassa().setEnte(getEnte());
		getTipoOperazioneCassa().setCassaEconomale(getCassaEconomale());
		request.setTipoOperazioneCassa(getTipoOperazioneCassa());
		
		request.setBilancio(getBilancio());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaTipoOperazioneDiCassa}.
	 * 
	 * @return la request creata
	 */
	public AggiornaTipoOperazioneDiCassa creaRequestAggiornaTipoOperazioneDiCassa() {
		AggiornaTipoOperazioneDiCassa request = creaRequest(AggiornaTipoOperazioneDiCassa.class);
		
		getTipoOperazioneCassa().setEnte(getEnte());
		getTipoOperazioneCassa().setCassaEconomale(getCassaEconomale());
		request.setTipoOperazioneCassa(getTipoOperazioneCassa());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AnnullaTipoOperazioneDiCassa}.
	 * 
	 * @return la request creata
	 */
	public AnnullaTipoOperazioneDiCassa creaRequestAnnullaTipoOperazioneDiCassa() {
		AnnullaTipoOperazioneDiCassa request = creaRequest(AnnullaTipoOperazioneDiCassa.class);
		
		request.setBilancio(getBilancio());
		
		getTipoOperazioneCassa().setCassaEconomale(getCassaEconomale());
		request.setTipoOperazioneCassa(getTipoOperazioneCassa());
		
		return request;
	}
	
}
