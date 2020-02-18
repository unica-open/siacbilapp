/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.rimborsospese;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRicercaRichiestaEconomaleModel;


/**
 * Classe di model per la ricerca del rimborso spese.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 */
public class RicercaRimborsoSpeseCassaEconomaleModel extends BaseRicercaRichiestaEconomaleModel {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = -4504943333215161060L;

	/** Costruttore vuoto di default */
	public RicercaRimborsoSpeseCassaEconomaleModel() {
		setTitolo("Ricerca rimborso spese");
	}

	/**
	 * @return the urlRicerca
	 */
	public String getUrlRicerca() {
		return "effettuaRicercaRimborsoSpeseCassaEconomale";
	}
	
	/**
	 * @return the denominazioneRicerca
	 */
	public String getDenominazioneRicerca() {
		return getTitolo();
	}
	
}
