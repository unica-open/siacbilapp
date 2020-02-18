/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaRichiesteAnticipoMissioniNonErogate;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * Classe di model per la ricerca delle missioni da caricamento esterno.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/12/2015
 *
 */
public class RicercaMissioneDaEsternoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1429572074591296961L;
	
	private RichiestaEconomale richiestaEconomale;
	private List<RichiestaEconomale> listaRichiestaEconomale = new ArrayList<RichiestaEconomale>();
	
	/** Default empty contructor */
	public RicercaMissioneDaEsternoModel() {
		setTitolo("Ricerca missioni da esterno");
	}

	/**
	 * @return the richiestaEconomale
	 */
	public RichiestaEconomale getRichiestaEconomale() {
		return richiestaEconomale;
	}

	/**
	 * @param richiestaEconomale the richiestaEconomale to set
	 */
	public void setRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		this.richiestaEconomale = richiestaEconomale;
	}

	/**
	 * @return the listaRichiestaEconomale
	 */
	public List<RichiestaEconomale> getListaRichiestaEconomale() {
		return listaRichiestaEconomale;
	}

	/**
	 * @param listaRichiestaEconomale the listaRichiestaEconomale to set
	 */
	public void setListaRichiestaEconomale(List<RichiestaEconomale> listaRichiestaEconomale) {
		this.listaRichiestaEconomale = listaRichiestaEconomale != null ? listaRichiestaEconomale : new ArrayList<RichiestaEconomale>();
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaRichiesteAnticipoMissioniNonErogate}.
	 * 
	 * @param caricaDettaglioGiustificativo se caricare il dettaglio del giustificativo
	 * @return la request creata
	 */
	public RicercaRichiesteAnticipoMissioniNonErogate creaRequestRicercaRichiesteAnticipoMissioniNonErogate(boolean caricaDettaglioGiustificativo) {
		RicercaRichiesteAnticipoMissioniNonErogate request = creaRequest(RicercaRichiesteAnticipoMissioniNonErogate.class);
		request.setCaricaDettaglioGiustificativi(caricaDettaglioGiustificativo);
		return request;
	}
}
