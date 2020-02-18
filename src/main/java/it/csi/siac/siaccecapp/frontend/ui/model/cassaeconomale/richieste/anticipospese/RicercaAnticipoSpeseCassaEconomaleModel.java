/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRicercaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.model.RichiestaEconomaleModelDetail;


/**
 * Classe di model per la ricerca dell'anticipo spese.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 */
public class RicercaAnticipoSpeseCassaEconomaleModel extends BaseRicercaRichiestaEconomaleModel {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = -9107250864303115287L;

	/** Costruttore vuoto di default */
	public RicercaAnticipoSpeseCassaEconomaleModel() {
		setTitolo("Ricerca anticipo spese");
	}

	/**
	 * @return the urlRicerca
	 */
	public String getUrlRicerca() {
		return "effettuaRicercaAnticipoSpeseCassaEconomale";
	}
	
	/**
	 * @return the denominazioneRicerca
	 */
	public String getDenominazioneRicerca() {
		return getTitolo();
	}
	
	@Override
	protected RichiestaEconomaleModelDetail[] getRichiestaEconomaleModelDetails() {
		return new RichiestaEconomaleModelDetail[] {
				RichiestaEconomaleModelDetail.StatoOperativo,
				RichiestaEconomaleModelDetail.RendicontoConMovimentoBase,
				RichiestaEconomaleModelDetail.Movimenti,
				RichiestaEconomaleModelDetail.Sospeso,
				RichiestaEconomaleModelDetail.Soggetto};
	}
}
