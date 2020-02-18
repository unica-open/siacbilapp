/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.rimborsospese;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseInserisciAggiornaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe base di model per l'inserimento e l'aggiornamento dell'anticipo spese per missione.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 *
 */
public abstract class BaseInserisciAggiornaRimborsoSpeseCassaEconomaleModel extends BaseInserisciAggiornaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4564513931435646248L;

	// Step 1
	private List<Giustificativo> listaGiustificativo = new ArrayList<Giustificativo>();

	private Integer uidValutaEuro;
	private Giustificativo giustificativo;
	private Integer rowNumber;
	
	private List<TipoGiustificativo> listaTipoGiustificativo = new ArrayList<TipoGiustificativo>();
	private List<Valuta> listaValuta = new ArrayList<Valuta>();


	/**
	 * @return the listaGiustificativo
	 */
	public List<Giustificativo> getListaGiustificativo() {
		return listaGiustificativo;
	}

	/**
	 * @param listaGiustificativo the listaGiustificativo to set
	 */
	public void setListaGiustificativo(List<Giustificativo> listaGiustificativo) {
		this.listaGiustificativo = listaGiustificativo != null ? listaGiustificativo : new ArrayList<Giustificativo>();
	}

	/**
	 * @return the uidValutaEuro
	 */
	public Integer getUidValutaEuro() {
		return uidValutaEuro;
	}

	/**
	 * @param uidValutaEuro the uidValutaEuro to set
	 */
	public void setUidValutaEuro(Integer uidValutaEuro) {
		this.uidValutaEuro = uidValutaEuro;
	}

	/**
	 * @return the giustificativo
	 */
	public Giustificativo getGiustificativo() {
		return giustificativo;
	}

	/**
	 * @param giustificativo the giustificativo to set
	 */
	public void setGiustificativo(Giustificativo giustificativo) {
		this.giustificativo = giustificativo;
	}

	/**
	 * @return the rowNumber
	 */
	public Integer getRowNumber() {
		return rowNumber;
	}

	/**
	 * @param rowNumber the rowNumber to set
	 */
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}


	/**
	 * @return the listaTipoGiustificativo
	 */
	public List<TipoGiustificativo> getListaTipoGiustificativo() {
		return listaTipoGiustificativo;
	}

	/**
	 * @param listaTipoGiustificativo the listaTipoGiustificativo to set
	 */
	public void setListaTipoGiustificativo(List<TipoGiustificativo> listaTipoGiustificativo) {
		this.listaTipoGiustificativo = listaTipoGiustificativo != null ? listaTipoGiustificativo : new ArrayList<TipoGiustificativo>();
	}


	/**
	 * @return the listaValuta
	 */
	public List<Valuta> getListaValuta() {
		return listaValuta;
	}

	/**
	 * @param listaValuta the listaValuta to set
	 */
	public void setListaValuta(List<Valuta> listaValuta) {
		this.listaValuta = listaValuta != null ? listaValuta : new ArrayList<Valuta>();
	}
	
	/**
	 * @return the totaleImportiGiustificativi
	 */
	public BigDecimal getTotaleImportiGiustificativi() {
		BigDecimal totale = BigDecimal.ZERO;
		for(Giustificativo g : getListaGiustificativo()) {
			if(g.getImportoGiustificativo() != null) {
				totale = totale.add(g.getImportoGiustificativo());
			}
		}
		return totale;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaValuta}.
	 * 
	 * @return la request creata
	 */
	public RicercaValuta creaRequestRicercaValuta() {
		RicercaValuta request = creaRequest(RicercaValuta.class);
		request.setEnte(getEnte());
		return request;
	}
	
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoGiustificativo}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoGiustificativo creaRequestRicercaTipoGiustificativo() {
		return creaRequestRicercaTipoGiustificativo(TipologiaGiustificativo.RIMBORSO);
	}
	
}
