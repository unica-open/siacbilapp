/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.tabelle;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.CassaEconomaleBaseModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoGiustificativo;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;

/**
 * Classe di model per la gestione della tabella dei tipi giustificativo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/12/2014
 *
 */
public class CassaEconomaleGestioneTabelleTipiGiustificativiModel extends CassaEconomaleBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7092049045864356813L;
	
	private TipoGiustificativo tipoGiustificativo;
	private TipoGiustificativo tipoGiustificativoRicerca;
	private Boolean inserimentoAbilitato;
	private Boolean cassaInStatoValido;
	private OperazioneTipiGiustificativo operazioneTipiGiustificativo;
	
	private List<TipologiaGiustificativo> listaTipologiaGiustificativo = new ArrayList<TipologiaGiustificativo>();
	
	/** Costruttore vuoto di default */
	public CassaEconomaleGestioneTabelleTipiGiustificativiModel() {
		setTitolo("Tabella giustificativi");
	}

	/**
	 * @return the tipoGiustificativo
	 */
	public TipoGiustificativo getTipoGiustificativo() {
		return tipoGiustificativo;
	}

	/**
	 * @param tipoGiustificativo the tipoGiustificativo to set
	 */
	public void setTipoGiustificativo(TipoGiustificativo tipoGiustificativo) {
		this.tipoGiustificativo = tipoGiustificativo;
	}

	/**
	 * @return the tipoGiustificativoRicerca
	 */
	public TipoGiustificativo getTipoGiustificativoRicerca() {
		return tipoGiustificativoRicerca;
	}

	/**
	 * @param tipoGiustificativoRicerca the tipoGiustificativoRicerca to set
	 */
	public void setTipoGiustificativoRicerca(TipoGiustificativo tipoGiustificativoRicerca) {
		this.tipoGiustificativoRicerca = tipoGiustificativoRicerca;
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
	 * @return the operazioneTipiGiustificativo
	 */
	public OperazioneTipiGiustificativo getOperazioneTipiGiustificativo() {
		return operazioneTipiGiustificativo;
	}

	/**
	 * @param operazioneTipiGiustificativo the operazioneTipiGiustificativo to set
	 */
	public void setOperazioneTipiGiustificativo(OperazioneTipiGiustificativo operazioneTipiGiustificativo) {
		this.operazioneTipiGiustificativo = operazioneTipiGiustificativo;
	}
	
	/**
	 * @return the listaTipologiaGiustificativo
	 */
	public List<TipologiaGiustificativo> getListaTipologiaGiustificativo() {
		return listaTipologiaGiustificativo;
	}

	/**
	 * @param listaTipologiaGiustificativo the listaTipologiaGiustificativo to set
	 */
	public void setListaTipologiaGiustificativo(List<TipologiaGiustificativo> listaTipologiaGiustificativo) {
		this.listaTipologiaGiustificativo = listaTipologiaGiustificativo != null ? listaTipologiaGiustificativo : new ArrayList<TipologiaGiustificativo>();
	}
	
	/**
	 * @return the tipoGiustificativoAnticipoMissione
	 */
	public boolean isTipoGiustificativoAnticipoMissione() {
		return tipoGiustificativo != null && TipologiaGiustificativo.ANTICIPO_MISSIONE.equals(tipoGiustificativo.getTipologiaGiustificativo());
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaTipoGiustificativo}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaTipoGiustificativo creaRequestRicercaSinteticaTipoGiustificativo() {
		RicercaSinteticaTipoGiustificativo request = creaRequest(RicercaSinteticaTipoGiustificativo.class);
		
		getTipoGiustificativoRicerca().setEnte(getEnte());
		getTipoGiustificativoRicerca().setCassaEconomale(getCassaEconomale());
		request.setTipoGiustificativo(getTipoGiustificativoRicerca());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceTipoGiustificativo}.
	 * 
	 * @return la request creata
	 */
	public InserisceTipoGiustificativo creaRequestInserisceTipoGiustificativo() {
		InserisceTipoGiustificativo request = creaRequest(InserisceTipoGiustificativo.class);
		
		getTipoGiustificativo().setEnte(getEnte());
		getTipoGiustificativo().setCassaEconomale(getCassaEconomale());
		request.setTipoGiustificativo(getTipoGiustificativo());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaTipoGiustificativo}.
	 * 
	 * @return la request creata
	 */
	public AggiornaTipoGiustificativo creaRequestAggiornaTipoGiustificativo() {
		AggiornaTipoGiustificativo request = creaRequest(AggiornaTipoGiustificativo.class);
		
		getTipoGiustificativo().setEnte(getEnte());
		getTipoGiustificativo().setCassaEconomale(getCassaEconomale());
		request.setTipoGiustificativo(getTipoGiustificativo());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AnnullaTipoGiustificativo}.
	 * 
	 * @return la request creata
	 */
	public AnnullaTipoGiustificativo creaRequestAnnullaTipoGiustificativo() {
		AnnullaTipoGiustificativo request = creaRequest(AnnullaTipoGiustificativo.class);
		
		request.setBilancio(getBilancio());
		request.setTipoGiustificativo(getTipoGiustificativo());
		
		return request;
	}
	
}
