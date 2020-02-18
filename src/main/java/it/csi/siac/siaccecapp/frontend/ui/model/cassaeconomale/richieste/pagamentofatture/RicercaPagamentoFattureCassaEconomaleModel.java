/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamentofatture;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRicercaRichiestaEconomaleModel;


/**
 * Classe di model per la consultazione del pagamento fatture.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 */
public class RicercaPagamentoFattureCassaEconomaleModel extends BaseRicercaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7368012718084906292L;

	/** Costruttore vuoto di default */
	public RicercaPagamentoFattureCassaEconomaleModel() {
		setTitolo("Ricerca pagamento fatture");
	}

	/**
	 * @return the urlRicerca
	 */
	public String getUrlRicerca() {
		return "effettuaRicercaPagamentoFattureCassaEconomale";
	}
	
	/**
	 * @return the denominazioneRicerca
	 */
	public String getDenominazioneRicerca() {
		return getTitolo();
	}
	
}
