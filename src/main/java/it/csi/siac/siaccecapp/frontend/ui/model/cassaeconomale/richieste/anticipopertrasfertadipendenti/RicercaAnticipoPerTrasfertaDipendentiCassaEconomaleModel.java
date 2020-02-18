/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipopertrasfertadipendenti;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRicercaRichiestaEconomaleModel;


/**
 * Classe di model per la ricerca dell'anticipo spese per trasferta.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 */
public class RicercaAnticipoPerTrasfertaDipendentiCassaEconomaleModel extends BaseRicercaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1218475195240999125L;

	/** Costruttore vuoto di default */
	public RicercaAnticipoPerTrasfertaDipendentiCassaEconomaleModel() {
		setTitolo("Ricerca anticipo per trasferta dipendenti");
	}

	/**
	 * @return the urlRicerca
	 */
	public String getUrlRicerca() {
		return "effettuaRicercaAnticipoPerTrasfertaDipendentiCassaEconomale";
	}
	
	/**
	 * @return the denominazioneRicerca
	 */
	public String getDenominazioneRicerca() {
		return getTitolo();
	}
	
}
