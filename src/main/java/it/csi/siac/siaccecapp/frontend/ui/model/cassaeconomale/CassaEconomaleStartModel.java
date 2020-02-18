/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.TipologiaRichiestaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaCassaEconomale;
import it.csi.siac.siaccecser.model.CassaEconomale;

/**
 * Classe di model per la pagina iniziale della cassa economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/12/2014
 *
 */
public class CassaEconomaleStartModel extends CassaEconomaleBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4045564492417822217L;
	
	private boolean necessarioSelezionareCassa = true;
	private List<CassaEconomale> listaCassaEconomale = new ArrayList<CassaEconomale>();
	
	private List<TipologiaRichiestaCassaEconomale> listaTipologiaRichiestaCassaEconomale = new ArrayList<TipologiaRichiestaCassaEconomale>();
	private List<TipologiaRichiestaCassaEconomale> listaOperazioniExtra = new ArrayList<TipologiaRichiestaCassaEconomale>();
	
	/** Costruttore vuoto di default */
	public CassaEconomaleStartModel() {
		setTitolo("Cassa Economale");
	}

	/**
	 * @return the necessarioSelezionareCassa
	 */
	public boolean isNecessarioSelezionareCassa() {
		return necessarioSelezionareCassa;
	}

	/**
	 * @param necessarioSelezionareCassa the necessarioSelezionareCassa to set
	 */
	public void setNecessarioSelezionareCassa(boolean necessarioSelezionareCassa) {
		this.necessarioSelezionareCassa = necessarioSelezionareCassa;
	}

	/**
	 * @return the listaCassaEconomale
	 */
	public List<CassaEconomale> getListaCassaEconomale() {
		return listaCassaEconomale;
	}

	/**
	 * @param listaCassaEconomale the listaCassaEconomale to set
	 */
	public void setListaCassaEconomale(List<CassaEconomale> listaCassaEconomale) {
		this.listaCassaEconomale = listaCassaEconomale != null ? listaCassaEconomale : new ArrayList<CassaEconomale>();
	}

	/**
	 * @return the listaTipologiaRichiestaCassaEconomale
	 */
	public List<TipologiaRichiestaCassaEconomale> getListaTipologiaRichiestaCassaEconomale() {
		return listaTipologiaRichiestaCassaEconomale;
	}

	/**
	 * @param listaTipologiaRichiestaCassaEconomale the listaTipologiaRichiestaCassaEconomale to set
	 */
	public void setListaTipologiaRichiestaCassaEconomale(List<TipologiaRichiestaCassaEconomale> listaTipologiaRichiestaCassaEconomale) {
		this.listaTipologiaRichiestaCassaEconomale = listaTipologiaRichiestaCassaEconomale != null
			? listaTipologiaRichiestaCassaEconomale
			: new ArrayList<TipologiaRichiestaCassaEconomale>();
	}

	/**
	 * @return the listaOperazioniExtra
	 */
	public List<TipologiaRichiestaCassaEconomale> getListaOperazioniExtra() {
		return listaOperazioniExtra;
	}

	/**
	 * @param listaOperazioniExtra the listaOperazioniExtra to set
	 */
	public void setListaOperazioniExtra(List<TipologiaRichiestaCassaEconomale> listaOperazioniExtra) {
		this.listaOperazioniExtra = listaOperazioniExtra != null ? listaOperazioniExtra : new ArrayList<TipologiaRichiestaCassaEconomale>();
	}

	/* **** Requests **** */
	
	@Override
	public RicercaSinteticaCassaEconomale creaRequestRicercaSinteticaCassaEconomale() {
		RicercaSinteticaCassaEconomale request = creaRequest(RicercaSinteticaCassaEconomale.class);
		
		request.setBilancio(getBilancio());
		
		return request;
	}

}
