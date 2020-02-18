/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.RichiestaEconomale;


/**
 * Classe di model per la consultazione dell'anticipo spese .
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 */
public class ConsultaAnticipoSpeseCassaEconomaleModel extends BaseConsultaRichiestaEconomaleModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1424871655736681537L;

	private List<Giustificativo> listaGiustificativo = new ArrayList<Giustificativo>();
	
	/** Costruttore vuoto di default */
	public ConsultaAnticipoSpeseCassaEconomaleModel() {
		setTitolo("Consultazione anticipo spese");
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
		this.listaGiustificativo = listaGiustificativo;
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
		if(richiestaEconomale == null || richiestaEconomale.getSospeso() == null) {
			return "";
		}
		return " - Sospeso N. " + richiestaEconomale.getSospeso().getNumeroSospeso();
	}
}
