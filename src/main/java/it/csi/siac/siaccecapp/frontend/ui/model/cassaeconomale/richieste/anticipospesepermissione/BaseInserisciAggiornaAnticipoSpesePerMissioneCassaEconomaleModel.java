/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseInserisciAggiornaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaMezziDiTrasporto;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.MezziDiTrasporto;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe base di model per l'inserimento e l'aggiornamento dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/01/2015
 *
 */
public abstract class BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleModel extends BaseInserisciAggiornaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1338725861862028342L;
	
	// Step 1
	private List<MezziDiTrasporto> mezziDiTrasportoSelezionati = new ArrayList<MezziDiTrasporto>();
	private List<Giustificativo> listaGiustificativo = new ArrayList<Giustificativo>();

	private Integer uidValutaEuro;
	private Giustificativo giustificativo;
	private Integer rowNumber;
	
	private List<MezziDiTrasporto> listaMezziDiTrasporto = new ArrayList<MezziDiTrasporto>();
	private List<TipoGiustificativo> listaTipoGiustificativo = new ArrayList<TipoGiustificativo>();
	private List<Valuta> listaValuta = new ArrayList<Valuta>();
	
	
	/**
	 * @return the mezziDiTrasportoSelezionati
	 */
	public List<MezziDiTrasporto> getMezziDiTrasportoSelezionati() {
		return mezziDiTrasportoSelezionati;
	}

	/**
	 * @param mezziDiTrasportoSelezionati the mezziDiTrasportoSelezionati to set
	 */
	public void setMezziDiTrasportoSelezionati(List<MezziDiTrasporto> mezziDiTrasportoSelezionati) {
		this.mezziDiTrasportoSelezionati = mezziDiTrasportoSelezionati != null ? mezziDiTrasportoSelezionati : new ArrayList<MezziDiTrasporto>();
	}

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
	 * @return the listaMezziDiTrasporto
	 */
	public List<MezziDiTrasporto> getListaMezziDiTrasporto() {
		return listaMezziDiTrasporto;
	}

	/**
	 * @param listaMezziDiTrasporto the listaMezziDiTrasporto to set
	 */
	public void setListaMezziDiTrasporto(List<MezziDiTrasporto> listaMezziDiTrasporto) {
		this.listaMezziDiTrasporto = listaMezziDiTrasporto != null ? listaMezziDiTrasporto : new ArrayList<MezziDiTrasporto>();
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
	
	/**
	 * @return the totaleImportiSpettantiGiustificativi
	 */
	public BigDecimal getTotaleImportiSpettantiGiustificativi() {
		BigDecimal totale = BigDecimal.ZERO;
		for(Giustificativo g : getListaGiustificativo()) {
			if(g.getImportoGiustificativo() != null) {
				totale = totale.add(g.getImportoSpettanteAnticipoMissioneNotNull());
			}
		}
		return totale;
	}
	
	@Override
	protected String computaStringaSospeso(RichiestaEconomale richiestaEconomale) {
		String result = "";
		if(richiestaEconomale.getSospeso() != null) {
			result = " - Sospeso N. " + richiestaEconomale.getSospeso().getNumeroSospeso();
		}
		return result;
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
	 * Crea una request per il servizio di {@link RicercaMezziDiTrasporto}.
	 * 
	 * @return la request creata
	 */
	public RicercaMezziDiTrasporto creaRequestRicercaMezziDiTrasporto() {
		return creaRequest(RicercaMezziDiTrasporto.class);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoGiustificativo}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoGiustificativo creaRequestRicercaTipoGiustificativo() {
		return creaRequestRicercaTipoGiustificativo(TipologiaGiustificativo.ANTICIPO);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoGiustificativo} per l'anticipo missione.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoGiustificativo creaRequestRicercaTipoGiustificativoAnticipoMissione() {
		return creaRequestRicercaTipoGiustificativo(TipologiaGiustificativo.ANTICIPO_MISSIONE);
	}
	
	
}
