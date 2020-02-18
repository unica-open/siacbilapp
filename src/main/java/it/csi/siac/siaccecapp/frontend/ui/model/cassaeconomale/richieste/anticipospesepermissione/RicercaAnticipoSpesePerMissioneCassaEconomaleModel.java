/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRicercaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.model.RichiestaEconomaleModelDetail;


/**
 * Classe di model per la ricerca dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/01/2015
 * @version 1.0.1 - 02/02/2015 - rifattorizzazione
 */
public class RicercaAnticipoSpesePerMissioneCassaEconomaleModel extends BaseRicercaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7578475195240999125L;

	/** Costruttore vuoto di default */
	public RicercaAnticipoSpesePerMissioneCassaEconomaleModel() {
		setTitolo("Ricerca anticipo spese per missione");
	}

	/**
	 * @return the urlRicerca
	 */
	public String getUrlRicerca() {
		return "effettuaRicercaAnticipoSpesePerMissioneCassaEconomale";
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
